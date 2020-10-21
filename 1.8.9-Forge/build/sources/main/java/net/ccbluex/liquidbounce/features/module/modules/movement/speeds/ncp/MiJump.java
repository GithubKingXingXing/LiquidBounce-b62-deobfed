//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class MiJump extends SpeedMode
{
    public MiJump() {
        super("MiJump");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        if (MiJump.mc.thePlayer.onGround && !MiJump.mc.thePlayer.movementInput.jump) {
            final EntityPlayerSP thePlayer = MiJump.mc.thePlayer;
            thePlayer.motionY += 0.1;
            final double multiplier = 1.8;
            final EntityPlayerSP thePlayer2 = MiJump.mc.thePlayer;
            thePlayer2.motionX *= 1.8;
            final EntityPlayerSP thePlayer3 = MiJump.mc.thePlayer;
            thePlayer3.motionZ *= 1.8;
            final double currentSpeed = Math.sqrt(Math.pow(MiJump.mc.thePlayer.motionX, 2.0) + Math.pow(MiJump.mc.thePlayer.motionZ, 2.0));
            final double maxSpeed = 0.66;
            if (currentSpeed > 0.66) {
                MiJump.mc.thePlayer.motionX = MiJump.mc.thePlayer.motionX / currentSpeed * 0.66;
                MiJump.mc.thePlayer.motionZ = MiJump.mc.thePlayer.motionZ / currentSpeed * 0.66;
            }
        }
        MovementUtils.strafe();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
