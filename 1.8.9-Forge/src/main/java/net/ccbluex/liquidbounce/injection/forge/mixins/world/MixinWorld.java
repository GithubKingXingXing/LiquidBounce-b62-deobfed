
package net.ccbluex.liquidbounce.injection.forge.mixins.world;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ World.class })
public abstract class MixinWorld
{
    @Shadow
    public abstract IBlockState getBlockState(final BlockPos p0);
}
