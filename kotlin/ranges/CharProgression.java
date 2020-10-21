// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import kotlin.internal.ProgressionUtilKt;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import kotlin.collections.CharIterator;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00192\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0019B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0006H\u0016J\b\u0010\u0014\u001a\u00020\u0010H\u0016J\t\u0010\u0015\u001a\u00020\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0011\u0010\b\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a" }, d2 = { "Lkotlin/ranges/CharProgression;", "", "", "start", "endInclusive", "step", "", "(CCI)V", "first", "getFirst", "()C", "last", "getLast", "getStep", "()I", "equals", "", "other", "", "hashCode", "isEmpty", "iterator", "Lkotlin/collections/CharIterator;", "toString", "", "Companion", "kotlin-stdlib" })
public class CharProgression implements Iterable<Character>, KMappedMarker
{
    private final char first;
    private final char last;
    private final int step;
    public static final Companion Companion;
    
    public final char getFirst() {
        return this.first;
    }
    
    public final char getLast() {
        return this.last;
    }
    
    public final int getStep() {
        return this.step;
    }
    
    @NotNull
    @Override
    public CharIterator iterator() {
        return new CharProgressionIterator(this.first, this.last, this.step);
    }
    
    public boolean isEmpty() {
        return (this.step > 0) ? (this.first > this.last) : (this.first < this.last);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof CharProgression && ((this.isEmpty() && ((CharProgression)other).isEmpty()) || (this.first == ((CharProgression)other).first && this.last == ((CharProgression)other).last && this.step == ((CharProgression)other).step));
    }
    
    @Override
    public int hashCode() {
        return this.isEmpty() ? -1 : (31 * ('\u001f' * this.first + this.last) + this.step);
    }
    
    @NotNull
    @Override
    public String toString() {
        return (this.step > 0) ? (this.first + ".." + this.last + " step " + this.step) : (this.first + " downTo " + this.last + " step " + -this.step);
    }
    
    public CharProgression(final char start, final char endInclusive, final int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step must be non-zero.");
        }
        if (step == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.first = start;
        this.last = (char)ProgressionUtilKt.getProgressionLastElement(start, endInclusive, step);
        this.step = step;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¨\u0006\n" }, d2 = { "Lkotlin/ranges/CharProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/CharProgression;", "rangeStart", "", "rangeEnd", "step", "", "kotlin-stdlib" })
    public static final class Companion
    {
        @NotNull
        public final CharProgression fromClosedRange(final char rangeStart, final char rangeEnd, final int step) {
            return new CharProgression(rangeStart, rangeEnd, step);
        }
        
        private Companion() {
        }
    }
}
