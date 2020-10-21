
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.Minecraft;
import java.net.UnknownHostException;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import java.net.InetAddress;
import net.minecraft.network.Packet;
import net.minecraft.network.login.client.C00PacketLoginStart;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.mcleaks.MCLeaks;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.atomic.AtomicInteger;
import org.spongepowered.asm.mixin.Final;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
@Mixin({ GuiConnecting.class })
public abstract class MixinGuiConnecting extends GuiScreen
{
    @Shadow
    private NetworkManager networkManager;
    @Shadow
    @Final
    private static Logger logger;
    @Shadow
    private boolean cancel;
    @Shadow
    @Final
    private GuiScreen previousGuiScreen;
    @Shadow
    @Final
    private static AtomicInteger CONNECTION_ID;
    
    @Inject(method = { "connect" }, at = { @At("HEAD") })
    private void headConnect(final String ip, final int port, final CallbackInfo callbackInfo) {
        ServerUtils.serverData = new ServerData("", ip + ":" + port, false);
    }
    
    @Inject(method = { "connect" }, at = { @At(value = "NEW", target = "net/minecraft/network/login/client/C00PacketLoginStart") }, cancellable = true)
    private void mcLeaks(final CallbackInfo callbackInfo) {
        if (MCLeaks.isAltActive()) {
            this.networkManager.sendPacket((Packet)new C00PacketLoginStart(new GameProfile((UUID)null, MCLeaks.getSession().getUsername())));
            callbackInfo.cancel();
        }
    }
    
    @Overwrite
    private void connect(final String ip, final int port) {
        MixinGuiConnecting.logger.info("Connecting to " + ip + ", " + port);
        InetAddress inetaddress;
        NetworkManager networkManager;
        GameProfile getProfile = null;
        final Packet packet;
        Minecraft mc;
        GuiScreen previousGuiScreen;
        final ChatComponentTranslation chatComponentTranslation;
        final GuiScreen guiScreen;
        final String s3;
        String s;
        String s2;
        Minecraft mc2;
        GuiScreen previousGuiScreen2;
        final ChatComponentTranslation chatComponentTranslation2;
        final GuiScreen guiScreen2;
        final String s4;
        new Thread(() -> {
            inetaddress = null;
            try {
                if (!this.cancel) {
                    inetaddress = InetAddress.getByName(ip);
                    (this.networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, this.mc.gameSettings.isUsingNativeTransport())).setNetHandler((INetHandler)new NetHandlerLoginClient(this.networkManager, this.mc, this.previousGuiScreen));
                    this.networkManager.sendPacket((Packet)new C00Handshake(47, ip, port, EnumConnectionState.LOGIN, true));
                    networkManager = this.networkManager;
                    // new(net.minecraft.network.login.client.C00PacketLoginStart.class)
                    if (MCLeaks.isAltActive()) {
                        // new(com.mojang.authlib.GameProfile.class)
                        new GameProfile((UUID)null, MCLeaks.getSession().getUsername());
                    }
                    else {
                        getProfile = this.mc.getSession().getProfile();
                    }
                    new C00PacketLoginStart(getProfile);
                    networkManager.sendPacket(packet);
                }
            }
            catch (UnknownHostException unknownhostexception) {
                if (!this.cancel) {
                    MixinGuiConnecting.logger.error("Couldn't connect to server", (Throwable)unknownhostexception);
                    mc = this.mc;
                    // new(net.minecraft.client.gui.GuiDisconnected.class)
                    previousGuiScreen = this.previousGuiScreen;
                    new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host" });
                    new GuiDisconnected(previousGuiScreen, s3, (IChatComponent)chatComponentTranslation);
                    mc.displayGuiScreen(guiScreen);
                }
            }
            catch (Exception exception) {
                if (!this.cancel) {
                    MixinGuiConnecting.logger.error("Couldn't connect to server", (Throwable)exception);
                    s = exception.toString();
                    if (inetaddress != null) {
                        s2 = inetaddress.toString() + ":" + port;
                        s = s.replaceAll(s2, "");
                    }
                    mc2 = this.mc;
                    // new(net.minecraft.client.gui.GuiDisconnected.class)
                    previousGuiScreen2 = this.previousGuiScreen;
                    new ChatComponentTranslation("disconnect.genericReason", new Object[] { s });
                    new GuiDisconnected(previousGuiScreen2, s4, (IChatComponent)chatComponentTranslation2);
                    mc2.displayGuiScreen(guiScreen2);
                }
            }
        }, "Server Connector #" + MixinGuiConnecting.CONNECTION_ID.incrementAndGet()).start();
    }
    
    @Overwrite
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.drawDefaultBackground();
        RenderUtils.drawLoadingCircle((float)(scaledResolution.getScaledWidth() / 2), (float)(scaledResolution.getScaledHeight() / 4 + 70));
        String ip = "Unknown";
        final ServerData serverData = this.mc.getCurrentServerData();
        if (serverData != null) {
            ip = serverData.serverIP;
        }
        Fonts.font40.drawCenteredString("Connecting to", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 4 + 110, 16777215, true);
        Fonts.font35.drawCenteredString(ip, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 4 + 120, 5407227, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
