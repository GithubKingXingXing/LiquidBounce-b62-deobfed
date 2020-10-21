
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class AACHop3313 extends SpeedMode
{
    public AACHop3313() {
        super("AACHop3.3.13");
    }
    
    @Override
    public void onMotion() {
        if (!MovementUtils.isMoving() || AACHop3313.mc.thePlayer.isInWater() || AACHop3313.mc.thePlayer.isInLava() || AACHop3313.mc.thePlayer.isOnLadder() || AACHop3313.mc.thePlayer.isRiding()) {
            return;
        }
        if (AACHop3313.mc.thePlayer.onGround) {
            AACHop3313.mc.thePlayer.jump();
            AACHop3313.mc.thePlayer.motionY = 0.41;
        }
        else if (!AACHop3313.mc.thePlayer.isCollidedHorizontally && AACHop3313.mc.thePlayer.hurtTime <= 6) {
            AACHop3313.mc.thePlayer.jumpMovementFactor = 0.027f;
            final float boostUp = (AACHop3313.mc.thePlayer.motionY <= 0.0) ? RandomUtils.nextFloat(1.002f, 1.0023f) : RandomUtils.nextFloat(1.0059f, 1.0061f);
            final EntityPlayerSP thePlayer = AACHop3313.mc.thePlayer;
            thePlayer.motionX *= boostUp;
            final EntityPlayerSP thePlayer2 = AACHop3313.mc.thePlayer;
            thePlayer2.motionZ *= boostUp;
            MovementUtils.forward(0.0019);
            final EntityPlayerSP thePlayer3 = AACHop3313.mc.thePlayer;
            thePlayer3.motionY -= 0.01489999983459711;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    @Override
    public void onDisable() {
        AACHop3313.mc.thePlayer.jumpMovementFactor = 0.02f;
    }
}
