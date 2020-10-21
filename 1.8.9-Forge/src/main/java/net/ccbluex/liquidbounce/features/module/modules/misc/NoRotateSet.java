//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoRotateSet", description = "Prevents the server from rotating your head.", category = ModuleCategory.MISC)
public class NoRotateSet extends Module
{
    private final BoolValue confirmValue;
    private final BoolValue illegalRotationValue;
    private final BoolValue noZeroValue;
    
    public NoRotateSet() {
        this.confirmValue = new BoolValue("Confirm", true);
        this.illegalRotationValue = new BoolValue("ConfirmIllegalRotation", false);
        this.noZeroValue = new BoolValue("NoZero", false);
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (NoRotateSet.mc.thePlayer == null) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook sPacketPlayerPosLook = (S08PacketPlayerPosLook)packet;
            if (this.noZeroValue.asBoolean() && sPacketPlayerPosLook.getYaw() == 0.0f && sPacketPlayerPosLook.getPitch() == 0.0f) {
                return;
            }
            if ((this.illegalRotationValue.asBoolean() || (sPacketPlayerPosLook.getPitch() <= 90.0f && sPacketPlayerPosLook.getPitch() >= -90.0f && RotationUtils.lastLook != null && sPacketPlayerPosLook.getYaw() != RotationUtils.lastLook[0] && sPacketPlayerPosLook.getPitch() != RotationUtils.lastLook[1])) && this.confirmValue.asBoolean()) {
                NoRotateSet.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(sPacketPlayerPosLook.getYaw(), sPacketPlayerPosLook.getPitch(), NoRotateSet.mc.thePlayer.onGround));
            }
            sPacketPlayerPosLook.yaw = NoRotateSet.mc.thePlayer.rotationYaw;
            sPacketPlayerPosLook.pitch = NoRotateSet.mc.thePlayer.rotationPitch;
        }
    }
}
