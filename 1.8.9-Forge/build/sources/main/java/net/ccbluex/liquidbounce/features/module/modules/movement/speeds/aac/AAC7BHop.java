//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC7BHop extends SpeedMode
{
    public AAC7BHop() {
        super("AAC7BHop");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving() || AAC7BHop.mc.thePlayer.ridingEntity != null || AAC7BHop.mc.thePlayer.hurtTime > 0) {
            return;
        }
        if (AAC7BHop.mc.thePlayer.onGround) {
            AAC7BHop.mc.thePlayer.jump();
            AAC7BHop.mc.thePlayer.motionY = 0.405;
            final EntityPlayerSP thePlayer = AAC7BHop.mc.thePlayer;
            thePlayer.motionX *= 1.004;
            final EntityPlayerSP thePlayer2 = AAC7BHop.mc.thePlayer;
            thePlayer2.motionZ *= 1.004;
            return;
        }
        final double speed = MovementUtils.getSpeed() * 1.0072;
        final double yaw = Math.toRadians(AAC7BHop.mc.thePlayer.rotationYaw);
        AAC7BHop.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        AAC7BHop.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
