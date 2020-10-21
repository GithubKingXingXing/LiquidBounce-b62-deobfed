
package net.ccbluex.liquidbounce.ui.hud.element.elements;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Model extends Element
{
    private float rotate;
    private boolean rotateDirection;
    
    @Override
    public void drawElement() {
        final int delta = RenderUtils.deltaTime;
        if (this.rotateDirection) {
            if (this.rotate <= 70.0f) {
                this.rotate += 0.12f * delta;
            }
            else {
                this.rotateDirection = false;
                this.rotate = 70.0f;
            }
        }
        else if (this.rotate >= -70.0f) {
            this.rotate -= 0.12f * delta;
        }
        else {
            this.rotateDirection = true;
            this.rotate = -70.0f;
        }
        final int[] location = this.getLocationFromFacing();
        final EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().thePlayer;
        float pitch = entityPlayerSP.rotationPitch;
        if (pitch > 0.0f) {
            pitch = -entityPlayerSP.rotationPitch;
        }
        else {
            pitch = Math.abs(entityPlayerSP.rotationPitch);
        }
        this.drawEntityOnScreen(location[0], location[1], this.rotate, pitch, (EntityLivingBase)entityPlayerSP);
        if (Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)(location[0] + 30), (float)(location[1] + 10), (float)(location[0] - 30), (float)(location[1] - 100), 3.0f, Integer.MIN_VALUE, 0);
        }
    }
    
    @Override
    public void destroyElement() {
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void handleKey(final char c, final int keyCode) {
    }
    
    @Override
    public boolean isMouseOverElement(final int mouseX, final int mouseY) {
        final int[] location = this.getLocationFromFacing();
        return mouseX >= location[0] - 30 && mouseY >= location[1] - 100 && mouseX <= location[0] + 30 && mouseY <= location[1] + 10;
    }
    
    private void drawEntityOnScreen(final int x, final int y, final float yaw, final float pitch, final EntityLivingBase entityLivingBase) {
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, 50.0f);
        GlStateManager.scale(-50.0f, 50.0f, 50.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float var6 = entityLivingBase.renderYawOffset;
        final float var7 = entityLivingBase.rotationYaw;
        final float var8 = entityLivingBase.rotationPitch;
        final float var9 = entityLivingBase.prevRotationYawHead;
        final float var10 = entityLivingBase.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(pitch / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        entityLivingBase.renderYawOffset = (float)Math.atan(yaw / 40.0f) * 20.0f;
        entityLivingBase.rotationYaw = (float)Math.atan(yaw / 40.0f) * 40.0f;
        entityLivingBase.rotationPitch = -(float)Math.atan(pitch / 40.0f) * 20.0f;
        entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw;
        entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0f);
        var11.setRenderShadow(false);
        var11.renderEntityWithPosYaw((Entity)entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.setRenderShadow(true);
        entityLivingBase.renderYawOffset = var6;
        entityLivingBase.rotationYaw = var7;
        entityLivingBase.rotationPitch = var8;
        entityLivingBase.prevRotationYawHead = var9;
        entityLivingBase.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.resetColor();
    }
}
