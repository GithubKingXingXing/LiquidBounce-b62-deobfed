//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC5BHop extends SpeedMode
{
    private boolean legitJump;
    
    public AAC5BHop() {
        super("AAC5BHop");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onTick() {
        AAC5BHop.mc.timer.timerSpeed = 1.0f;
        if (AAC5BHop.mc.thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (AAC5BHop.mc.thePlayer.onGround) {
                if (this.legitJump) {
                    AAC5BHop.mc.thePlayer.jump();
                    this.legitJump = false;
                    return;
                }
                AAC5BHop.mc.thePlayer.motionY = 0.41;
                AAC5BHop.mc.thePlayer.onGround = false;
                MovementUtils.strafe(0.374f);
            }
            else if (AAC5BHop.mc.thePlayer.motionY < 0.0) {
                AAC5BHop.mc.thePlayer.speedInAir = 0.0201f;
                AAC5BHop.mc.timer.timerSpeed = 1.02f;
            }
            else {
                AAC5BHop.mc.timer.timerSpeed = 1.01f;
            }
        }
        else {
            this.legitJump = true;
            AAC5BHop.mc.thePlayer.motionX = 0.0;
            AAC5BHop.mc.thePlayer.motionZ = 0.0;
        }
    }
}
