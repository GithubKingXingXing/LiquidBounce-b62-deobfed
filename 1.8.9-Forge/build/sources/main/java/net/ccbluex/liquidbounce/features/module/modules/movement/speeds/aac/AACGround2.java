
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACGround2 extends SpeedMode
{
    public AACGround2() {
        super("AACGround2");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        AACGround2.mc.timer.timerSpeed = ((Speed)ModuleManager.getModule(Speed.class)).aacGroundTimerValue.asFloat();
        MovementUtils.strafe(0.02f);
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        AACGround2.mc.timer.timerSpeed = 1.0f;
    }
}
