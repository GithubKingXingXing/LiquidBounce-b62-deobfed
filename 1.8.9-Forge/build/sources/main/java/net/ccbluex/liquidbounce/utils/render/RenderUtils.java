//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render;

import java.util.HashMap;
import java.math.BigDecimal;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.BiConsumer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.util.Timer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.World;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.util.BlockPos;
import java.util.Map;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RenderUtils
{
    private static final Map<Integer, Boolean> glCapMap;
    public static int deltaTime;
    
    public static void drawBlockBox(final BlockPos blockPos, final Color color, final boolean outline) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        final Timer timer = Minecraft.getMinecraft().timer;
        final double x = blockPos.getX() - renderManager.renderPosX;
        final double y = blockPos.getY() - renderManager.renderPosY;
        final double z = blockPos.getZ() - renderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = BlockUtils.getBlock(blockPos);
        if (block != null) {
            final double d0 = Minecraft.getMinecraft().thePlayer.lastTickPosX + (Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.lastTickPosX) * timer.renderPartialTicks;
            final double d2 = Minecraft.getMinecraft().thePlayer.lastTickPosY + (Minecraft.getMinecraft().thePlayer.posY - Minecraft.getMinecraft().thePlayer.lastTickPosY) * timer.renderPartialTicks;
            final double d3 = Minecraft.getMinecraft().thePlayer.lastTickPosZ + (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.lastTickPosZ) * timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox((World)Minecraft.getMinecraft().theWorld, blockPos).expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(-d0, -d2, -d3);
        }
        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        GL11.glLineWidth(1.0f);
        GL11.glColor4d(0.0, 1.0, 0.0, 0.15000000596046448);
        disableGlCap(3553, 2929);
        GL11.glDepthMask(false);
        glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (color.getAlpha() == 255) ? (outline ? 26 : 35) : color.getAlpha()));
        drawFilledBox(axisAlignedBB);
        if (outline) {
            glColor(color);
            RenderGlobal.drawSelectionBoundingBox(axisAlignedBB);
        }
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        resetCaps();
    }
    
    public static void drawEntityBox(final Entity entity, final Color color, final boolean outline) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        disableGlCap(3553, 2929);
        GL11.glLineWidth(1.0f);
        GL11.glDepthMask(false);
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + (entity.posX - renderManager.renderPosX), entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - renderManager.renderPosY), entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + (entity.posZ - renderManager.renderPosZ), entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + (entity.posX - renderManager.renderPosX), entity.getEntityBoundingBox().maxY + 0.15 - entity.posY + (entity.posY - renderManager.renderPosY), entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + (entity.posZ - renderManager.renderPosZ));
        if (outline) {
            glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 95));
            RenderGlobal.drawSelectionBoundingBox(axisAlignedBB);
        }
        glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35));
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        resetCaps();
    }
    
    public static void drawTracer(final Entity entity, final Color color) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        final double x = entity.posX - renderManager.renderPosX;
        final double y = entity.posY - renderManager.renderPosY;
        final double z = entity.posZ - renderManager.renderPosZ;
        final Vec3 eyeVector = new Vec3(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationPitch)).rotateYaw(-(float)Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw));
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        glColor(color);
        GL11.glBegin(1);
        GL11.glVertex3d(eyeVector.xCoord, Minecraft.getMinecraft().thePlayer.getEyeHeight() + eyeVector.yCoord, eyeVector.zCoord);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + entity.height, z);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GlStateManager.resetColor();
    }
    
    public static void drawAxisAlignedBB(final AxisAlignedBB axisAlignedBB, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        if (color != null) {
            glColor(color);
        }
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawPlatform(final double x, final double y, final double z, final Color color, final double size) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        final double renderX = x - renderManager.renderPosX;
        final double renderY = y - renderManager.renderPosY;
        final double renderZ = z - renderManager.renderPosZ;
        drawAxisAlignedBB(new AxisAlignedBB(renderX + size, renderY + 0.02, renderZ + size, renderX - size, renderY, renderZ - size), color);
    }
    
    public static void drawPlatform(final Entity entity, final Color color) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        drawAxisAlignedBB(new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + (entity.posX - renderManager.renderPosX), entity.getEntityBoundingBox().maxY - renderManager.renderPosY + 0.2, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + (entity.posZ - renderManager.renderPosZ), entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + (entity.posX - renderManager.renderPosX), entity.getEntityBoundingBox().maxY - renderManager.renderPosY + 0.26, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + (entity.posZ - renderManager.renderPosZ)), color);
    }
    
    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }
    
    public static void drawRect(final float x, final float y, final float x2, final float y2, final int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawRect(final float x, final float y, final float x2, final float y2, final Color color) {
        drawRect(x, y, x2, y2, color.getRGB());
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float width, final int color1, final int color2) {
        drawRect(x, y, x2, y2, color2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        glColor(color1);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawLoadingCircle(final float x, final float y) {
        for (int i = 0; i < 4; ++i) {
            final int rot = (int)(System.nanoTime() / 5000000L * i % 360L);
            drawCircle(x, y, (float)(i * 10), rot - 180, rot);
        }
    }
    
    public static void drawCircle(final float x, final float y, final float radius, final int start, final int end) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(Color.WHITE);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        for (float i = (float)end; i >= start; i -= 4.0f) {
            GL11.glVertex2f((float)(x + Math.cos(i * 3.141592653589793 / 180.0) * (radius * 1.001f)), (float)(y + Math.sin(i * 3.141592653589793 / 180.0) * (radius * 1.001f)));
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawFilledCircle(final int xx, final int yy, final float radius, final Color col) {
        final int sections = 50;
        final double dAngle = 6.283185307179586 / sections;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; ++i) {
            final float x = (float)(radius * Math.sin(i * dAngle));
            final float y = (float)(radius * Math.cos(i * dAngle));
            GL11.glColor4f(col.getRed() / 255.0f, col.getGreen() / 255.0f, col.getBlue() / 255.0f, col.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    private static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void resetCaps() {
        RenderUtils.glCapMap.forEach(RenderUtils::setGlState);
    }
    
    public static void enableGlCap(final int cap) {
        setGlCap(cap, true);
    }
    
    public static void enableGlCap(final int... caps) {
        for (final int cap : caps) {
            setGlCap(cap, true);
        }
    }
    
    public static void disableGlCap(final int cap) {
        setGlCap(cap, true);
    }
    
    public static void disableGlCap(final int... caps) {
        for (final int cap : caps) {
            setGlCap(cap, false);
        }
    }
    
    public static void setGlCap(final int cap, final boolean state) {
        RenderUtils.glCapMap.put(cap, GL11.glGetBoolean(cap));
        setGlState(cap, state);
    }
    
    public static void setGlState(final int cap, final boolean state) {
        if (state) {
            GL11.glEnable(cap);
        }
        else {
            GL11.glDisable(cap);
        }
    }
    
    public static void draw2D(final EntityLivingBase entity, final double posX, final double posY, final double posZ, final int color, final int backgroundColor) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.translate(0.0, 21.0 + -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0, 0.0);
        drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        drawRect(4.0f, -20.3f, 7.3f, -20.0f, backgroundColor);
        drawRect(-7.3f, -20.3f, -4.0f, -20.0f, backgroundColor);
        GL11.glEnable(2929);
        GlStateManager.popMatrix();
    }
    
    public static void draw2D(final BlockPos blockPos, final int color, final int backgroundColor) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        final double posX = blockPos.getX() + 0.5 - renderManager.renderPosX;
        final double posY = blockPos.getY() - renderManager.renderPosY;
        final double posZ = blockPos.getZ() + 0.5 - renderManager.renderPosZ;
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        setGlCap(2929, false);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.translate(0.0f, 9.0f, 0.0f);
        drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        drawRect(4.0f, -20.3f, 7.3f, -20.0f, backgroundColor);
        drawRect(-7.3f, -20.3f, -4.0f, -20.0f, backgroundColor);
        resetCaps();
        GlStateManager.popMatrix();
    }
    
    public static void renderNameTag(final String string, final double x, final double y, final double z) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GL11.glPushMatrix();
        GL11.glTranslated(x - renderManager.renderPosX, y - renderManager.renderPosY, z - renderManager.renderPosZ);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-0.05f, -0.05f, 0.05f);
        setGlCap(2896, false);
        setGlCap(2929, false);
        setGlCap(3042, true);
        GL11.glBlendFunc(770, 771);
        final int width = Fonts.font35.getStringWidth(string) / 2;
        Gui.drawRect(-width - 1, -1, width + 1, Fonts.font35.FONT_HEIGHT, Integer.MIN_VALUE);
        Fonts.font35.drawString(string, (float)(-width), 1.5f, Color.WHITE.getRGB(), true);
        resetCaps();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public static void drawLine(final double x, final double y, final double x1, final double y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    public static void makeScissorBox(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((scaledResolution.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    public static float drawSlider(final float value, final float min, final float max, final int x, final int y, final int width, final int mouseX, final int mouseY, final Color color) {
        final float displayValue = Math.max(min, Math.min(value, max));
        final float sliderValue = x + width * (displayValue - min) / (max - min);
        drawRect((float)x, (float)y, (float)(x + width), (float)(y + 2), Integer.MAX_VALUE);
        drawRect((float)x, (float)y, sliderValue, (float)(y + 2), color);
        drawFilledCircle((int)sliderValue, y + 1, 3.0f, color);
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 3 && Mouse.isButtonDown(0)) {
            final double i = MathHelper.clamp_double((mouseX - (double)x) / (width - 3.0), 0.0, 1.0);
            BigDecimal bigDecimal = new BigDecimal(Double.toString(min + (max - min) * i));
            bigDecimal = bigDecimal.setScale(2, 4);
            return bigDecimal.floatValue();
        }
        return value;
    }
    
    static {
        glCapMap = new HashMap<Integer, Boolean>();
    }
}
