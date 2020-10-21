
package net.ccbluex.liquidbounce.ui.hud.element.elements;

import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Effects extends Element
{
    private FontRenderer fontRenderer;
    private final BoolValue shadow;
    private int x2;
    private int y2;
    
    public Effects() {
        this.fontRenderer = Fonts.font35;
        this.shadow = new BoolValue("Shadow", true);
    }
    
    @Override
    public void drawElement() {
        final int[] location = this.getLocationFromFacing();
        int y = location[1];
        int width = 0;
        for (final PotionEffect effect : Minecraft.getMinecraft().thePlayer.getActivePotionEffects()) {
            final Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName(), new Object[0]);
            if (effect.getAmplifier() == 1) {
                name += " II";
            }
            else if (effect.getAmplifier() == 2) {
                name += " III";
            }
            else if (effect.getAmplifier() == 3) {
                name += " IV";
            }
            else if (effect.getAmplifier() == 4) {
                name += " V";
            }
            else if (effect.getAmplifier() == 5) {
                name += " VI";
            }
            else if (effect.getAmplifier() == 6) {
                name += " VII";
            }
            else if (effect.getAmplifier() == 7) {
                name += " VIII";
            }
            else if (effect.getAmplifier() == 8) {
                name += " IX";
            }
            else if (effect.getAmplifier() == 9) {
                name += " X";
            }
            else if (effect.getAmplifier() > 10) {
                name += " X+";
            }
            else {
                name += " I";
            }
            name = name + "§f: §7" + Potion.getDurationString(effect);
            if (width < this.fontRenderer.getStringWidth(name)) {
                width = this.fontRenderer.getStringWidth(name);
            }
            this.fontRenderer.drawString(name, (float)(location[0] - this.fontRenderer.getStringWidth(name)), (float)y, potion.getLiquidColor(), this.shadow.asBoolean());
            y -= this.fontRenderer.FONT_HEIGHT;
        }
        if (width == 0) {
            width = 40;
        }
        if (location[1] == y) {
            y = location[1] - 10;
        }
        this.y2 = y + this.fontRenderer.FONT_HEIGHT - 2;
        this.x2 = location[0] - width;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)(location[0] + 2), (float)(location[1] + this.fontRenderer.FONT_HEIGHT), (float)(this.x2 - 2), (float)this.y2, 3.0f, Integer.MIN_VALUE, 0);
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
        return mouseX <= location[0] + 2 && mouseY <= location[1] + this.fontRenderer.FONT_HEIGHT && mouseX >= this.x2 - 2 && mouseY >= this.y2;
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public Effects setFontRenderer(final FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }
    
    public boolean isShadow() {
        return this.shadow.asBoolean();
    }
    
    public Effects setShadow(final boolean b) {
        this.shadow.setValue(b);
        return this;
    }
}
