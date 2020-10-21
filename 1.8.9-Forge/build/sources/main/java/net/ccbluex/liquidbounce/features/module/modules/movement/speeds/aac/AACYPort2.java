
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACYPort2 extends SpeedMode
{
    public AACYPort2() {
        super("AACYPort2");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            AACYPort2.mc.thePlayer.cameraPitch = 0.0f;
            if (AACYPort2.mc.thePlayer.onGround) {
                AACYPort2.mc.thePlayer.jump();
                AACYPort2.mc.thePlayer.motionY = 0.38510000705718994;
                final EntityPlayerSP thePlayer = AACYPort2.mc.thePlayer;
                thePlayer.motionX *= 1.01;
                final EntityPlayerSP thePlayer2 = AACYPort2.mc.thePlayer;
                thePlayer2.motionZ *= 1.01;
            }
            else {
                AACYPort2.mc.thePlayer.motionY = -0.21;
            }
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
