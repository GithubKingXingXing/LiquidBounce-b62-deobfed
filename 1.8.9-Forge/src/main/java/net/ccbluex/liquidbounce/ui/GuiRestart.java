
package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiRestart extends GuiScreen
{
    public void initGui() {
        final int j = this.height / 4 + 48;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, j + 48, "OK"));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString((FontRenderer)Fonts.font35, "Restart the client!", this.width / 2, this.height / 8 + 80, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        this.drawCenteredString((FontRenderer)Fonts.font35, "Client restart!", this.width / 2 / 2, this.height / 8 / 2 + 20, new Color(255, 0, 0).getRGB());
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.shutdown();
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
