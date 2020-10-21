//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiSlot.class })
public abstract class MixinGuiSlot
{
    @Shadow
    protected boolean field_178041_q;
    @Shadow
    protected int mouseX;
    @Shadow
    protected int mouseY;
    @Shadow
    public int left;
    @Shadow
    public int top;
    @Shadow
    public int width;
    @Shadow
    protected float amountScrolled;
    @Shadow
    protected boolean hasListHeader;
    @Shadow
    public int right;
    @Shadow
    public int bottom;
    @Shadow
    @Final
    protected Minecraft mc;
    @Shadow
    public int height;
    
    @Shadow
    protected abstract void drawBackground();
    
    @Shadow
    protected abstract void bindAmountScrolled();
    
    @Shadow
    public abstract int getListWidth();
    
    @Shadow
    protected abstract void drawListHeader(final int p0, final int p1, final Tessellator p2);
    
    @Shadow
    protected abstract void drawSelectionBox(final int p0, final int p1, final int p2, final int p3);
    
    @Shadow
    protected abstract int getContentHeight();
    
    @Shadow
    public abstract int func_148135_f();
    
    @Shadow
    protected abstract void func_148142_b(final int p0, final int p1);
    
    @Overwrite
    public void drawScreen(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
        if (this.field_178041_q) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            final int i = this.getScrollBarX();
            final int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            final int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int l = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }
            this.drawSelectionBox(k, l + 2, mouseXIn, mouseYIn + 2);
            GlStateManager.disableDepth();
            final int i2 = 4;
            final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            Gui.drawRect(0, 0, scaledResolution.getScaledWidth(), this.top, Integer.MIN_VALUE);
            Gui.drawRect(0, this.bottom, scaledResolution.getScaledWidth(), this.height, Integer.MIN_VALUE);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos((double)this.left, (double)(this.top + i2), 0.0).tex(0.0, 1.0).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos((double)this.right, (double)(this.top + i2), 0.0).tex(1.0, 1.0).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos((double)this.right, (double)this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos((double)this.left, (double)this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos((double)this.left, (double)this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos((double)this.right, (double)this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            worldrenderer.pos((double)this.right, (double)(this.bottom - i2), 0.0).tex(1.0, 0.0).color(0, 0, 0, 0).endVertex();
            worldrenderer.pos((double)this.left, (double)(this.bottom - i2), 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex();
            tessellator.draw();
            final int j2 = this.func_148135_f();
            if (j2 > 0) {
                int k2 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k2 = MathHelper.clamp_int(k2, 32, this.bottom - this.top - 8);
                int l2 = (int)this.amountScrolled * (this.bottom - this.top - k2) / j2 + this.top;
                if (l2 < this.top) {
                    l2 = this.top;
                }
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos((double)i, (double)this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double)j, (double)this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double)j, (double)this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos((double)i, (double)this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos((double)i, (double)(l2 + k2), 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double)j, (double)(l2 + k2), 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double)j, (double)l2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos((double)i, (double)l2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos((double)i, (double)(l2 + k2 - 1), 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((double)(j - 1), (double)(l2 + k2 - 1), 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((double)(j - 1), (double)l2, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
                worldrenderer.pos((double)i, (double)l2, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }
            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
    
    @Overwrite
    protected int getScrollBarX() {
        return this.width - 5;
    }
}
