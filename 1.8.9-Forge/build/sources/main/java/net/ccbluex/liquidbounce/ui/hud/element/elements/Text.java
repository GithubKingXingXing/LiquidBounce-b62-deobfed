//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element.elements;

import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.TextValue;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Text extends Element
{
    private final TextValue displayString;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final BoolValue rainbow;
    private final BoolValue shadow;
    private FontRenderer fontRenderer;
    private boolean editMode;
    private float editTicks;
    private long prevClick;
    
    public Text() {
        this.displayString = new TextValue("DisplayText", "");
        final Color c = Color.WHITE;
        this.redValue = new IntegerValue("Red", c.getRed(), 0, 255);
        this.greenValue = new IntegerValue("Green", c.getGreen(), 0, 255);
        this.blueValue = new IntegerValue("Blue", c.getBlue(), 0, 255);
        this.rainbow = new BoolValue("Rainbow", false);
        this.shadow = new BoolValue("Shadow", true);
        this.fontRenderer = Fonts.font40;
    }
    
    @Override
    public void drawElement() {
        this.editTicks += 0.1f * RenderUtils.deltaTime;
        if (this.editTicks > 80.0f) {
            this.editTicks = 0.0f;
        }
        final int color = new Color(this.redValue.asInteger(), this.greenValue.asInteger(), this.blueValue.asInteger()).getRGB();
        final int[] location = this.getLocationFromFacing();
        this.fontRenderer.drawString(this.editMode ? this.displayString.asString() : this.getDisplay(), (float)location[0], (float)location[1], this.rainbow.asBoolean() ? ColorUtils.rainbow(400000000L).getRGB() : color, this.shadow.asBoolean());
        if (this.editMode && Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner && this.editTicks <= 40.0f) {
            this.fontRenderer.drawString("_", (float)(location[0] + this.fontRenderer.getStringWidth(this.displayString.asString()) + 2), (float)location[1], this.rainbow.asBoolean() ? ColorUtils.rainbow(400000000L).getRGB() : color, this.shadow.asBoolean());
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)(location[0] - 2), (float)(location[1] - 2), (float)(location[0] + this.fontRenderer.getStringWidth(this.editMode ? this.displayString.asString() : this.getDisplay()) + 2), (float)(location[1] + this.fontRenderer.FONT_HEIGHT), 3.0f, Integer.MIN_VALUE, 0);
        }
    }
    
    @Override
    public void destroyElement() {
    }
    
    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isMouseOverElement(mouseX, mouseY) && mouseButton == 0) {
            if (System.currentTimeMillis() - this.prevClick <= 250L) {
                this.editMode = true;
            }
            this.prevClick = System.currentTimeMillis();
        }
        else {
            this.editMode = false;
        }
    }
    
    @Override
    public void handleKey(final char c, final int keyCode) {
        if (this.editMode && Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            if (keyCode == 14) {
                if (this.displayString.asString().length() > 0) {
                    this.displayString.setValue(this.displayString.asString().substring(0, this.displayString.asString().length() - 1));
                }
                return;
            }
            if (ChatAllowedCharacters.isAllowedCharacter(c) || c == '§') {
                this.displayString.setValue(this.displayString.asString() + String.valueOf(c));
            }
        }
    }
    
    @Override
    public boolean isMouseOverElement(final int mouseX, final int mouseY) {
        final int[] location = this.getLocationFromFacing();
        return mouseX >= location[0] && mouseY >= location[1] && mouseX <= location[0] + this.fontRenderer.getStringWidth(this.displayString.asString().isEmpty() ? "Text Element" : this.getDisplay()) && mouseY <= location[1] + this.fontRenderer.FONT_HEIGHT;
    }
    
    private String getDisplay() {
        String s = (this.displayString.asString().isEmpty() && !this.editMode) ? "Text Element" : this.displayString.asString();
        if (s.contains("%")) {
            s = StringUtils.replace(s, "%username%", Minecraft.getMinecraft().getSession().getUsername());
            s = StringUtils.replace(s, "%clientName%", "LiquidBounce");
            s = StringUtils.replace(s, "%clientVersion%", "b62");
            s = StringUtils.replace(s, "%clientCreator%", "CCBlueX");
            s = StringUtils.replace(s, "%fps%", String.valueOf(Minecraft.getDebugFPS()));
            if (Minecraft.getMinecraft().thePlayer != null) {
                s = StringUtils.replace(s, "%x%", String.valueOf((int)Minecraft.getMinecraft().thePlayer.posX));
                s = StringUtils.replace(s, "%y%", String.valueOf((int)Minecraft.getMinecraft().thePlayer.posY));
                s = StringUtils.replace(s, "%z%", String.valueOf((int)Minecraft.getMinecraft().thePlayer.posZ));
                s = StringUtils.replace(s, "%ping%", String.valueOf(EntityUtils.getPing((EntityPlayer)Minecraft.getMinecraft().thePlayer)));
            }
        }
        return s;
    }
    
    public Text setText(final String s) {
        this.displayString.setValue(s);
        return this;
    }
    
    public Text setColor(final Color c) {
        this.redValue.setValue(c.getRed());
        this.greenValue.setValue(c.getGreen());
        this.blueValue.setValue(c.getBlue());
        return this;
    }
    
    public Text setRainbow(final boolean b) {
        this.rainbow.setValue(b);
        return this;
    }
    
    public Text setShadow(final boolean b) {
        this.shadow.setValue(b);
        return this;
    }
    
    public Text setFontRenderer(final FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }
}
