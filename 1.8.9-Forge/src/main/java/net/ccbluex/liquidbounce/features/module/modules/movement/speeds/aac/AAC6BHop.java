
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC6BHop extends SpeedMode
{
    private boolean legitJump;
    
    public AAC6BHop() {
        super("AAC6BHop");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        AAC6BHop.mc.timer.timerSpeed = 1.0f;
        if (AAC6BHop.mc.thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (AAC6BHop.mc.thePlayer.onGround) {
                if (this.legitJump) {
                    AAC6BHop.mc.thePlayer.motionY = 0.4;
                    MovementUtils.strafe(0.15f);
                    AAC6BHop.mc.thePlayer.onGround = false;
                    this.legitJump = false;
                    return;
                }
                AAC6BHop.mc.thePlayer.motionY = 0.41;
                MovementUtils.strafe(0.47458485f);
            }
            if (AAC6BHop.mc.thePlayer.motionY < 0.0 && AAC6BHop.mc.thePlayer.motionY > -0.2) {
                AAC6BHop.mc.timer.timerSpeed = (float)(1.2 + AAC6BHop.mc.thePlayer.motionY);
            }
            AAC6BHop.mc.thePlayer.speedInAir = 0.022151f;
        }
        else {
            this.legitJump = true;
            AAC6BHop.mc.thePlayer.motionX = 0.0;
            AAC6BHop.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onEnable() {
        this.legitJump = true;
    }
    
    @Override
    public void onDisable() {
        AAC6BHop.mc.timer.timerSpeed = 1.0f;
        AAC6BHop.mc.thePlayer.speedInAir = 0.02f;
    }
}
