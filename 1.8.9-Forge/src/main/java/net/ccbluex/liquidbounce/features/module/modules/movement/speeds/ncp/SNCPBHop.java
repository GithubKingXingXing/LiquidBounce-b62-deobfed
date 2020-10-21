
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SNCPBHop extends SpeedMode
{
    private int level;
    private double moveSpeed;
    private double lastDist;
    private int timerDelay;
    
    public SNCPBHop() {
        super("SNCPBHop");
        this.level = 1;
        this.moveSpeed = 0.2873;
    }
    
    @Override
    public void onEnable() {
        SNCPBHop.mc.timer.timerSpeed = 1.0f;
        this.lastDist = 0.0;
        this.moveSpeed = 0.0;
        this.level = 4;
    }
    
    @Override
    public void onDisable() {
        SNCPBHop.mc.timer.timerSpeed = 1.0f;
        this.moveSpeed = this.getBaseMoveSpeed();
        this.level = 0;
    }
    
    @Override
    public void onMotion() {
        final double xDist = SNCPBHop.mc.thePlayer.posX - SNCPBHop.mc.thePlayer.prevPosX;
        final double zDist = SNCPBHop.mc.thePlayer.posZ - SNCPBHop.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
        ++this.timerDelay;
        this.timerDelay %= 5;
        if (this.timerDelay != 0) {
            SNCPBHop.mc.timer.timerSpeed = 1.0f;
        }
        else {
            if (MovementUtils.isMoving()) {
                SNCPBHop.mc.timer.timerSpeed = 32767.0f;
            }
            if (MovementUtils.isMoving()) {
                SNCPBHop.mc.timer.timerSpeed = 1.3f;
                final EntityPlayerSP thePlayer = SNCPBHop.mc.thePlayer;
                thePlayer.motionX *= 1.0199999809265137;
                final EntityPlayerSP thePlayer2 = SNCPBHop.mc.thePlayer;
                thePlayer2.motionZ *= 1.0199999809265137;
            }
        }
        if (SNCPBHop.mc.thePlayer.onGround && MovementUtils.isMoving()) {
            this.level = 2;
        }
        if (this.round(SNCPBHop.mc.thePlayer.posY - (int)SNCPBHop.mc.thePlayer.posY) == this.round(0.138)) {
            final EntityPlayerSP thePlayer3 = SNCPBHop.mc.thePlayer;
            thePlayer3.motionY -= 0.08;
            event.y -= 0.09316090325960147;
            final EntityPlayerSP thePlayer4 = SNCPBHop.mc.thePlayer;
            thePlayer4.posY -= 0.09316090325960147;
        }
        if (this.level == 1 && (SNCPBHop.mc.thePlayer.moveForward != 0.0f || SNCPBHop.mc.thePlayer.moveStrafing != 0.0f)) {
            this.level = 2;
            this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
        }
        else if (this.level == 2) {
            this.level = 3;
            SNCPBHop.mc.thePlayer.motionY = 0.399399995803833;
            event.y = 0.399399995803833;
            this.moveSpeed *= 2.149;
        }
        else if (this.level == 3) {
            this.level = 4;
            final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        }
        else if (this.level == 88) {
            this.moveSpeed = this.getBaseMoveSpeed();
            this.lastDist = 0.0;
            this.level = 89;
        }
        else {
            if (this.level == 89) {
                if (SNCPBHop.mc.theWorld.getCollidingBoundingBoxes((Entity)SNCPBHop.mc.thePlayer, SNCPBHop.mc.thePlayer.getEntityBoundingBox().offset(0.0, SNCPBHop.mc.thePlayer.motionY, 0.0)).size() > 0 || SNCPBHop.mc.thePlayer.isCollidedVertically) {
                    this.level = 1;
                }
                this.lastDist = 0.0;
                this.moveSpeed = this.getBaseMoveSpeed();
                return;
            }
            if (SNCPBHop.mc.theWorld.getCollidingBoundingBoxes((Entity)SNCPBHop.mc.thePlayer, SNCPBHop.mc.thePlayer.getEntityBoundingBox().offset(0.0, SNCPBHop.mc.thePlayer.motionY, 0.0)).size() > 0 || SNCPBHop.mc.thePlayer.isCollidedVertically) {
                this.moveSpeed = this.getBaseMoveSpeed();
                this.lastDist = 0.0;
                this.level = 88;
                return;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        final MovementInput movementInput = SNCPBHop.mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = SNCPBHop.mc.thePlayer.rotationYaw;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double mx2 = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz2 = Math.sin(Math.toRadians(yaw + 90.0f));
        event.x = forward * this.moveSpeed * mx2 + strafe * this.moveSpeed * mz2;
        event.z = forward * this.moveSpeed * mz2 - strafe * this.moveSpeed * mx2;
        SNCPBHop.mc.thePlayer.stepHeight = 0.6f;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (SNCPBHop.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (SNCPBHop.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    private double round(final double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
