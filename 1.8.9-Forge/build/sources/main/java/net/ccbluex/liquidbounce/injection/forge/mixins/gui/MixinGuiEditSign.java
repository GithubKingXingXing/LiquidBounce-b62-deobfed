//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatAllowedCharacters;
import java.io.IOException;
import java.awt.Color;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.inventory.GuiEditSign;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiScreen;

@Mixin({ GuiEditSign.class })
public class MixinGuiEditSign extends GuiScreen
{
    @Shadow
    private int editLine;
    @Shadow
    private TileEntitySign tileSign;
    @Shadow
    private GuiButton doneBtn;
    private boolean enabled;
    private GuiButton toggleButton;
    private GuiTextField signCommand1;
    private GuiTextField signCommand2;
    private GuiTextField signCommand3;
    private GuiTextField signCommand4;
    
    @Inject(method = { "initGui" }, at = { @At("RETURN") })
    private void initGui(final CallbackInfo callbackInfo) {
        this.buttonList.add(this.toggleButton = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 145, this.enabled ? "Disable Formatting codes" : "Enable Formatting codes"));
        this.signCommand1 = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, this.height - 15, 200, 10);
        this.signCommand2 = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, this.height - 30, 200, 10);
        this.signCommand3 = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height - 45, 200, 10);
        this.signCommand4 = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 100, this.height - 60, 200, 10);
        this.signCommand1.setText("");
        this.signCommand2.setText("");
        this.signCommand3.setText("");
        this.signCommand4.setText("");
    }
    
    @Inject(method = { "actionPerformed" }, at = { @At("HEAD") })
    private void actionPerformed(final GuiButton button, final CallbackInfo callbackInfo) {
        switch (button.id) {
            case 0: {
                if (!this.signCommand1.getText().isEmpty()) {
                    this.tileSign.signText[0].setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand1.getText())));
                }
                if (!this.signCommand2.getText().isEmpty()) {
                    this.tileSign.signText[1].setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand2.getText())));
                }
                if (!this.signCommand3.getText().isEmpty()) {
                    this.tileSign.signText[2].setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand3.getText())));
                }
                if (!this.signCommand4.getText().isEmpty()) {
                    this.tileSign.signText[3].setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.signCommand4.getText())));
                    break;
                }
                break;
            }
            case 1: {
                this.enabled = !this.enabled;
                this.toggleButton.displayString = (this.enabled ? "Disable Formatting codes" : "Enable Formatting codes");
                break;
            }
        }
    }
    
    @Inject(method = { "drawScreen" }, at = { @At("RETURN") })
    private void drawFields(final CallbackInfo callbackInfo) {
        this.fontRendererObj.drawString("§c§lCommands §7(§f§l1.8§7)", this.width / 2 - 100, this.height - 75, Color.WHITE.getRGB());
        this.signCommand1.drawTextBox();
        this.signCommand2.drawTextBox();
        this.signCommand3.drawTextBox();
        this.signCommand4.drawTextBox();
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.signCommand1.mouseClicked(mouseX, mouseY, mouseButton);
        this.signCommand2.mouseClicked(mouseX, mouseY, mouseButton);
        this.signCommand3.mouseClicked(mouseX, mouseY, mouseButton);
        this.signCommand4.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Overwrite
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.signCommand1.textboxKeyTyped(typedChar, keyCode);
        this.signCommand2.textboxKeyTyped(typedChar, keyCode);
        this.signCommand3.textboxKeyTyped(typedChar, keyCode);
        this.signCommand4.textboxKeyTyped(typedChar, keyCode);
        if (this.signCommand1.isFocused() || this.signCommand2.isFocused() || this.signCommand3.isFocused() || this.signCommand4.isFocused()) {
            return;
        }
        if (keyCode == 200) {
            this.editLine = (this.editLine - 1 & 0x3);
        }
        if (keyCode == 208 || keyCode == 28 || keyCode == 156) {
            this.editLine = (this.editLine + 1 & 0x3);
        }
        String s = this.tileSign.signText[this.editLine].getUnformattedText();
        if (keyCode == 14 && s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
        if ((ChatAllowedCharacters.isAllowedCharacter(typedChar) || (this.enabled && typedChar == '§')) && this.fontRendererObj.getStringWidth(s + typedChar) <= 90) {
            s += typedChar;
        }
        this.tileSign.signText[this.editLine] = (IChatComponent)new ChatComponentText(s);
        if (keyCode == 1) {
            this.actionPerformed(this.doneBtn);
        }
    }
}
