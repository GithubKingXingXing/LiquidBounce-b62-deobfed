
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SpectreBHop extends SpeedMode
{
    public SpectreBHop() {
        super("SpectreBHop");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving() || SpectreBHop.mc.thePlayer.movementInput.jump) {
            return;
        }
        if (SpectreBHop.mc.thePlayer.onGround) {
            MovementUtils.strafe(1.1f);
            SpectreBHop.mc.thePlayer.motionY = 0.44;
            return;
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
