//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui;

import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class GuiIconButton extends GuiButton
{
    private float hover;
    private final ResourceLocation resourceLocation;
    
    public GuiIconButton(final int buttonId, final int x, final int y, final int width, final int height, final String buttonText, final ResourceLocation resourceLocation) {
        super(buttonId, x, y, width, height, buttonText);
        this.resourceLocation = resourceLocation;
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition - this.hover && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int delta = RenderUtils.deltaTime;
            if (this.hovered) {
                this.hover += 0.03f * delta;
                if (this.hover >= 5.0f) {
                    this.hover = 5.0f;
                }
            }
            else {
                this.hover -= 0.03f * delta;
                if (this.hover < 0.0f) {
                    this.hover = 0.0f;
                }
            }
            RenderUtils.drawImage(this.resourceLocation, this.xPosition, this.yPosition - (int)this.hover, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            this.drawCenteredString((FontRenderer)Fonts.font35, this.displayString, this.xPosition + this.width / 2, this.yPosition + this.height + 7 - (int)this.hover, Color.WHITE.getRGB());
        }
    }
}
