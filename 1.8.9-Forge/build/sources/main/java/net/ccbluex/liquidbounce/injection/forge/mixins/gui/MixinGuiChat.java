
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiChat.class })
public abstract class MixinGuiChat extends MixinGuiScreen
{
    @Shadow
    protected GuiTextField inputField;
    private float yPosOfInputField;
    private float fade;
    
    public MixinGuiChat() {
        this.fade = 0.0f;
    }
    
    @Inject(method = { "initGui" }, at = { @At("RETURN") })
    private void init(final CallbackInfo callbackInfo) {
        this.inputField.yPosition = this.height + 1;
        this.yPosOfInputField = (float)this.inputField.yPosition;
    }
    
    @Inject(method = { "keyTyped" }, at = { @At("RETURN") })
    private void updateLenght(final CallbackInfo callbackInfo) {
        final String getText = this.inputField.getText();
        LiquidBounce.CLIENT.commandManager.getClass();
        if (getText.startsWith(String.valueOf('.'))) {
            final String getText2 = this.inputField.getText();
            final StringBuilder sb = new StringBuilder();
            LiquidBounce.CLIENT.commandManager.getClass();
            if (!getText2.startsWith(sb.append(String.valueOf('.')).append("lc").toString())) {
                this.inputField.setMaxStringLength(10000);
                return;
            }
        }
        this.inputField.setMaxStringLength(100);
    }
    
    @Inject(method = { "updateScreen" }, at = { @At("HEAD") })
    private void updateScreen(final CallbackInfo callbackInfo) {
        final int delta = RenderUtils.deltaTime;
        if (this.fade < 14.0f) {
            this.fade += 0.08f * delta;
        }
        if (this.fade > 14.0f) {
            this.fade = 14.0f;
        }
        if (this.yPosOfInputField > this.height - 12) {
            this.yPosOfInputField -= 0.08f * delta;
        }
        if (this.yPosOfInputField < this.height - 12) {
            this.yPosOfInputField = (float)(this.height - 12);
        }
        this.inputField.yPosition = (int)this.yPosOfInputField;
    }
    
    @Overwrite
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Gui.drawRect(2, this.height - (int)this.fade, this.width - 2, this.height, Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        final IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (ichatcomponent != null) {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }
    }
}
