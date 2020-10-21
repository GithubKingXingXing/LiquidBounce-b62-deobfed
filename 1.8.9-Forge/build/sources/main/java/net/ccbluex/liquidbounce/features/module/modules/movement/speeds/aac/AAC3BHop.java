
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC3BHop extends SpeedMode
{
    private boolean legitJump;
    
    public AAC3BHop() {
        super("AAC3BHop");
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
        AAC3BHop.mc.timer.timerSpeed = 1.0f;
        if (AAC3BHop.mc.thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (AAC3BHop.mc.thePlayer.onGround) {
                if (this.legitJump) {
                    AAC3BHop.mc.thePlayer.jump();
                    this.legitJump = false;
                    return;
                }
                AAC3BHop.mc.thePlayer.motionY = 0.3852;
                AAC3BHop.mc.thePlayer.onGround = false;
                MovementUtils.strafe(0.374f);
            }
            else if (AAC3BHop.mc.thePlayer.motionY < 0.0) {
                AAC3BHop.mc.thePlayer.speedInAir = 0.0201f;
                AAC3BHop.mc.timer.timerSpeed = 1.02f;
            }
            else {
                AAC3BHop.mc.timer.timerSpeed = 1.01f;
            }
        }
        else {
            this.legitJump = true;
            AAC3BHop.mc.thePlayer.motionX = 0.0;
            AAC3BHop.mc.thePlayer.motionZ = 0.0;
        }
    }
}
