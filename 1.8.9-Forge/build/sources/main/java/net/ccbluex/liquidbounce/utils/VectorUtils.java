//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.util.Vec3;

public final class VectorUtils
{
    private final Vec3 start;
    private final Vec3 end;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    
    public VectorUtils(final Vec3 vector, final float yaw, final float pitch, final double length) {
        this.start = vector;
        this.end = new Vec3(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * length + vector.xCoord, Math.sin(Math.toRadians(pitch)) * length + vector.yCoord, Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * length + vector.zCoord);
        this.offsetX = this.end.xCoord - this.start.xCoord;
        this.offsetY = this.end.yCoord - this.start.yCoord;
        this.offsetZ = this.end.zCoord - this.start.zCoord;
    }
    
    public double getOffsetX() {
        return this.offsetX;
    }
    
    public double getOffsetY() {
        return this.offsetY;
    }
    
    public double getOffsetZ() {
        return this.offsetZ;
    }
    
    public Vec3 getStartVector() {
        return this.start;
    }
    
    public Vec3 getEndVector() {
        return this.end;
    }
}
