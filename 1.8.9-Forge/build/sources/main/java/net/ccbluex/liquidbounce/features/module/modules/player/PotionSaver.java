
package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "PotionSaver", description = "Freezes all potion effects while you are standing still.", category = ModuleCategory.PLAYER)
public class PotionSaver extends Module
{
    @EventTarget
    public void onPacket(final PacketEvent e) {
        final Packet packet = e.getPacket();
        if (packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) && !(packet instanceof C03PacketPlayer.C05PacketPlayerLook) && PotionSaver.mc.thePlayer != null && !PotionSaver.mc.thePlayer.isUsingItem()) {
            e.setCancelled(true);
        }
    }
}
