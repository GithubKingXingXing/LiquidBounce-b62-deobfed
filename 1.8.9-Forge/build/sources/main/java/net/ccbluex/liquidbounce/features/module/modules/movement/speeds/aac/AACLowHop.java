
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACLowHop extends SpeedMode
{
    private boolean legitJump;
    
    public AACLowHop() {
        super("AACLowHop");
    }
    
    @Override
    public void onEnable() {
        this.legitJump = true;
        super.onEnable();
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (AACLowHop.mc.thePlayer.onGround) {
                if (this.legitJump) {
                    AACLowHop.mc.thePlayer.jump();
                    this.legitJump = false;
                    return;
                }
                AACLowHop.mc.thePlayer.motionY = 0.34299999475479126;
                MovementUtils.strafe(0.534f);
            }
        }
        else {
            this.legitJump = true;
            AACLowHop.mc.thePlayer.motionX = 0.0;
            AACLowHop.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
