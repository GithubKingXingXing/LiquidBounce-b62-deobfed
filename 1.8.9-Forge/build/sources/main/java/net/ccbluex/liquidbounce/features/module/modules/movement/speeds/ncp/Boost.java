
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class Boost extends SpeedMode
{
    private int motionDelay;
    private float ground;
    
    public Boost() {
        super("Boost");
    }
    
    @Override
    public void onMotion() {
        double speed = 3.1981;
        double offset = 4.69;
        boolean shouldOffset = true;
        for (final Object o : Boost.mc.theWorld.getCollidingBoundingBoxes((Entity)Boost.mc.thePlayer, Boost.mc.thePlayer.getEntityBoundingBox().offset(Boost.mc.thePlayer.motionX / offset, 0.0, Boost.mc.thePlayer.motionZ / offset))) {
            if (o instanceof AxisAlignedBB) {
                shouldOffset = false;
                break;
            }
        }
        if (Boost.mc.thePlayer.onGround && this.ground < 1.0f) {
            this.ground += 0.2f;
        }
        if (!Boost.mc.thePlayer.onGround) {
            this.ground = 0.0f;
        }
        if (this.ground == 1.0f && this.shouldSpeedUp()) {
            if (!Boost.mc.thePlayer.isSprinting()) {
                offset += 0.8;
            }
            if (Boost.mc.thePlayer.moveStrafing != 0.0f) {
                speed -= 0.1;
                offset += 0.5;
            }
            if (Boost.mc.thePlayer.isInWater()) {
                speed -= 0.1;
            }
            switch (++this.motionDelay) {
                case 1: {
                    final EntityPlayerSP thePlayer = Boost.mc.thePlayer;
                    thePlayer.motionX *= speed;
                    final EntityPlayerSP thePlayer2 = Boost.mc.thePlayer;
                    thePlayer2.motionZ *= speed;
                    break;
                }
                case 2: {
                    final EntityPlayerSP thePlayer3 = Boost.mc.thePlayer;
                    thePlayer3.motionX /= 1.458;
                    final EntityPlayerSP thePlayer4 = Boost.mc.thePlayer;
                    thePlayer4.motionZ /= 1.458;
                    break;
                }
                case 4: {
                    if (shouldOffset) {
                        Boost.mc.thePlayer.setPosition(Boost.mc.thePlayer.posX + Boost.mc.thePlayer.motionX / offset, Boost.mc.thePlayer.posY, Boost.mc.thePlayer.posZ + Boost.mc.thePlayer.motionZ / offset);
                    }
                    this.motionDelay = 0;
                    break;
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
    
    private boolean shouldSpeedUp() {
        return !Boost.mc.thePlayer.isInWater() && !Boost.mc.thePlayer.isOnLadder() && !Boost.mc.thePlayer.isSneaking() && MovementUtils.isMoving();
    }
}
