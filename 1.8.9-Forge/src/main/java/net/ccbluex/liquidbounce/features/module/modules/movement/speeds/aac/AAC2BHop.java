
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AAC2BHop extends SpeedMode
{
    public AAC2BHop() {
        super("AAC2BHop");
    }
    
    @Override
    public void onMotion() {
        if (AAC2BHop.mc.thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (AAC2BHop.mc.thePlayer.onGround) {
                AAC2BHop.mc.thePlayer.jump();
                final EntityPlayerSP thePlayer = AAC2BHop.mc.thePlayer;
                thePlayer.motionX *= 1.02;
                final EntityPlayerSP thePlayer2 = AAC2BHop.mc.thePlayer;
                thePlayer2.motionZ *= 1.02;
            }
            else if (AAC2BHop.mc.thePlayer.motionY > -0.2) {
                AAC2BHop.mc.thePlayer.jumpMovementFactor = 0.08f;
                final EntityPlayerSP thePlayer3 = AAC2BHop.mc.thePlayer;
                thePlayer3.motionY += 0.01431;
                AAC2BHop.mc.thePlayer.jumpMovementFactor = 0.07f;
            }
        }
        else {
            AAC2BHop.mc.thePlayer.motionX = 0.0;
            AAC2BHop.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
