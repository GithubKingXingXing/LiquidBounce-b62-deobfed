//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class NCPBHop extends SpeedMode
{
    private int level;
    private double moveSpeed;
    private double lastDist;
    private int timerDelay;
    
    public NCPBHop() {
        super("NCPBHop");
        this.level = 1;
        this.moveSpeed = 0.2873;
    }
    
    @Override
    public void onEnable() {
        NCPBHop.mc.timer.timerSpeed = 1.0f;
        this.level = ((NCPBHop.mc.theWorld.getCollidingBoundingBoxes((Entity)NCPBHop.mc.thePlayer, NCPBHop.mc.thePlayer.getEntityBoundingBox().offset(0.0, NCPBHop.mc.thePlayer.motionY, 0.0)).size() > 0 || NCPBHop.mc.thePlayer.isCollidedVertically) ? 1 : 4);
    }
    
    @Override
    public void onDisable() {
        NCPBHop.mc.timer.timerSpeed = 1.0f;
        this.moveSpeed = this.getBaseMoveSpeed();
        this.level = 0;
    }
    
    @Override
    public void onMotion() {
        final double xDist = NCPBHop.mc.thePlayer.posX - NCPBHop.mc.thePlayer.prevPosX;
        final double zDist = NCPBHop.mc.thePlayer.posZ - NCPBHop.mc.thePlayer.prevPosZ;
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
            NCPBHop.mc.timer.timerSpeed = 1.0f;
        }
        else {
            if (MovementUtils.isMoving()) {
                NCPBHop.mc.timer.timerSpeed = 32767.0f;
            }
            if (MovementUtils.isMoving()) {
                NCPBHop.mc.timer.timerSpeed = 1.3f;
                final EntityPlayerSP thePlayer = NCPBHop.mc.thePlayer;
                thePlayer.motionX *= 1.0199999809265137;
                final EntityPlayerSP thePlayer2 = NCPBHop.mc.thePlayer;
                thePlayer2.motionZ *= 1.0199999809265137;
            }
        }
        if (NCPBHop.mc.thePlayer.onGround && MovementUtils.isMoving()) {
            this.level = 2;
        }
        if (this.round(NCPBHop.mc.thePlayer.posY - (int)NCPBHop.mc.thePlayer.posY) == this.round(0.138)) {
            final EntityPlayerSP thePlayer3;
            final EntityPlayerSP thePlayer = thePlayer3 = NCPBHop.mc.thePlayer;
            thePlayer3.motionY -= 0.08;
            event.y -= 0.09316090325960147;
            final EntityPlayerSP entityPlayerSP = thePlayer;
            entityPlayerSP.posY -= 0.09316090325960147;
        }
        if (this.level == 1 && (NCPBHop.mc.thePlayer.moveForward != 0.0f || NCPBHop.mc.thePlayer.moveStrafing != 0.0f)) {
            this.level = 2;
            this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
        }
        else if (this.level == 2) {
            this.level = 3;
            NCPBHop.mc.thePlayer.motionY = 0.399399995803833;
            event.y = 0.399399995803833;
            this.moveSpeed *= 2.149;
        }
        else if (this.level == 3) {
            this.level = 4;
            final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        }
        else {
            if (NCPBHop.mc.theWorld.getCollidingBoundingBoxes((Entity)NCPBHop.mc.thePlayer, NCPBHop.mc.thePlayer.getEntityBoundingBox().offset(0.0, NCPBHop.mc.thePlayer.motionY, 0.0)).size() > 0 || NCPBHop.mc.thePlayer.isCollidedVertically) {
                this.level = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        final MovementInput movementInput = NCPBHop.mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = NCPBHop.mc.thePlayer.rotationYaw;
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
        NCPBHop.mc.thePlayer.stepHeight = 0.6f;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (NCPBHop.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (NCPBHop.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    private double round(final double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
