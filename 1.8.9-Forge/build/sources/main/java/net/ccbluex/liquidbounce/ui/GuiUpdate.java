
package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.ui.mainmenu.GuiMainMenu;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiUpdate extends GuiScreen
{
    public void initGui() {
        final int j = this.height / 4 + 48;
        this.buttonList.add(new GuiButton(1, this.width / 2 + 2, j + 48, 98, 20, "OK"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, j + 48, 98, 20, "Download"));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString((FontRenderer)Fonts.font35, "b" + LiquidBounce.CLIENT.latestVersion + " got released!", this.width / 2, this.height / 8 + 80, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font35, "Press \"Download\" to visit our website or dismiss this message by pressing \"OK\".", this.width / 2, this.height / 8 + 80 + Fonts.font35.FONT_HEIGHT, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        this.drawCenteredString((FontRenderer)Fonts.font35, "New update available!", this.width / 2 / 2, this.height / 8 / 2 + 20, new Color(255, 0, 0).getRGB());
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
                break;
            }
            case 2: {
                MiscUtils.showURL("https://liquidbounce.net/#download");
                break;
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
