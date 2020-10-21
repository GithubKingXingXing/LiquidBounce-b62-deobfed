//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraft.world.World;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ EntityRenderer.class })
public abstract class MixinEntityRenderer
{
    @Shadow
    private Entity pointedEntity;
    @Shadow
    private Minecraft mc;
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private float thirdPersonDistance;
    @Shadow
    private boolean cloudFog;
    
    @Shadow
    public abstract void loadShader(final ResourceLocation p0);
    
    @Shadow
    protected abstract void setupCameraTransform(final float p0, final int p1);
    
    @Inject(method = { "renderWorldPass" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE) })
    private void renderWorldPass(final int pass, final float partialTicks, final long finishTimeNano, final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new Render3DEvent(partialTicks));
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    private void injectHurtCameraEffect(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(NoHurtCam.class).getState()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "orientCamera" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D") }, cancellable = true)
    private void cameraClip(final float partialTicks, final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(CameraClip.class).getState()) {
            callbackInfo.cancel();
            final Entity entity = this.mc.getRenderViewEntity();
            float f = entity.getEyeHeight();
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
                ++f;
                GlStateManager.translate(0.0f, 0.3f, 0.0f);
                if (!this.mc.gameSettings.debugCamEnable) {
                    final BlockPos blockpos = new BlockPos(entity);
                    final IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                    ForgeHooksClient.orientBedCamera((IBlockAccess)this.mc.theWorld, blockpos, iblockstate, entity);
                    GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                    GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
                }
            }
            else if (this.mc.gameSettings.thirdPersonView > 0) {
                final double d3 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;
                if (this.mc.gameSettings.debugCamEnable) {
                    GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
                }
                else {
                    final float f2 = entity.rotationYaw;
                    float f3 = entity.rotationPitch;
                    if (this.mc.gameSettings.thirdPersonView == 2) {
                        f3 += 180.0f;
                    }
                    if (this.mc.gameSettings.thirdPersonView == 2) {
                        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    }
                    GlStateManager.rotate(entity.rotationPitch - f3, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(entity.rotationYaw - f2, 0.0f, 1.0f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
                    GlStateManager.rotate(f2 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(f3 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
                }
            }
            else {
                GlStateManager.translate(0.0f, 0.0f, -0.1f);
            }
            if (!this.mc.gameSettings.debugCamEnable) {
                float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
                final float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                final float roll = 0.0f;
                if (entity instanceof EntityAnimal) {
                    final EntityAnimal entityanimal = (EntityAnimal)entity;
                    yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f;
                }
                final Block block = ActiveRenderInfo.getBlockAtEntityViewpoint((World)this.mc.theWorld, entity, partialTicks);
                final EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup((EntityRenderer)this, entity, block, (double)partialTicks, yaw, pitch, roll);
                MinecraftForge.EVENT_BUS.post((net.minecraftforge.fml.common.eventhandler.Event)event);
                GlStateManager.rotate(event.roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(event.pitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(event.yaw, 0.0f, 1.0f, 0.0f);
            }
            GlStateManager.translate(0.0f, -f, 0.0f);
            final double d4 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            final double d5 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
            final double d6 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            this.cloudFog = this.mc.renderGlobal.hasCloudFog(d4, d5, d6, partialTicks);
        }
    }
    
    @Inject(method = { "setupCameraTransform" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;setupViewBobbing(F)V", shift = At.Shift.BEFORE) })
    private void setupCameraViewBobbingBefore(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(Tracers.class).getState()) {
            GL11.glPushMatrix();
        }
    }
    
    @Inject(method = { "setupCameraTransform" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;setupViewBobbing(F)V", shift = At.Shift.AFTER) })
    private void setupCameraViewBobbingAfter(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(Tracers.class).getState()) {
            GL11.glPopMatrix();
        }
    }
    
    @Overwrite
    public void getMouseOver(final float p_getMouseOver_1_) {
        final Entity entity = this.mc.getRenderViewEntity();
        if (entity != null && this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            double d0 = this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(d0, p_getMouseOver_1_);
            double d2 = d0;
            final Vec3 vec3 = entity.getPositionEyes(p_getMouseOver_1_);
            boolean flag = false;
            if (this.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d2 = 6.0;
            }
            else if (d0 > 3.0 && !ModuleManager.getModule(Reach.class).getState()) {
                flag = true;
            }
            if (this.mc.objectMouseOver != null) {
                d2 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = entity.getLook(p_getMouseOver_1_);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            this.pointedEntity = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List<Entity> list = (List<Entity>)this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_.canBeCollidedWith()));
            double d3 = d2;
            for (int j = 0; j < list.size(); ++j) {
                final Entity entity2 = list.get(j);
                final float f2 = entity2.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand((double)f2, (double)f2, (double)f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d3 >= 0.0) {
                        this.pointedEntity = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                        d3 = 0.0;
                    }
                }
                else if (movingobjectposition != null) {
                    final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d4 < d3 || d3 == 0.0) {
                        if (entity2 == entity.ridingEntity && !entity.canRiderInteract()) {
                            if (d3 == 0.0) {
                                this.pointedEntity = entity2;
                                vec6 = movingobjectposition.hitVec;
                            }
                        }
                        else {
                            this.pointedEntity = entity2;
                            vec6 = movingobjectposition.hitVec;
                            d3 = d4;
                        }
                    }
                }
            }
            if (this.pointedEntity != null && flag && vec3.distanceTo(vec6) > 3.0) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, (EnumFacing)null, new BlockPos(vec6));
            }
            if (this.pointedEntity != null && (d3 < d2 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec6);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }
}
