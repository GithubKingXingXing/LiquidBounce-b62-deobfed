// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

abstract class ScannerSupport extends IntHolder implements ErrorMessages
{
    protected final char[] chars;
    protected int p;
    protected int stop;
    private int lastFetched;
    protected int c;
    private final int begin;
    private final int end;
    protected int _p;
    private static final int INT_SIGN_BIT = Integer.MIN_VALUE;
    
    protected ScannerSupport(final char[] chars, final int p, final int end) {
        this.chars = chars;
        this.begin = p;
        this.end = end;
        this.reset();
    }
    
    protected int getBegin() {
        return this.begin;
    }
    
    protected int getEnd() {
        return this.end;
    }
    
    protected final int scanUnsignedNumber() {
        final int last = this.c;
        int num = 0;
        while (this.left()) {
            this.fetch();
            if (!Character.isDigit(this.c)) {
                this.unfetch();
                break;
            }
            final int onum = num;
            num = num * 10 + EncodingHelper.digitVal(this.c);
            if (((onum ^ num) & Integer.MIN_VALUE) != 0x0) {
                return -1;
            }
        }
        this.c = last;
        return num;
    }
    
    protected final int scanUnsignedHexadecimalNumber(final int maxLength) {
        final int last = this.c;
        int num = 0;
        int ml = maxLength;
        while (this.left() && ml-- != 0) {
            this.fetch();
            if (!EncodingHelper.isXDigit(this.c)) {
                this.unfetch();
                break;
            }
            final int onum = num;
            final int val = EncodingHelper.xdigitVal(this.c);
            num = (num << 4) + val;
            if (((onum ^ num) & Integer.MIN_VALUE) != 0x0) {
                return -1;
            }
        }
        this.c = last;
        return num;
    }
    
    protected final int scanUnsignedOctalNumber(final int maxLength) {
        final int last = this.c;
        int num = 0;
        int ml = maxLength;
        while (this.left() && ml-- != 0) {
            this.fetch();
            if (!Character.isDigit(this.c) || this.c >= 56) {
                this.unfetch();
                break;
            }
            final int onum = num;
            final int val = EncodingHelper.odigitVal(this.c);
            num = (num << 3) + val;
            if (((onum ^ num) & Integer.MIN_VALUE) != 0x0) {
                return -1;
            }
        }
        this.c = last;
        return num;
    }
    
    protected final void reset() {
        this.p = this.begin;
        this.stop = this.end;
    }
    
    protected final void mark() {
        this._p = this.p;
    }
    
    protected final void restore() {
        this.p = this._p;
    }
    
    protected final void inc() {
        this.lastFetched = this.p;
        ++this.p;
    }
    
    protected final void fetch() {
        this.lastFetched = this.p;
        this.c = this.chars[this.p++];
    }
    
    protected int fetchTo() {
        this.lastFetched = this.p;
        return this.chars[this.p++];
    }
    
    protected final void unfetch() {
        this.p = this.lastFetched;
    }
    
    protected final int peek() {
        return (this.p < this.stop) ? this.chars[this.p] : '\0';
    }
    
    protected final boolean peekIs(final int ch) {
        return this.peek() == ch;
    }
    
    protected final boolean left() {
        return this.p < this.stop;
    }
}
