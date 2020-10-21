
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class NCPFHop extends SpeedMode
{
    public NCPFHop() {
        super("NCPFHop");
    }
    
    @Override
    public void onEnable() {
        NCPFHop.mc.timer.timerSpeed = 1.0866f;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        NCPFHop.mc.thePlayer.speedInAir = 0.02f;
        NCPFHop.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
        if (MovementUtils.isMoving()) {
            if (NCPFHop.mc.thePlayer.onGround) {
                NCPFHop.mc.thePlayer.jump();
                final EntityPlayerSP thePlayer = NCPFHop.mc.thePlayer;
                thePlayer.motionX *= 1.01;
                final EntityPlayerSP thePlayer2 = NCPFHop.mc.thePlayer;
                thePlayer2.motionZ *= 1.01;
                NCPFHop.mc.thePlayer.speedInAir = 0.0223f;
            }
            final EntityPlayerSP thePlayer3 = NCPFHop.mc.thePlayer;
            thePlayer3.motionY -= 9.9999E-4;
            MovementUtils.strafe();
        }
        else {
            NCPFHop.mc.thePlayer.motionX = 0.0;
            NCPFHop.mc.thePlayer.motionZ = 0.0;
        }
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
