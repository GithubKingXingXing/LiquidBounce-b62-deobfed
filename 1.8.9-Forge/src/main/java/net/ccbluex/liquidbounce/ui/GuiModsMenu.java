
package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.script.GuiScripts;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiModsMenu extends GuiScreen
{
    private final GuiScreen prevGui;
    
    public GuiModsMenu(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48, "Forge Mods"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48 + 25, "Scripts"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48 + 50, "Back"));
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen((GuiScreen)new GuiModList((GuiScreen)this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen((GuiScreen)new GuiScripts(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Fonts.fontBold180.drawCenteredString("Mods", (int)(this.width / 2.0f), (int)(this.height / 8.0f + 5.0f), 4673984, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
