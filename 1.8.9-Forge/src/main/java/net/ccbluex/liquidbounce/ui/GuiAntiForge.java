//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import java.io.IOException;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiAntiForge extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiButton enabledButton;
    private GuiButton fmlButton;
    private GuiButton proxyButton;
    private GuiButton payloadButton;
    
    public GuiAntiForge(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }
    
    public void initGui() {
        this.buttonList.add(this.enabledButton = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 35, "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")"));
        this.buttonList.add(this.fmlButton = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 50 + 25, "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")"));
        this.buttonList.add(this.proxyButton = new GuiButton(3, this.width / 2 - 100, this.height / 4 + 50 + 50, "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")"));
        this.buttonList.add(this.payloadButton = new GuiButton(4, this.width / 2 - 100, this.height / 4 + 50 + 75, "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 55 + 100 + 5, "Back"));
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                AntiForge.enabled = !AntiForge.enabled;
                this.enabledButton.displayString = "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 2: {
                AntiForge.blockFML = !AntiForge.blockFML;
                this.fmlButton.displayString = "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 3: {
                AntiForge.blockProxyPacket = !AntiForge.blockProxyPacket;
                this.proxyButton.displayString = "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 4: {
                AntiForge.blockPayloadPackets = !AntiForge.blockPayloadPackets;
                this.payloadButton.displayString = "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")";
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
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
        Fonts.fontBold180.drawCenteredString("AntiForge", (int)(this.width / 2.0f), (int)(this.height / 8.0f + 5.0f), 4673984, true);
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
