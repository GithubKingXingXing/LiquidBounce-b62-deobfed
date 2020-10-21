
package net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.thealtening;

import net.minecraft.client.gui.Gui;
import com.thealtening.utilities.SSLVerification;
import net.ccbluex.liquidbounce.ui.GuiPasswordField;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.LiquidFontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import org.jetbrains.annotations.NotNull;
import com.thealtening.domain.User;
import net.minecraft.client.gui.GuiTextField;
import kotlin.Metadata;
import net.minecraft.client.gui.GuiScreen;

@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0014J \u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\fH\u0016J\u0018\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0011H\u0014J \u0010\u001a\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u0011H\u0014J\b\u0010\u001c\u001a\u00020\fH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e" }, d2 = { "Lnet/ccbluex/liquidbounce/ui/altmanager/sub/altgenerator/thealtening/GuiTheAltening;", "Lnet/minecraft/client/gui/GuiScreen;", "prevScreen", "(Lnet/minecraft/client/gui/GuiScreen;)V", "apiKeyField", "Lnet/minecraft/client/gui/GuiTextField;", "status", "", "tokenField", "user", "Lcom/thealtening/domain/User;", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "onGuiClosed", "Companion", "LiquidBounce" })
public final class GuiTheAltening extends GuiScreen
{
    private GuiTextField apiKeyField;
    private GuiTextField tokenField;
    private String status;
    private User user;
    private final GuiScreen prevScreen;
    @NotNull
    private static String apiKey;
    public static final Companion Companion;
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 105, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 175, "Generate"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 60, "Back"));
        this.tokenField = new GuiTextField(666, (FontRenderer)Fonts.font40, this.width / 2 - 100, 80, 200, 20);
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField.setFocused(true);
        final GuiTextField tokenField2 = this.tokenField;
        if (tokenField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField2.setMaxStringLength(Integer.MAX_VALUE);
        this.apiKeyField = new GuiPasswordField(1337, Fonts.font40, this.width / 2 - 100, 150, 200, 20);
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField.setMaxStringLength(18);
        final GuiTextField apiKeyField2 = this.apiKeyField;
        if (apiKeyField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField2.setText(GuiTheAltening.apiKey);
        final SSLVerification sslVerification = new SSLVerification();
        sslVerification.verify();
        super.initGui();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Gui.drawRect(30, 30, this.width - 30, this.height - 30, Integer.MIN_VALUE);
        this.drawCenteredString((FontRenderer)Fonts.font35, "TheAltening", this.width / 2, 36, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font35, this.status, this.width / 2, this.height - 70, 16777215);
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField.drawTextBox();
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField.drawTextBox();
        this.drawCenteredString((FontRenderer)Fonts.font40, "§7API-Key:", this.width / 2 - 78, 137, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font40, "§7Token:", this.width / 2 - 84, 66, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void actionPerformed(@NotNull final GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.prevScreen);
                break;
            }
            case 1: {
                new Thread((Runnable)new GuiTheAltening$actionPerformed.GuiTheAltening$actionPerformed$1(this)).start();
                break;
            }
            case 2: {
                new Thread((Runnable)new GuiTheAltening$actionPerformed.GuiTheAltening$actionPerformed$2(this)).start();
                break;
            }
        }
        super.actionPerformed(button);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.prevScreen);
            return;
        }
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        if (apiKeyField.isFocused()) {
            final GuiTextField apiKeyField2 = this.apiKeyField;
            if (apiKeyField2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            }
            apiKeyField2.textboxKeyTyped(typedChar, keyCode);
        }
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        if (tokenField.isFocused()) {
            final GuiTextField tokenField2 = this.tokenField;
            if (tokenField2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            }
            tokenField2.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        apiKeyField.mouseClicked(mouseX, mouseY, mouseButton);
        final GuiTextField tokenField = this.tokenField;
        if (tokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        tokenField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        final GuiTextField apiKeyField = this.apiKeyField;
        if (apiKeyField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        final String getText = apiKeyField.getText();
        Intrinsics.checkExpressionValueIsNotNull(getText, "apiKeyField.text");
        GuiTheAltening.apiKey = getText;
        super.onGuiClosed();
    }
    
    public GuiTheAltening(@NotNull final GuiScreen prevScreen) {
        Intrinsics.checkParameterIsNotNull(prevScreen, "prevScreen");
        this.prevScreen = prevScreen;
        this.status = "";
    }
    
    static {
        Companion = new Companion(null);
        GuiTheAltening.apiKey = "";
    }
    
    public static final /* synthetic */ String access$getApiKey$cp() {
        return GuiTheAltening.apiKey;
    }
    
    public static final /* synthetic */ void access$setApiKey$cp(final String <set-?>) {
        GuiTheAltening.apiKey = <set-?>;
    }
    
    @Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\t" }, d2 = { "Lnet/ccbluex/liquidbounce/ui/altmanager/sub/altgenerator/thealtening/GuiTheAltening$Companion;", "", "()V", "apiKey", "", "getApiKey", "()Ljava/lang/String;", "setApiKey", "(Ljava/lang/String;)V", "LiquidBounce" })
    public static final class Companion
    {
        @NotNull
        public final String getApiKey() {
            return GuiTheAltening.access$getApiKey$cp();
        }
        
        public final void setApiKey(@NotNull final String <set-?>) {
            Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
            GuiTheAltening.access$setApiKey$cp(<set-?>);
        }
        
        private Companion() {
        }
    }
}
