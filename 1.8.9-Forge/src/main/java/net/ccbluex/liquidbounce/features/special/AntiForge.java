
package net.ccbluex.liquidbounce.features.special;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.event.EventListener;

@SideOnly(Side.CLIENT)
public class AntiForge implements EventListener
{
    public static boolean enabled;
    public static boolean blockFML;
    public static boolean blockProxyPacket;
    public static boolean blockPayloadPackets;
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (AntiForge.enabled && !Minecraft.getMinecraft().isIntegratedServerRunning()) {
            try {
                if (AntiForge.blockProxyPacket && packet.getClass().getName().equals("net.minecraftforge.fml.common.network.internal.FMLProxyPacket")) {
                    event.setCancelled(true);
                }
                if (AntiForge.blockPayloadPackets && packet instanceof C17PacketCustomPayload) {
                    final C17PacketCustomPayload customPayload = (C17PacketCustomPayload)packet;
                    if (!customPayload.getChannelName().startsWith("MC|")) {
                        event.setCancelled(true);
                    }
                    else if (customPayload.getChannelName().equalsIgnoreCase("MC|Brand")) {
                        customPayload.data = new PacketBuffer(Unpooled.buffer()).writeString("vanilla");
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public boolean handleEvents() {
        return true;
    }
    
    static {
        AntiForge.enabled = true;
        AntiForge.blockFML = true;
        AntiForge.blockProxyPacket = true;
        AntiForge.blockPayloadPackets = true;
    }
}
