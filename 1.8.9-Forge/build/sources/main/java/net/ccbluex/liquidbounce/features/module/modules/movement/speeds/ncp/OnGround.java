
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class OnGround extends SpeedMode
{
    public OnGround() {
        super("OnGround");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        if (OnGround.mc.thePlayer.fallDistance > 3.994) {
            return;
        }
        if (OnGround.mc.thePlayer.isInWater() || OnGround.mc.thePlayer.isOnLadder() || OnGround.mc.thePlayer.isCollidedHorizontally) {
            return;
        }
        final EntityPlayerSP thePlayer = OnGround.mc.thePlayer;
        thePlayer.posY -= 0.3993000090122223;
        OnGround.mc.thePlayer.motionY = -1000.0;
        OnGround.mc.thePlayer.cameraPitch = 0.3f;
        OnGround.mc.thePlayer.distanceWalkedModified = 44.0f;
        OnGround.mc.timer.timerSpeed = 1.0f;
        if (OnGround.mc.thePlayer.onGround) {
            final EntityPlayerSP thePlayer2 = OnGround.mc.thePlayer;
            thePlayer2.posY += 0.3993000090122223;
            OnGround.mc.thePlayer.motionY = 0.3993000090122223;
            OnGround.mc.thePlayer.distanceWalkedOnStepModified = 44.0f;
            final EntityPlayerSP thePlayer3 = OnGround.mc.thePlayer;
            thePlayer3.motionX *= 1.590000033378601;
            final EntityPlayerSP thePlayer4 = OnGround.mc.thePlayer;
            thePlayer4.motionZ *= 1.590000033378601;
            OnGround.mc.thePlayer.cameraPitch = 0.0f;
            OnGround.mc.timer.timerSpeed = 1.199f;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
