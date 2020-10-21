//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.tools;

import net.ccbluex.liquidbounce.utils.TabUtils;
import java.io.IOException;
import java.io.File;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiPortScanner extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiTextField hostField;
    private GuiTextField minPortField;
    private GuiTextField maxPortField;
    private GuiTextField threadsField;
    private GuiButton buttonToggle;
    private boolean running;
    private String status;
    private String host;
    private int currentPort;
    private int maxPort;
    private int minPort;
    private int checkedPort;
    private final List<Integer> ports;
    
    public GuiPortScanner(final GuiScreen prevGui) {
        this.status = "§7Wating...";
        this.ports = new ArrayList<Integer>();
        this.prevGui = prevGui;
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        (this.hostField = new GuiTextField(0, (FontRenderer)Fonts.font40, this.width / 2 - 100, 60, 200, 20)).setFocused(true);
        this.hostField.setMaxStringLength(Integer.MAX_VALUE);
        this.hostField.setText("localhost");
        (this.minPortField = new GuiTextField(1, (FontRenderer)Fonts.font40, this.width / 2 - 100, 90, 90, 20)).setMaxStringLength(5);
        this.minPortField.setText(String.valueOf(1));
        (this.maxPortField = new GuiTextField(2, (FontRenderer)Fonts.font40, this.width / 2 + 10, 90, 90, 20)).setMaxStringLength(5);
        this.maxPortField.setText(String.valueOf(65535));
        (this.threadsField = new GuiTextField(3, (FontRenderer)Fonts.font40, this.width / 2 - 100, 120, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.threadsField.setText(String.valueOf(500));
        this.buttonList.add(this.buttonToggle = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 95, this.running ? "Stop" : "Start"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Back"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 155, "Export"));
        super.initGui();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString((FontRenderer)Fonts.font40, "Port Scanner", this.width / 2, 34, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font35, this.running ? ("§7" + this.checkedPort + " §8/ §7" + this.maxPort) : ((this.status == null) ? "" : this.status), this.width / 2, this.height / 4 + 80, 16777215);
        this.buttonToggle.displayString = (this.running ? "Stop" : "Start");
        this.hostField.drawTextBox();
        this.minPortField.drawTextBox();
        this.maxPortField.drawTextBox();
        this.threadsField.drawTextBox();
        this.drawString((FontRenderer)Fonts.font40, "§c§lPorts:", 2, 2, Color.WHITE.hashCode());
        synchronized (this.ports) {
            int i = 12;
            for (final Integer integer : this.ports) {
                this.drawString((FontRenderer)Fonts.font35, String.valueOf(integer), 2, i, Color.WHITE.hashCode());
                i += Fonts.font35.FONT_HEIGHT;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                if (this.running) {
                    this.running = false;
                }
                else {
                    this.host = this.hostField.getText();
                    if (this.host.isEmpty()) {
                        this.status = "§cInvalid host";
                        return;
                    }
                    try {
                        this.minPort = Integer.parseInt(this.minPortField.getText());
                    }
                    catch (NumberFormatException e3) {
                        this.status = "§cInvalid min port";
                        return;
                    }
                    try {
                        this.maxPort = Integer.parseInt(this.maxPortField.getText());
                    }
                    catch (NumberFormatException e3) {
                        this.status = "§cInvalid max port";
                        return;
                    }
                    int threads;
                    try {
                        threads = Integer.parseInt(this.threadsField.getText());
                    }
                    catch (NumberFormatException e4) {
                        this.status = "§cInvalid threads";
                        return;
                    }
                    this.ports.clear();
                    this.currentPort = this.minPort - 1;
                    this.checkedPort = this.minPort;
                    for (int i = 0; i < threads; ++i) {
                        int port;
                        Socket socket;
                        new Thread(() -> {
                            try {
                                while (this.running && this.currentPort < this.maxPort) {
                                    ++this.currentPort;
                                    port = this.currentPort;
                                    try {
                                        socket = new Socket();
                                        socket.connect(new InetSocketAddress(this.host, port), 500);
                                        socket.close();
                                        synchronized (this.ports) {
                                            if (!this.ports.contains(port)) {
                                                this.ports.add(port);
                                            }
                                        }
                                    }
                                    catch (Exception ex) {}
                                    if (this.checkedPort < port) {
                                        this.checkedPort = port;
                                    }
                                }
                                this.running = false;
                                this.buttonToggle.displayString = "Start";
                            }
                            catch (Exception e) {
                                this.status = "§a§l" + e.getClass().getSimpleName() + ": §c" + e.getMessage();
                            }
                            return;
                        }).start();
                    }
                    this.running = true;
                }
                this.buttonToggle.displayString = (this.running ? "Stop" : "Start");
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
                final int returnValue = jFileChooser.showSaveDialog(null);
                if (returnValue != 0) {
                    break;
                }
                final File selectedFile = jFileChooser.getSelectedFile();
                if (selectedFile.isDirectory()) {
                    return;
                }
                try {
                    if (!selectedFile.exists()) {
                        selectedFile.createNewFile();
                    }
                    final FileWriter fileWriter = new FileWriter(selectedFile);
                    fileWriter.write("Portscan\r\n");
                    fileWriter.write("Host: " + this.host + "\r\n\r\n");
                    fileWriter.write("Ports (" + this.minPort + " - " + this.maxPort + "):\r\n");
                    for (final Integer integer : this.ports) {
                        fileWriter.write(String.valueOf(integer) + "\r\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Exported successful!", "Port Scanner", 1);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e2.getClass().getName() + "\nMessage: " + e2.getMessage());
                }
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
        if (15 == keyCode) {
            TabUtils.tab(this.hostField, this.minPortField, this.maxPortField);
        }
        if (this.running) {
            return;
        }
        if (this.hostField.isFocused()) {
            this.hostField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.minPortField.isFocused() && !Character.isLetter(typedChar)) {
            this.minPortField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.maxPortField.isFocused() && !Character.isLetter(typedChar)) {
            this.maxPortField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.threadsField.isFocused() && !Character.isLetter(typedChar)) {
            this.threadsField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.hostField.mouseClicked(mouseX, mouseY, mouseButton);
        this.minPortField.mouseClicked(mouseX, mouseY, mouseButton);
        this.maxPortField.mouseClicked(mouseX, mouseY, mouseButton);
        this.threadsField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.running = false;
        super.onGuiClosed();
    }
}
