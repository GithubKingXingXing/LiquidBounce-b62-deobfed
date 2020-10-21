
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirJump;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityLivingBase.class })
public abstract class MixinEntityLivingBase extends MixinEntity
{
    @Shadow
    private int jumpTicks;
    @Shadow
    protected boolean isJumping;
    
    @Shadow
    protected abstract float getJumpUpwardsMotion();
    
    @Shadow
    public abstract PotionEffect getActivePotionEffect(final Potion p0);
    
    @Shadow
    public abstract boolean isPotionActive(final Potion p0);
    
    @Shadow
    public void onLivingUpdate() {
    }
    
    @Shadow
    protected abstract void updateFallState(final double p0, final boolean p1, final Block p2, final BlockPos p3);
    
    @Shadow
    public abstract float getHealth();
    
    @Shadow
    public abstract ItemStack getHeldItem();
    
    @Overwrite
    protected void jump() {
        final JumpEvent jumpEvent = new JumpEvent(this.getJumpUpwardsMotion());
        LiquidBounce.CLIENT.eventManager.callEvent(jumpEvent);
        if (jumpEvent.isCancelled()) {
            return;
        }
        this.motionY = jumpEvent.getMotion();
        if (this.isPotionActive(Potion.jump)) {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
        }
        if (this.isSprinting()) {
            final float f = this.rotationYaw * 0.017453292f;
            this.motionX -= MathHelper.sin(f) * 0.2f;
            this.motionZ += MathHelper.cos(f) * 0.2f;
        }
        this.isAirBorne = true;
    }
    
    @Inject(method = { "onLivingUpdate" }, at = { @At("HEAD") })
    private void headLiving(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(NoJumpDelay.class).getState()) {
            this.jumpTicks = 0;
        }
    }
    
    @Inject(method = { "onLivingUpdate" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;isJumping:Z", ordinal = 1) })
    private void airJumpInjection(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(AirJump.class).getState() && this.isJumping && this.jumpTicks == 0) {
            this.jump();
            this.jumpTicks = 10;
        }
    }
    
    @Inject(method = { "getLook" }, at = { @At("HEAD") }, cancellable = true)
    private void getLook(final CallbackInfoReturnable<Vec3> callbackInfoReturnable) {
        if (((EntityLivingBase)this) instanceof EntityPlayerSP) {
            callbackInfoReturnable.setReturnValue(this.getVectorForRotation(this.rotationPitch, this.rotationYaw));
        }
    }
    
    @Inject(method = { "isPotionActive(Lnet/minecraft/potion/Potion;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private void isPotionActive(final Potion p_isPotionActive_1_, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final AntiBlind antiBlind = (AntiBlind)ModuleManager.getModule(AntiBlind.class);
        if ((p_isPotionActive_1_ == Potion.confusion || p_isPotionActive_1_ == Potion.blindness) && antiBlind.getState() && antiBlind.getConfusionEffect().asBoolean()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
