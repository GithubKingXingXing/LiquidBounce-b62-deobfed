
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketThreadUtil;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.minecraft.network.play.server.S01PacketJoinGame;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import java.net.URISyntaxException;
import java.net.URI;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetHandlerPlayClient.class })
public abstract class MixinNetHandlerPlayClient
{
    @Shadow
    @Final
    private NetworkManager netManager;
    @Shadow
    private Minecraft gameController;
    @Shadow
    private WorldClient clientWorldController;
    @Shadow
    public int currentServerMaxPlayers;
    
    @Inject(method = { "handleResourcePack" }, at = { @At("HEAD") }, cancellable = true)
    private void handleResourcePack(final S48PacketResourcePackSend p_handleResourcePack_1_, final CallbackInfo callbackInfo) {
        final String url = p_handleResourcePack_1_.getURL();
        final String hash = p_handleResourcePack_1_.getHash();
        try {
            final String scheme = new URI(url).getScheme();
            final boolean isLevelProtocol = "level".equals(scheme);
            if (!"http".equals(scheme) && !"https".equals(scheme) && !isLevelProtocol) {
                throw new URISyntaxException(url, "Wrong protocol");
            }
            if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
                throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
            }
        }
        catch (URISyntaxException e) {
            ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)e);
            this.netManager.sendPacket((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "handleJoinGame" }, at = { @At("HEAD") }, cancellable = true)
    private void handleJoinGameWithAntiForge(final S01PacketJoinGame packetIn, final CallbackInfo callbackInfo) {
        if (!AntiForge.enabled || !AntiForge.blockFML) {
            return;
        }
        PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
        this.gameController.playerController = new PlayerControllerMP(this.gameController, (NetHandlerPlayClient)this);
        this.clientWorldController = new WorldClient((NetHandlerPlayClient)this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
        this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
        this.gameController.loadWorld(this.clientWorldController);
        this.gameController.thePlayer.dimension = packetIn.getDimension();
        this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain((NetHandlerPlayClient)this));
        this.gameController.thePlayer.setEntityId(packetIn.getEntityId());
        this.currentServerMaxPlayers = packetIn.getMaxPlayers();
        this.gameController.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
        this.gameController.playerController.setGameType(packetIn.getGameType());
        this.gameController.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket((Packet)new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
        callbackInfo.cancel();
    }
}
