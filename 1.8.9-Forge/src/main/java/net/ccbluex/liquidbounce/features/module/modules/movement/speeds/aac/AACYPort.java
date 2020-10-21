
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACYPort extends SpeedMode
{
    public AACYPort() {
        super("AACYPort");
    }
    
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving() && !AACYPort.mc.thePlayer.isSneaking()) {
            AACYPort.mc.thePlayer.cameraPitch = 0.0f;
            if (AACYPort.mc.thePlayer.onGround) {
                AACYPort.mc.thePlayer.motionY = 0.3425000011920929;
                final EntityPlayerSP thePlayer = AACYPort.mc.thePlayer;
                thePlayer.motionX *= 1.5893000364303589;
                final EntityPlayerSP thePlayer2 = AACYPort.mc.thePlayer;
                thePlayer2.motionZ *= 1.5893000364303589;
            }
            else {
                AACYPort.mc.thePlayer.motionY = -0.19;
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
