
package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import net.ccbluex.liquidbounce.features.module.modules.exploit.GhostHand;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Overwrite;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ Block.class })
public abstract class MixinBlock
{
    @Shadow
    @Final
    protected BlockState blockState;
    
    @Shadow
    public abstract AxisAlignedBB getCollisionBoundingBox(final World p0, final BlockPos p1, final IBlockState p2);
    
    @Shadow
    public abstract void setBlockBounds(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5);
    
    @Overwrite
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);
        final BlockBBEvent blockBBEvent = new BlockBBEvent(pos, this.blockState.getBlock(), axisalignedbb);
        LiquidBounce.CLIENT.eventManager.callEvent(blockBBEvent);
        axisalignedbb = blockBBEvent.getBoundingBox();
        if (axisalignedbb != null && mask.intersectsWith(axisalignedbb)) {
            list.add(axisalignedbb);
        }
    }
    
    @Inject(method = { "shouldSideBeRendered" }, at = { @At("HEAD") }, cancellable = true)
    private void shouldSideBeRendered(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final XRay xray = (XRay)ModuleManager.getModule(XRay.class);
        if (xray.getState()) {
            callbackInfoReturnable.setReturnValue(xray.xrayBlocks.contains(this));
        }
    }
    
    @Inject(method = { "isCollidable" }, at = { @At("HEAD") }, cancellable = true)
    private void isCollidable(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final GhostHand ghostHand = (GhostHand)ModuleManager.getModule(GhostHand.class);
        if (ghostHand.getState() && ghostHand.blockValue.asInteger() != Block.getIdFromBlock((Block)this)) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
    
    @Inject(method = { "getAmbientOcclusionLightValue" }, at = { @At("HEAD") }, cancellable = true)
    private void getAmbientOcclusionLightValue(final CallbackInfoReturnable<Float> floatCallbackInfoReturnable) {
        if (ModuleManager.getModule(XRay.class).getState()) {
            floatCallbackInfoReturnable.setReturnValue(1.0f);
        }
    }
}
