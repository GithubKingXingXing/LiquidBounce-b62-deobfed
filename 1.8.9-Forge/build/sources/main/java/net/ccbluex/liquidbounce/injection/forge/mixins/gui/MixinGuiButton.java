
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ GuiButton.class })
public abstract class MixinGuiButton
{
    @Shadow
    public boolean visible;
    @Shadow
    public int xPosition;
    @Shadow
    public int yPosition;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    protected boolean hovered;
    @Shadow
    public boolean enabled;
    @Shadow
    public String displayString;
    private float cut;
    private float alpha;
    
    @Shadow
    protected abstract void mouseDragged(final Minecraft p0, final int p1, final int p2);
    
    @Overwrite
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            FontRenderer fontRenderer = mc.fontRendererObj;
            final String languageCode = mc.getLanguageManager().getCurrentLanguage().getLanguageCode();
            if (languageCode.equalsIgnoreCase("de_DE") || languageCode.equalsIgnoreCase("en_US") || languageCode.equalsIgnoreCase("en_GB")) {
                fontRenderer = Fonts.font35;
            }
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int delta = RenderUtils.deltaTime;
            if (this.enabled) {
                if (this.hovered) {
                    this.alpha += 0.3f * delta;
                    if (this.alpha >= 210.0f) {
                        this.alpha = 210.0f;
                    }
                }
                else {
                    this.alpha -= 0.3f * delta;
                    if (this.alpha <= 120.0f) {
                        this.alpha = 120.0f;
                    }
                }
            }
            if (this.hovered) {
                this.cut += 0.05f * delta;
                if (this.cut >= 4.0f) {
                    this.cut = 4.0f;
                }
            }
            else {
                this.cut -= 0.05f * delta;
                if (this.cut <= 0.0f) {
                    this.cut = 0.0f;
                }
            }
            if (this.enabled) {
                Gui.drawRect(this.xPosition + (int)this.cut, this.yPosition, this.xPosition + this.width - (int)this.cut, this.yPosition + this.height, new Color(0.0f, 0.0f, 0.0f, this.alpha / 255.0f).getRGB());
            }
            else {
                Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, new Color(0.5f, 0.5f, 0.5f, 0.5f).getRGB());
            }
            this.mouseDragged(mc, mouseX, mouseY);
            fontRenderer.drawStringWithShadow(this.displayString, (float)(this.xPosition + this.width / 2 - fontRenderer.getStringWidth(this.displayString) / 2), (float)(this.yPosition + (this.height - 5) / 2), 14737632);
            GlStateManager.resetColor();
        }
    }
}
