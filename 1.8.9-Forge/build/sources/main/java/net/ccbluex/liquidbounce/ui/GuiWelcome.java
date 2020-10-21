//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import net.ccbluex.liquidbounce.ui.mainmenu.GuiMainMenu;
import java.io.IOException;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWelcome extends GuiScreen
{
    public void initGui() {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, "Ok"));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Fonts.font35.drawCenteredString("Thank you for downloading and installing our client!", this.width / 2, this.height / 8 + 70, 16777215, true);
        Fonts.font35.drawCenteredString("Here is some information you might need if you are using liquidbounce for the first time.", this.width / 2, this.height / 8 + 70 + Fonts.font35.FONT_HEIGHT, 16777215, true);
        Fonts.font35.drawCenteredString("§lClickGUI:", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 3, 16777215, true);
        Fonts.font35.drawCenteredString("Press " + Keyboard.getKeyName(ModuleManager.getModule(ClickGUI.class).getKeyBind()) + " to open up the ClickGUI", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 4, 16777215, true);
        Fonts.font35.drawCenteredString("Rightclick modules with a little + next to it to edit it's settings.", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 5, 16777215, true);
        Fonts.font35.drawCenteredString("Hover a module to see what it does.", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 6, 16777215, true);
        Fonts.font35.drawCenteredString("§lImportant Commands:", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 8, 16777215, true);
        Fonts.font35.drawCenteredString(".bind <module> <key> / .bind <module> none", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 9, 16777215, true);
        Fonts.font35.drawCenteredString(".autosettings <servername>", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 10, 16777215, true);
        Fonts.font35.drawCenteredString("§lNeed help? Feel free to contact us!", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 12, 16777215, true);
        Fonts.font35.drawCenteredString("YouTube: https://youtube.com/ccbluex", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 13, 16777215, true);
        Fonts.font35.drawCenteredString("Twitter: https://twitter.com/ccbluex", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 14, 16777215, true);
        Fonts.font35.drawCenteredString("Forum: https://forum.ccbluex.net/", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT * 15, 16777215, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        Fonts.font40.drawCenteredString("Welcome!", this.width / 2 / 2, this.height / 8 / 2 + 20, new Color(0, 140, 255).getRGB(), true);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
                break;
            }
        }
    }
}
