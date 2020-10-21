
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class NCPHop extends SpeedMode
{
    public NCPHop() {
        super("NCPHop");
    }
    
    @Override
    public void onEnable() {
        NCPHop.mc.timer.timerSpeed = 1.0865f;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        NCPHop.mc.thePlayer.speedInAir = 0.02f;
        NCPHop.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (MovementUtils.isMoving()) {
            if (NCPHop.mc.thePlayer.onGround) {
                NCPHop.mc.thePlayer.jump();
                NCPHop.mc.thePlayer.speedInAir = 0.0223f;
            }
            MovementUtils.strafe();
        }
        else {
            NCPHop.mc.thePlayer.motionX = 0.0;
            NCPHop.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
