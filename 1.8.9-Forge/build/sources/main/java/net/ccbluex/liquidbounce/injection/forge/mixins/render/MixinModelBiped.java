
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.HeadRotations;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBiped;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ ModelBiped.class })
public class MixinModelBiped
{
    @Shadow
    public ModelRenderer bipedRightArm;
    @Shadow
    public int heldItemRight;
    @Shadow
    public ModelRenderer bipedHead;
    
    @Inject(method = { "setRotationAngles" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F") })
    private void revertSwordAnimation(final float p_setRotationAngles_1_, final float p_setRotationAngles_2_, final float p_setRotationAngles_3_, final float p_setRotationAngles_4_, final float p_setRotationAngles_5_, final float p_setRotationAngles_6_, final Entity p_setRotationAngles_7_, final CallbackInfo callbackInfo) {
        if (this.heldItemRight == 3) {
            this.bipedRightArm.rotateAngleY = 0.0f;
        }
        if (ModuleManager.getModule(HeadRotations.class).getState() && RotationUtils.lastLook != null && p_setRotationAngles_7_ instanceof EntityPlayer && p_setRotationAngles_7_.equals((Object)Minecraft.getMinecraft().thePlayer)) {
            this.bipedHead.rotateAngleX = RotationUtils.lastLook[1] / 57.295776f;
        }
    }
}
