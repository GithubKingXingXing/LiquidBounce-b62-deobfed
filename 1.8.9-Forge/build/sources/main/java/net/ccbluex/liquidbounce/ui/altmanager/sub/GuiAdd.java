
package net.ccbluex.liquidbounce.ui.altmanager.sub;

import net.ccbluex.liquidbounce.utils.TabUtils;
import java.io.IOException;
import net.ccbluex.liquidbounce.file.FileConfig;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.ui.GuiPasswordField;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAdd extends GuiScreen
{
    private final GuiScreen prevGui;
    private GuiTextField username;
    private GuiPasswordField password;
    private GuiTextField up;
    private String status;
    
    public GuiAdd(final GuiScreen gui) {
        this.status = "§7Idle...";
        this.prevGui = gui;
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, "Add"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Back"));
        (this.username = new GuiTextField(2, (FontRenderer)Fonts.font40, this.width / 2 - 100, 60, 200, 20)).setFocused(true);
        this.username.setMaxStringLength(Integer.MAX_VALUE);
        (this.password = new GuiPasswordField(3, Fonts.font40, this.width / 2 - 100, 85, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        (this.up = new GuiTextField(4, (FontRenderer)Fonts.font40, this.width / 2 - 100, 110, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Gui.drawRect(30, 30, this.width - 30, this.height - 30, Integer.MIN_VALUE);
        this.drawCenteredString((FontRenderer)Fonts.font40, "Add Account", this.width / 2, 34, 16777215);
        this.drawCenteredString((FontRenderer)Fonts.font35, (this.status == null) ? "" : this.status, this.width / 2, this.height / 4 + 86, 16777215);
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.up.drawTextBox();
        if (this.username.getText().isEmpty()) {
            this.drawCenteredString(this.fontRendererObj, "§7Username / E-Mail", this.width / 2 - 50, 65, 16777215);
        }
        if (this.password.getText().isEmpty()) {
            this.drawCenteredString(this.fontRendererObj, "§7Password", this.width / 2 - 70, 90, 16777215);
        }
        if (this.up.getText().isEmpty()) {
            this.drawCenteredString(this.fontRendererObj, "§7E-Mail:Password", this.width / 2 - 55, 115, 16777215);
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
                if (this.username.getText().isEmpty() && this.up.getText().isEmpty()) {
                    this.status = "§cYou must fill out the username/email box.";
                    return;
                }
                if (LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.stream().anyMatch(account -> account.getName().equalsIgnoreCase(this.username.getText()))) {
                    this.status = "§cThis minecraftAccount is already in the list.";
                    break;
                }
                MinecraftAccount minecraftAccount;
                if (!this.up.getText().isEmpty()) {
                    final String[] accountString = this.up.getText().split(":");
                    if (accountString.length < 2) {
                        this.status = "§cSyntax error";
                        return;
                    }
                    minecraftAccount = new MinecraftAccount(accountString[0], accountString[1]);
                }
                else if (this.password.getText().isEmpty()) {
                    minecraftAccount = new MinecraftAccount(this.username.getText());
                }
                else {
                    minecraftAccount = new MinecraftAccount(this.username.getText(), this.password.getText());
                }
                boolean isWorking;
                if (!minecraftAccount.isCracked()) {
                    final YggdrasilUserAuthentication userAuthentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                    userAuthentication.setUsername(minecraftAccount.getName());
                    userAuthentication.setPassword(minecraftAccount.getPassword());
                    try {
                        userAuthentication.logIn();
                        minecraftAccount.setAccountName(userAuthentication.getSelectedProfile().getName());
                        isWorking = true;
                    }
                    catch (NullPointerException | AuthenticationException ex2) {
                        final Exception ex;
                        final Exception exception = ex;
                        isWorking = false;
                    }
                }
                else {
                    isWorking = true;
                }
                if (isWorking) {
                    LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts.add(minecraftAccount);
                    LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.accountsConfig);
                    this.status = "§aAccount was added.";
                    this.mc.displayGuiScreen(this.prevGui);
                    break;
                }
                this.status = "§cAccount is not working.";
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
            TabUtils.tab(this.username, this.password, this.up);
        }
        if (this.username.isFocused()) {
            this.username.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.password.isFocused()) {
            this.password.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.up.isFocused()) {
            this.up.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.username.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);
        this.up.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
}
