//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.BlockLadder;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ BlockLadder.class })
public abstract class MixinBlockLadder extends MixinBlock
{
    @Shadow
    @Final
    public static PropertyDirection FACING;
    
    @Overwrite
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() instanceof BlockLadder) {
            final FastClimb fastClimb = (FastClimb)ModuleManager.getModule(FastClimb.class);
            final float f = (fastClimb.getState() && fastClimb.modeValue.asString().equalsIgnoreCase("AAC")) ? 0.99f : 0.125f;
            switch ((EnumFacing)iblockstate.getValue((IProperty)MixinBlockLadder.FACING)) {
                case NORTH: {
                    this.setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                default: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
                    break;
                }
            }
        }
    }
}
