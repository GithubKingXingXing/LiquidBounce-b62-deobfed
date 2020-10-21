
package net.ccbluex.liquidbounce.utils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.TickEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ccbluex.liquidbounce.event.EventListener;

@SideOnly(Side.CLIENT)
public final class RotationUtils implements EventListener
{
    private static Minecraft mc;
    public static boolean lookChanged;
    public static float targetYaw;
    public static float targetPitch;
    private static int keepLength;
    public static boolean keepRotation;
    public static float[] lastLook;
    private static double x;
    private static double y;
    private static double z;
    
    public static void faceBlockPacket(final BlockPos blockPos) {
        if (blockPos == null) {
            return;
        }
        final double diffX = blockPos.getX() + 0.5 - RotationUtils.mc.thePlayer.posX;
        final double diffY = blockPos.getY() + 0.5 - (RotationUtils.mc.thePlayer.getEntityBoundingBox().minY + RotationUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - RotationUtils.mc.thePlayer.posZ;
        final double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, sqrt) * 180.0 / 3.141592653589793));
        setTargetRotation(RotationUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - RotationUtils.mc.thePlayer.rotationYaw), RotationUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - RotationUtils.mc.thePlayer.rotationPitch));
    }
    
    public static void faceBow(final Entity target, final boolean silent, final boolean predict, final float predictSize) {
        final EntityPlayerSP player = RotationUtils.mc.thePlayer;
        final double posX = target.posX + (predict ? ((target.posX - target.prevPosX) * predictSize) : 0.0) - (player.posX + (predict ? (player.posX - player.prevPosX) : 0.0));
        final double posY = target.getEntityBoundingBox().minY + (predict ? ((target.getEntityBoundingBox().minY - target.prevPosY) * predictSize) : 0.0) + target.getEyeHeight() - 0.15 - (player.getEntityBoundingBox().minY + (predict ? (player.posY - player.prevPosY) : 0.0)) - player.getEyeHeight();
        final double posZ = target.posZ + (predict ? ((target.posZ - target.prevPosZ) * predictSize) : 0.0) - (player.posZ + (predict ? (player.posZ - player.prevPosZ) : 0.0));
        final double sqrt = Math.sqrt(posX * posX + posZ * posZ);
        float velocity = player.getItemInUseCount() / 20.0f;
        velocity = (velocity * velocity + velocity * 2.0f) / 3.0f;
        if (velocity > 1.0f) {
            velocity = 1.0f;
        }
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(velocity * velocity * velocity * velocity - 0.006000000052154064 * (0.006000000052154064 * (sqrt * sqrt) + 2.0 * posY * (velocity * velocity)))) / (0.006000000052154064 * sqrt))));
        if (velocity < 0.1f) {
            final float[] rotations = getNeededRotations(getCenter(target.getEntityBoundingBox()), true);
            yaw = rotations[0];
            pitch = rotations[1];
        }
        if (silent) {
            setTargetRotation(yaw, pitch);
        }
        else {
            final float[] rotations = limitAngleChange(new float[] { player.rotationYaw, player.rotationPitch }, new float[] { yaw, pitch }, (float)(10 + RandomUtils.getRandom().nextInt(6)));
            if (rotations == null) {
                return;
            }
            player.rotationYaw = rotations[0];
            player.rotationPitch = rotations[1];
        }
    }
    
    public static float[] getTargetRotation(final Entity entity) {
        if (entity == null || RotationUtils.mc.thePlayer == null) {
            return null;
        }
        return getNeededRotations(getRandomCenter(entity.getEntityBoundingBox(), false), true);
    }
    
    public static float[] getNeededRotations(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = getEyesPos();
        if (predict) {
            eyesPos.addVector(RotationUtils.mc.thePlayer.motionX, RotationUtils.mc.thePlayer.motionY, RotationUtils.mc.thePlayer.motionZ);
        }
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }
    
    public static Vec3 getRandomCenter(final AxisAlignedBB bb, final boolean outborder) {
        if (outborder) {
            return new Vec3(bb.minX + (bb.maxX - bb.minX) * (RotationUtils.x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (RotationUtils.y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (RotationUtils.z * 0.3 + 1.0));
        }
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * RotationUtils.x * 0.8, bb.minY + (bb.maxY - bb.minY) * RotationUtils.y * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * RotationUtils.z * 0.8);
    }
    
    public static double getRotationDifference(final Entity entity) {
        final float[] rotations = getTargetRotation(entity);
        if (rotations == null) {
            return 0.0;
        }
        return getRotationDifference(rotations[0], rotations[1]);
    }
    
    public static double getRotationDifference(final float yaw, final float pitch) {
        return Math.sqrt(Math.pow(Math.abs(angleDifference(RotationUtils.lastLook[0] % 360.0f, yaw)), 2.0) + Math.pow(Math.abs(angleDifference(RotationUtils.lastLook[1], pitch)), 2.0));
    }
    
    public static float[] limitAngleChange(final float[] current, final float[] target, final float turnSpeed) {
        final double yawDifference = angleDifference(target[0], current[0]);
        final double pitchDifference = angleDifference(target[1], current[1]);
        final int n = 0;
        current[n] += (float)((yawDifference > turnSpeed) ? turnSpeed : ((yawDifference < -turnSpeed) ? (-turnSpeed) : yawDifference));
        final int n2 = 1;
        current[n2] += (float)((pitchDifference > turnSpeed) ? turnSpeed : ((pitchDifference < -turnSpeed) ? (-turnSpeed) : pitchDifference));
        return current;
    }
    
    private static double angleDifference(final double a, final double b) {
        return ((a - b) % 360.0 + 540.0) % 360.0 - 180.0;
    }
    
    public static Vec3 getEyesPos() {
        return new Vec3(RotationUtils.mc.thePlayer.posX, RotationUtils.mc.thePlayer.getEntityBoundingBox().minY + RotationUtils.mc.thePlayer.getEyeHeight(), RotationUtils.mc.thePlayer.posZ);
    }
    
    public static boolean isFaced(final Entity targetEntity, final double blockReachDistance) {
        return RaycastUtils.raycastEntities(blockReachDistance).contains(targetEntity);
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        if (RotationUtils.lookChanged) {
            ++RotationUtils.keepLength;
            if (RotationUtils.keepLength > 15) {
                reset();
            }
        }
        if (RandomUtils.getRandom().nextGaussian() * 100.0 > 80.0) {
            RotationUtils.x = Math.random();
        }
        if (RandomUtils.getRandom().nextGaussian() * 100.0 > 80.0) {
            RotationUtils.y = Math.random();
        }
        if (RandomUtils.getRandom().nextGaussian() * 100.0 > 80.0) {
            RotationUtils.z = Math.random();
        }
    }
    
    public static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (RotationUtils.lookChanged && !RotationUtils.keepRotation && (RotationUtils.targetYaw != RotationUtils.lastLook[0] || RotationUtils.targetPitch != RotationUtils.lastLook[1])) {
                packetPlayer.yaw = RotationUtils.targetYaw;
                packetPlayer.pitch = RotationUtils.targetPitch;
                packetPlayer.rotating = true;
            }
            if (packetPlayer.rotating) {
                RotationUtils.lastLook = new float[] { packetPlayer.yaw, packetPlayer.pitch };
            }
        }
    }
    
    public static void setTargetRotation(final float yaw, final float pitch) {
        if (Double.isNaN(yaw) || Double.isNaN(pitch)) {
            return;
        }
        RotationUtils.targetYaw = yaw;
        RotationUtils.targetPitch = pitch;
        RotationUtils.lookChanged = true;
        RotationUtils.keepLength = 0;
    }
    
    public static void reset() {
        RotationUtils.lookChanged = false;
        RotationUtils.keepLength = 0;
        RotationUtils.targetYaw = 0.0f;
        RotationUtils.targetPitch = 0.0f;
    }
    
    @Override
    public boolean handleEvents() {
        return true;
    }
    
    static {
        RotationUtils.mc = Minecraft.getMinecraft();
        RotationUtils.keepRotation = false;
        RotationUtils.lastLook = new float[] { 0.0f, 0.0f };
        RotationUtils.x = Math.random();
        RotationUtils.y = Math.random();
        RotationUtils.z = Math.random();
    }
}
