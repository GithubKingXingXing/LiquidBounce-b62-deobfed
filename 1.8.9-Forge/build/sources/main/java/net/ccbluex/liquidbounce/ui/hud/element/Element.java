//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.ui.hud.element;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Element
{
    private int x;
    private int y;
    private float scale;
    private Facing facing;
    public boolean drag;
    public int prevMouseX;
    public int prevMouseY;
    
    public Element() {
        this.scale = 1.0f;
    }
    
    public abstract void drawElement();
    
    public abstract void destroyElement();
    
    public abstract void handleMouseClick(final int p0, final int p1, final int p2);
    
    public abstract void handleKey(final char p0, final int p1);
    
    public abstract boolean isMouseOverElement(final int p0, final int p1);
    
    public int getX() {
        return this.x;
    }
    
    public Element setX(final int x) {
        this.x = x;
        return this;
    }
    
    public int getY() {
        return this.y;
    }
    
    public Element setY(final int y) {
        this.y = y;
        return this;
    }
    
    public Element setScale(float scale) {
        if (scale < 0.0f) {
            scale = 0.0f;
        }
        this.scale = scale;
        return this;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public Facing getFacing() {
        return this.facing;
    }
    
    public Element setFacing(final Facing facing) {
        this.facing = facing;
        return this;
    }
    
    public int[] getLocationFromFacing() {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        return new int[] { (this.facing.getHorizontal() == Facing.Horizontal.RIGHT) ? (scaledResolution.getScaledWidth() - this.x) : ((this.facing.getHorizontal() == Facing.Horizontal.MIDDLE) ? (scaledResolution.getScaledWidth() / 2 + this.x) : this.x), (this.facing.getVertical() == Facing.Vertical.DOWN) ? (scaledResolution.getScaledHeight() - this.y) : ((this.facing.getVertical() == Facing.Vertical.MIDDLE) ? (scaledResolution.getScaledHeight() / 2 + this.y) : this.y) };
    }
    
    public Element setScreenX(final int x) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        switch (this.facing.getHorizontal()) {
            case LEFT: {
                this.x = x;
                break;
            }
            case MIDDLE: {
                this.x = x - scaledResolution.getScaledWidth() / 2;
                break;
            }
            case RIGHT: {
                this.x = scaledResolution.getScaledWidth() - x;
                break;
            }
        }
        return this;
    }
    
    public Element setScreenY(final int y) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        switch (this.facing.getVertical()) {
            case UP: {
                this.y = y;
                break;
            }
            case MIDDLE: {
                this.y = y - scaledResolution.getScaledHeight() / 2;
                break;
            }
            case DOWN: {
                this.y = scaledResolution.getScaledHeight() - y;
                break;
            }
        }
        return this;
    }
}
