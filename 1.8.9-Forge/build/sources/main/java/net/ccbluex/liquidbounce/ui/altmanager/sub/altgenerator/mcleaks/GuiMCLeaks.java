//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.mcleaks;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.mcleaks.MCLeaks;
import net.mcleaks.Session;
import net.mcleaks.RedeemResponse;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiMCLeaks extends GuiScreen
{
    private String message;
    private GuiTextField tokenField;
    private final GuiScreen prevGui;
    
    public GuiMCLeaks(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void updateScreen() {
        this.tokenField.updateCursorCounter();
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 150, this.height / 4 + 96, 300, 20, "Redeem Token"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 150, this.height / 4 + 120, 158, 20, "Get Token"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 12, this.height / 4 + 120, 138, 20, "Back"));
        (this.tokenField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 110, 200, 20)).setFocused(true);
        this.tokenField.setMaxStringLength(Integer.MAX_VALUE);
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (!button.enabled) {
            return;
        }
        switch (button.id) {
            case 1: {
                if (this.tokenField.getText().length() != 16) {
                    this.message = ChatColor.RED + "The token has to be 16 characters long!";
                    return;
                }
                button.enabled = false;
                button.displayString = "Please wait ...";
                RedeemResponse redeemResponse;
                MCLeaks.redeem(this.tokenField.getText(), o -> {
                    if (o instanceof String) {
                        this.message = ChatColor.RED + (String)o;
                        return;
                    }
                    else {
                        redeemResponse = o;
                        MCLeaks.refresh(new Session(redeemResponse.getUsername(), redeemResponse.getToken()));
                        this.message = ChatColor.GREEN + "Your token was redeemed successfully!";
                        button.enabled = true;
                        button.displayString = "Redeem Token";
                        return;
                    }
                });
                break;
            }
            case 2: {
                MiscUtils.showURL("https://mcleaks.net/");
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        this.tokenField.textboxKeyTyped(typedChar, keyCode);
        switch (keyCode) {
            case 15: {
                this.tokenField.setFocused(!this.tokenField.isFocused());
                break;
            }
            case 28: {
                this.actionPerformed(this.buttonList.get(1));
                break;
            }
            case 156: {
                this.actionPerformed(this.buttonList.get(1));
                break;
            }
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Gui.drawRect(30, 30, this.width - 30, this.height - 30, Integer.MIN_VALUE);
        this.drawCenteredString(this.mc.fontRendererObj, "MCLeaks", this.width / 2, 34, 16777215);
        this.drawCenteredString(this.fontRendererObj, MCLeaks.isAltActive() ? ("§aToken active. Using §9" + MCLeaks.getSession().getUsername() + "§a to login!") : "§7No Token redeemed.", this.width / 2, this.height / 4 + ((this.message != null) ? 74 : 84), 16777215);
        this.drawString(this.fontRendererObj, "Token", this.width / 2 - 100, 98, 10526880);
        if (this.message != null) {
            this.drawCenteredString(this.fontRendererObj, this.message, this.width / 2, this.height / 4 + 84, 16777215);
        }
        this.tokenField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
