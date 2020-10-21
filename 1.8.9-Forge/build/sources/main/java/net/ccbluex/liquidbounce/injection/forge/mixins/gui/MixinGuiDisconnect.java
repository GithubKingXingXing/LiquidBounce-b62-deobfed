
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.util.List;
import com.thealtening.domain.Account;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.util.Session;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.thealtening.AltService;
import net.ccbluex.liquidbounce.ui.altmanager.GuiAltManager;
import com.thealtening.TheAltening;
import net.ccbluex.liquidbounce.ui.altmanager.sub.altgenerator.thealtening.GuiTheAltening;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiDisconnected;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiDisconnected.class })
public abstract class MixinGuiDisconnect extends MixinGuiScreen
{
    @Shadow
    private int field_175353_i;
    private GuiButton reconnectButton;
    private GuiButton forgeBypassButton;
    private int reconnectTimer;
    
    @Inject(method = { "initGui" }, at = { @At("RETURN") })
    private void initGui(final CallbackInfo callbackInfo) {
        this.reconnectTimer = 0;
        this.buttonList.add(this.reconnectButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 22, "Reconnect"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 44, 98, 20, "Random alt"));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 44, 98, 20, "Random username"));
        this.buttonList.add(this.forgeBypassButton = new GuiButton(2, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 66, "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off")));
    }
    
    @Inject(method = { "actionPerformed" }, at = { @At("HEAD") })
    private void actionPerformed(final GuiButton button, final CallbackInfo callbackInfo) {
        switch (button.id) {
            case 1: {
                ServerUtils.connectToLastServer();
                break;
            }
            case 2: {
                AntiForge.enabled = !AntiForge.enabled;
                this.forgeBypassButton.displayString = "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off");
                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.valuesConfig);
                break;
            }
            case 3: {
                if (!GuiTheAltening.Companion.getApiKey().isEmpty()) {
                    final String apiKey = GuiTheAltening.Companion.getApiKey();
                    final TheAltening theAltening = new TheAltening(apiKey);
                    try {
                        final Account account = theAltening.generateAccount(theAltening.getUser());
                        GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                        final YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                        yggdrasilUserAuthentication.setUsername(account.getToken());
                        yggdrasilUserAuthentication.setPassword("LiquidBounce");
                        yggdrasilUserAuthentication.logIn();
                        this.mc.session = new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
                        ServerUtils.connectToLastServer();
                        break;
                    }
                    catch (Throwable throwable) {
                        ClientUtils.getLogger().error("Failed to login into random account from TheAltening.", throwable);
                    }
                }
                final List<MinecraftAccount> accounts = LiquidBounce.CLIENT.fileManager.accountsConfig.altManagerMinecraftAccounts;
                if (accounts.isEmpty()) {
                    break;
                }
                final MinecraftAccount minecraftAccount = accounts.get(RandomUtils.getRandom().nextInt(accounts.size()));
                GuiAltManager.login(minecraftAccount);
                ServerUtils.connectToLastServer();
                break;
            }
            case 4: {
                LoginUtils.loginCracked(RandomUtils.randomString(RandomUtils.nextInt(5, 16)));
                ServerUtils.connectToLastServer();
                break;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        ++this.reconnectTimer;
        if (this.reconnectTimer > 100) {
            ServerUtils.connectToLastServer();
        }
    }
    
    @Inject(method = { "drawScreen" }, at = { @At("RETURN") })
    private void drawScreen(final CallbackInfo callbackInfo) {
        this.reconnectButton.displayString = "Reconnect (" + (5 - this.reconnectTimer / 20) + ")";
    }
}
