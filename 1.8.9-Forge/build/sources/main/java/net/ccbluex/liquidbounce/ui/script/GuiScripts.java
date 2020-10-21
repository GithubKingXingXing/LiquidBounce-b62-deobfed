
package net.ccbluex.liquidbounce.ui.script;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Enumeration;
import java.net.URL;
import java.awt.Desktop;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.ZipFile;
import net.ccbluex.liquidbounce.ui.clickgui.ClickGui;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;

public class GuiScripts extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiList list;
    
    public GuiScripts(final GuiScreen gui) {
        this.prevGui = gui;
    }
    
    public void initGui() {
        (this.list = new GuiList(this)).registerScrollButtons(7, 8);
        this.list.elementClicked(-1, false, 0, 0);
        final int j = 22;
        final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.buttonList.add(new GuiButton(0, scaledResolution.getScaledWidth() - 80, scaledResolution.getScaledHeight() - 65, 70, 20, "Back"));
        this.buttonList.add(new GuiButton(1, scaledResolution.getScaledWidth() - 80, j + 24, 70, 20, "Import"));
        this.buttonList.add(new GuiButton(2, scaledResolution.getScaledWidth() - 80, j + 48, 70, 20, "Delete"));
        this.buttonList.add(new GuiButton(3, scaledResolution.getScaledWidth() - 80, j + 72, 70, 20, "Reload"));
        this.buttonList.add(new GuiButton(4, scaledResolution.getScaledWidth() - 80, j + 96, 70, 20, "Folder"));
        this.buttonList.add(new GuiButton(5, scaledResolution.getScaledWidth() - 80, j + 120, 70, 20, "Wiki"));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString((FontRenderer)Fonts.font40, "§9§lScripts", this.width / 2, 28, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                try {
                    final JFileChooser fileChooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(final Component parent) throws HeadlessException {
                            final JDialog jDialog = super.createDialog(parent);
                            jDialog.setAlwaysOnTop(true);
                            return jDialog;
                        }
                    };
                    fileChooser.setFileSelectionMode(0);
                    final int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == 0) {
                        final File file = fileChooser.getSelectedFile();
                        final String fileName = file.getName();
                        if (fileName.endsWith(".js")) {
                            LiquidBounce.CLIENT.scriptManager.importScript(file);
                            LiquidBounce.CLIENT.moduleManager.sortModules();
                            LiquidBounce.CLIENT.clickGui = new ClickGui();
                            LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
                        }
                        else if (fileName.endsWith(".zip")) {
                            final ZipFile zipFile = new ZipFile(file);
                            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
                            final List<File> scriptFiles = new ArrayList<File>();
                            while (entries.hasMoreElements()) {
                                final ZipEntry entry = (ZipEntry)entries.nextElement();
                                final String entryName = entry.getName();
                                final File entryFile = new File(LiquidBounce.CLIENT.scriptManager.getScriptsFolder(), entryName);
                                if (entry.isDirectory()) {
                                    entryFile.mkdir();
                                }
                                else {
                                    final InputStream fileStream = zipFile.getInputStream(entry);
                                    final FileOutputStream fileOutputStream = new FileOutputStream(entryFile);
                                    IOUtils.copy(fileStream, (OutputStream)fileOutputStream);
                                    fileOutputStream.close();
                                    fileStream.close();
                                    if (entryName.contains("/")) {
                                        continue;
                                    }
                                    scriptFiles.add(entryFile);
                                }
                            }
                            scriptFiles.forEach(scriptFile -> LiquidBounce.CLIENT.scriptManager.loadScript(scriptFile));
                            LiquidBounce.CLIENT.moduleManager.sortModules();
                            LiquidBounce.CLIENT.clickGui = new ClickGui();
                            LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
                            LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.hudConfig);
                        }
                        else {
                            MiscUtils.showErrorPopup("Wrong file extension.", "The file extension has to be .js or .zip");
                        }
                    }
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while importing a script.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 2: {
                try {
                    if (this.list.getSelectedSlot() != -1) {
                        final Script script = LiquidBounce.CLIENT.scriptManager.getScripts().get(this.list.getSelectedSlot());
                        LiquidBounce.CLIENT.scriptManager.deleteScript(script);
                        LiquidBounce.CLIENT.moduleManager.sortModules();
                        LiquidBounce.CLIENT.clickGui = new ClickGui();
                        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.clickGuiConfig);
                        LiquidBounce.CLIENT.fileManager.loadConfig(LiquidBounce.CLIENT.fileManager.hudConfig);
                    }
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while deleting a script.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 3: {
                try {
                    LiquidBounce.CLIENT.scriptManager.reloadScripts();
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 4: {
                try {
                    Desktop.getDesktop().open(LiquidBounce.CLIENT.scriptManager.getScriptsFolder());
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while trying to open your scripts folder.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 5: {
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/CCBlueX/LiquidBounce-ScriptAPI/wiki").toURI());
                }
                catch (Exception ex) {}
                break;
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    private class GuiList extends GuiSlot
    {
        private int selectedSlot;
        
        GuiList(final GuiScreen prevGui) {
            super(Minecraft.getMinecraft(), prevGui.width, prevGui.height, 40, prevGui.height - 40, 30);
        }
        
        protected boolean isSelected(final int id) {
            return this.selectedSlot == id;
        }
        
        int getSelectedSlot() {
            if (this.selectedSlot > LiquidBounce.CLIENT.scriptManager.getScripts().size()) {
                this.selectedSlot = -1;
            }
            return this.selectedSlot;
        }
        
        protected int getSize() {
            return LiquidBounce.CLIENT.scriptManager.getScripts().size();
        }
        
        protected void elementClicked(final int id, final boolean doubleClick, final int var3, final int var4) {
            this.selectedSlot = id;
        }
        
        protected void drawSlot(final int id, final int x, final int y, final int var4, final int var5, final int var6) {
            final Script script = LiquidBounce.CLIENT.scriptManager.getScripts().get(id);
            GuiScripts.this.drawCenteredString((FontRenderer)Fonts.font40, "§9" + script.getScriptName() + " §7v" + script.getScriptVersion(), this.width / 2, y + 2, Color.LIGHT_GRAY.getRGB());
            GuiScripts.this.drawCenteredString((FontRenderer)Fonts.font40, "by §c" + script.getScriptAuthor(), this.width / 2, y + 15, Color.LIGHT_GRAY.getRGB());
        }
        
        protected void drawBackground() {
        }
    }
}
