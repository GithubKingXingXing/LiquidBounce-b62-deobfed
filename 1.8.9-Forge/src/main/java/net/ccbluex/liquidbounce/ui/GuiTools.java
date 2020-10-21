//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.tools.GuiPortScanner;
import net.ccbluex.liquidbounce.ui.tools.GuiGUIControlBruteforce;
import net.ccbluex.liquidbounce.ui.tools.GuiRconBruteforce;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiTools extends GuiScreen
{
    private final GuiScreen prevGui;
    
    public GuiTools(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void initGui() {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Bruteforce - RCON"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48 + 25, "Bruteforce - GUIControl"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 48 + 50, "Port Scanner"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48 + 75 + 5, "Back"));
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen((GuiScreen)new GuiRconBruteforce(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen((GuiScreen)new GuiGUIControlBruteforce(this));
                break;
            }
            case 3: {
                this.mc.displayGuiScreen((GuiScreen)new GuiPortScanner(this));
                break;
            }
            case 0: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Fonts.fontBold180.drawCenteredString("Tools", (int)(this.width / 2.0f), (int)(this.height / 8.0f + 5.0f), 4673984, true);
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
