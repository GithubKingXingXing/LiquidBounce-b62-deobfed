
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FreeCam", description = "Allows you to move out of your body.", category = ModuleCategory.RENDER)
public class FreeCam extends Module
{
    private final FloatValue speedValue;
    private final BoolValue flyValue;
    private final BoolValue noClipValue;
    private EntityOtherPlayerMP fakePlayer;
    private double oldX;
    private double oldY;
    private double oldZ;
    
    public FreeCam() {
        this.speedValue = new FloatValue("Speed", 0.8f, 0.1f, 2.0f);
        this.flyValue = new BoolValue("Fly", true);
        this.noClipValue = new BoolValue("NoClip", true);
        this.fakePlayer = null;
    }
    
    @Override
    public void onEnable() {
        if (FreeCam.mc.thePlayer == null) {
            return;
        }
        this.oldX = FreeCam.mc.thePlayer.posX;
        this.oldY = FreeCam.mc.thePlayer.posY;
        this.oldZ = FreeCam.mc.thePlayer.posZ;
        (this.fakePlayer = new EntityOtherPlayerMP((World)FreeCam.mc.theWorld, FreeCam.mc.thePlayer.getGameProfile())).clonePlayer((EntityPlayer)FreeCam.mc.thePlayer, true);
        this.fakePlayer.rotationYawHead = FreeCam.mc.thePlayer.rotationYawHead;
        this.fakePlayer.copyLocationAndAnglesFrom((Entity)FreeCam.mc.thePlayer);
        FreeCam.mc.theWorld.addEntityToWorld(-1000, (Entity)this.fakePlayer);
        if (this.noClipValue.asBoolean()) {
            FreeCam.mc.thePlayer.noClip = true;
        }
    }
    
    @Override
    public void onDisable() {
        if (FreeCam.mc.thePlayer == null || this.fakePlayer == null) {
            return;
        }
        FreeCam.mc.thePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, FreeCam.mc.thePlayer.rotationYaw, FreeCam.mc.thePlayer.rotationPitch);
        FreeCam.mc.theWorld.removeEntityFromWorld(this.fakePlayer.getEntityId());
        this.fakePlayer = null;
        FreeCam.mc.thePlayer.motionX = 0.0;
        FreeCam.mc.thePlayer.motionY = 0.0;
        FreeCam.mc.thePlayer.motionZ = 0.0;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.noClipValue.asBoolean()) {
            FreeCam.mc.thePlayer.noClip = true;
        }
        FreeCam.mc.thePlayer.fallDistance = 0.0f;
        if (this.flyValue.asBoolean()) {
            final float value = this.speedValue.asFloat();
            FreeCam.mc.thePlayer.motionY = 0.0;
            FreeCam.mc.thePlayer.motionX = 0.0;
            FreeCam.mc.thePlayer.motionZ = 0.0;
            if (FreeCam.mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP thePlayer = FreeCam.mc.thePlayer;
                thePlayer.motionY += value;
            }
            if (FreeCam.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP thePlayer2 = FreeCam.mc.thePlayer;
                thePlayer2.motionY -= value;
            }
            MovementUtils.strafe(value);
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction) {
            event.setCancelled(true);
        }
    }
}
