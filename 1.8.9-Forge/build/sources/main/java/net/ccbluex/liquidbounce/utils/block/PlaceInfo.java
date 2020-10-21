//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.block;

import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;

public final class PlaceInfo
{
    private final BlockPos blockPos;
    private final EnumFacing enumFacing;
    private final Vec3 vec3;
    
    private PlaceInfo(final BlockPos blockPos, final EnumFacing enumFacing) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = new Vec3((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
    }
    
    private PlaceInfo(final BlockPos blockPos, final EnumFacing enumFacing, final Vec3 vec3) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = vec3;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }
    
    public Vec3 getVec3() {
        return this.vec3;
    }
    
    public static PlaceInfo get(final BlockPos blockPos) {
        if (BlockUtils.canBeClicked(blockPos.add(0, -1, 0))) {
            return new PlaceInfo(blockPos.add(0, -1, 0), EnumFacing.UP);
        }
        if (BlockUtils.canBeClicked(blockPos.add(-1, 0, 0))) {
            return new PlaceInfo(blockPos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (BlockUtils.canBeClicked(blockPos.add(1, 0, 0))) {
            return new PlaceInfo(blockPos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (BlockUtils.canBeClicked(blockPos.add(0, 0, -1))) {
            return new PlaceInfo(blockPos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (BlockUtils.canBeClicked(blockPos.add(0, 0, 1))) {
            return new PlaceInfo(blockPos.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }
}
