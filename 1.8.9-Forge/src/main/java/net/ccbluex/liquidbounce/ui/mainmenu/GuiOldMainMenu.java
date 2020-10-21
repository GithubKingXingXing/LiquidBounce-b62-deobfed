//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.mainmenu;

import java.awt.image.BufferedImage;
import java.io.File;
import net.ccbluex.liquidbounce.ui.GuiCredits;
import net.ccbluex.liquidbounce.ui.GuiModsMenu;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.ui.GuiServerStatus;
import net.ccbluex.liquidbounce.ui.altmanager.GuiAltManager;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;

public class GuiOldMainMenu extends GuiScreen implements GuiYesNoCallback
{
    public void initGui() {
        final int j = this.height / 4 + 48;
        this.buttonList.add(new GuiButton(100, this.width / 2 - 100, j + 24, 98, 20, "AltManager"));
        this.buttonList.add(new GuiButton(103, this.width / 2 + 2, j + 24, 98, 20, "Mods"));
        this.buttonList.add(new GuiButton(101, this.width / 2 - 100, j + 48, 98, 20, "Server Status"));
        this.buttonList.add(new GuiButton(102, this.width / 2 + 2, j + 48, 98, 20, "Change Wallpaper"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, j, 98, 20, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, j, 98, 20, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(new GuiButton(108, this.width / 2 - 100, j + 72, "Credits"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 96, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 96, 98, 20, I18n.format("menu.quit", new Object[0])));
        super.initGui();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Gui.drawRect(this.width / 2 - 115, this.height / 4 + 35, this.width / 2 + 115, this.height / 4 + 175, Integer.MIN_VALUE);
        this.drawString((FontRenderer)Fonts.font40, "§c§lMinecraft 1.8.9", 2, (int)(this.height / 2 * 2.0f - 20.0f), 16777215);
        this.drawString((FontRenderer)Fonts.font40, "§cCopyright Mojang AB.", 2, (int)(this.height / 2 * 2.0f - 10.0f), 16777215);
        final String credits = "§7Made by §8CCBlueX";
        this.drawString((FontRenderer)Fonts.font40, credits, this.width - Fonts.font40.getStringWidth(credits) - 2, this.height - 10, 16777215);
        Fonts.fontBold180.drawCenteredString("LiquidBounce", (int)(this.width / 2.0f), (int)(this.height / 8.0f), 4673984, true);
        Fonts.font35.drawCenteredString("b" + String.valueOf(62), (int)(this.width / 2.0f + 148.0f), (int)(this.height / 8.0f + Fonts.font35.FONT_HEIGHT - 10.0f), 16777215, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                break;
            }
            case 4: {
                this.mc.shutdown();
                break;
            }
            case 100: {
                this.mc.displayGuiScreen((GuiScreen)new GuiAltManager(this));
                break;
            }
            case 101: {
                this.mc.displayGuiScreen((GuiScreen)new GuiServerStatus(this));
                break;
            }
            case 102: {
                if (Keyboard.isKeyDown(42)) {
                    LiquidBounce.CLIENT.background = null;
                    LiquidBounce.CLIENT.fileManager.backgroundFile.delete();
                    return;
                }
                final JFileChooser jFileChooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(final Component parent) throws HeadlessException {
                        final JDialog jDialog = super.createDialog(parent);
                        jDialog.setAlwaysOnTop(true);
                        return jDialog;
                    }
                };
                jFileChooser.setFileSelectionMode(0);
                final int returnValue = jFileChooser.showOpenDialog(null);
                if (returnValue != 0) {
                    break;
                }
                final File selectedFile = jFileChooser.getSelectedFile();
                if (selectedFile.isDirectory()) {
                    return;
                }
                try {
                    Files.copy(selectedFile.toPath(), new FileOutputStream(LiquidBounce.CLIENT.fileManager.backgroundFile));
                    final BufferedImage image = ImageIO.read(new FileInputStream(LiquidBounce.CLIENT.fileManager.backgroundFile));
                    LiquidBounce.CLIENT.background = new ResourceLocation("LiquidBounce".toLowerCase() + "/background.png");
                    this.mc.getTextureManager().loadTexture(LiquidBounce.CLIENT.background, (ITextureObject)new DynamicTexture(image));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                    LiquidBounce.CLIENT.fileManager.backgroundFile.delete();
                }
                break;
            }
            case 103: {
                this.mc.displayGuiScreen((GuiScreen)new GuiModsMenu(this));
                break;
            }
            case 108: {
                this.mc.displayGuiScreen((GuiScreen)new GuiCredits(this));
                break;
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
    }
}
