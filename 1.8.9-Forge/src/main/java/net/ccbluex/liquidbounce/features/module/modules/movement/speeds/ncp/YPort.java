
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.potion.Potion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class YPort extends SpeedMode
{
    private double moveSpeed;
    private int level;
    private double lastDist;
    private int timerDelay;
    private boolean safeJump;
    
    public YPort() {
        super("YPort");
        this.moveSpeed = 0.2873;
        this.level = 1;
    }
    
    @Override
    public void onMotion() {
        if (!this.safeJump && !YPort.mc.gameSettings.keyBindJump.isKeyDown() && !YPort.mc.thePlayer.isOnLadder() && !YPort.mc.thePlayer.isInsideOfMaterial(Material.water) && !YPort.mc.thePlayer.isInsideOfMaterial(Material.lava) && !YPort.mc.thePlayer.isInWater() && ((!(this.getBlock(-1.1) instanceof BlockAir) && !(this.getBlock(-1.1) instanceof BlockAir)) || (!(this.getBlock(-0.1) instanceof BlockAir) && YPort.mc.thePlayer.motionX != 0.0 && YPort.mc.thePlayer.motionZ != 0.0 && !YPort.mc.thePlayer.onGround && YPort.mc.thePlayer.fallDistance < 3.0f && YPort.mc.thePlayer.fallDistance > 0.05)) && this.level == 3) {
            YPort.mc.thePlayer.motionY = -0.3994;
        }
        final double xDist = YPort.mc.thePlayer.posX - YPort.mc.thePlayer.prevPosX;
        final double zDist = YPort.mc.thePlayer.posZ - YPort.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        if (!MovementUtils.isMoving()) {
            this.safeJump = true;
        }
        else if (YPort.mc.thePlayer.onGround) {
            this.safeJump = false;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
        ++this.timerDelay;
        this.timerDelay %= 5;
        if (this.timerDelay != 0) {
            YPort.mc.timer.timerSpeed = 1.0f;
        }
        else {
            if (MovementUtils.hasMotion()) {
                YPort.mc.timer.timerSpeed = 32767.0f;
            }
            if (MovementUtils.hasMotion()) {
                YPort.mc.timer.timerSpeed = 1.3f;
                final EntityPlayerSP thePlayer = YPort.mc.thePlayer;
                thePlayer.motionX *= 1.0199999809265137;
                final EntityPlayerSP thePlayer2 = YPort.mc.thePlayer;
                thePlayer2.motionZ *= 1.0199999809265137;
            }
        }
        if (YPort.mc.thePlayer.onGround && MovementUtils.hasMotion()) {
            this.level = 2;
        }
        if (this.round(YPort.mc.thePlayer.posY - (int)YPort.mc.thePlayer.posY) == this.round(0.138)) {
            final EntityPlayerSP thePlayer3 = YPort.mc.thePlayer;
            thePlayer3.motionY -= 0.08;
            event.y -= 0.09316090325960147;
            final EntityPlayerSP thePlayer4 = YPort.mc.thePlayer;
            thePlayer4.posY -= 0.09316090325960147;
        }
        if (this.level == 1 && (YPort.mc.thePlayer.moveForward != 0.0f || YPort.mc.thePlayer.moveStrafing != 0.0f)) {
            this.level = 2;
            this.moveSpeed = 1.38 * this.getBaseMoveSpeed() - 0.01;
        }
        else if (this.level == 2) {
            this.level = 3;
            YPort.mc.thePlayer.motionY = 0.399399995803833;
            event.y = 0.399399995803833;
            this.moveSpeed *= 2.149;
        }
        else if (this.level == 3) {
            this.level = 4;
            final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        }
        else {
            if (YPort.mc.theWorld.getCollidingBoundingBoxes((Entity)YPort.mc.thePlayer, YPort.mc.thePlayer.getEntityBoundingBox().offset(0.0, YPort.mc.thePlayer.motionY, 0.0)).size() > 0 || YPort.mc.thePlayer.isCollidedVertically) {
                this.level = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        float forward = YPort.mc.thePlayer.movementInput.moveForward;
        float strafe = YPort.mc.thePlayer.movementInput.moveStrafe;
        float yaw = YPort.mc.thePlayer.rotationYaw;
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
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
        event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
        YPort.mc.thePlayer.stepHeight = 0.6f;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (YPort.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = YPort.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    private Block getBlock(final AxisAlignedBB axisAlignedBB) {
        for (int x = MathHelper.floor_double(axisAlignedBB.minX); x < MathHelper.floor_double(axisAlignedBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(axisAlignedBB.minZ); z < MathHelper.floor_double(axisAlignedBB.maxZ) + 1; ++z) {
                final Block block = YPort.mc.theWorld.getBlockState(new BlockPos(x, (int)axisAlignedBB.minY, z)).getBlock();
                if (block != null) {
                    return block;
                }
            }
        }
        return null;
    }
    
    private Block getBlock(final double offset) {
        return this.getBlock(YPort.mc.thePlayer.getEntityBoundingBox().offset(0.0, offset, 0.0));
    }
    
    private double round(final double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
