
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class OldAACBHop extends SpeedMode
{
    public OldAACBHop() {
        super("OldAACBHop");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (OldAACBHop.mc.thePlayer.onGround) {
                MovementUtils.strafe(0.56f);
                OldAACBHop.mc.thePlayer.motionY = 0.41999998688697815;
            }
            else {
                MovementUtils.strafe(MovementUtils.getSpeed() * ((OldAACBHop.mc.thePlayer.fallDistance > 0.4f) ? 1.0f : 1.01f));
            }
        }
        else {
            OldAACBHop.mc.thePlayer.motionX = 0.0;
            OldAACBHop.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
