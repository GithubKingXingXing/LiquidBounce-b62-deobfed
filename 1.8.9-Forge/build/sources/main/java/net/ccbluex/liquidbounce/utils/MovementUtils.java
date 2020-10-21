//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.Minecraft;

public final class MovementUtils
{
    private static final Minecraft mc;
    
    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtils.mc.thePlayer.motionX * MovementUtils.mc.thePlayer.motionX + MovementUtils.mc.thePlayer.motionZ * MovementUtils.mc.thePlayer.motionZ);
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static boolean isMoving() {
        return MovementUtils.mc.thePlayer != null && (MovementUtils.mc.thePlayer.movementInput.moveForward != 0.0f || MovementUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean hasMotion() {
        return MovementUtils.mc.thePlayer.motionX != 0.0 && MovementUtils.mc.thePlayer.motionZ != 0.0 && MovementUtils.mc.thePlayer.motionY != 0.0;
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MovementUtils.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        MovementUtils.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(MovementUtils.mc.thePlayer.rotationYaw);
        MovementUtils.mc.thePlayer.setPosition(MovementUtils.mc.thePlayer.posX + -Math.sin(yaw) * length, MovementUtils.mc.thePlayer.posY, MovementUtils.mc.thePlayer.posZ + Math.cos(yaw) * length);
    }
    
    public static double getDirection() {
        float rotationYaw = MovementUtils.mc.thePlayer.rotationYaw;
        if (MovementUtils.mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MovementUtils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (MovementUtils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MovementUtils.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementUtils.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
