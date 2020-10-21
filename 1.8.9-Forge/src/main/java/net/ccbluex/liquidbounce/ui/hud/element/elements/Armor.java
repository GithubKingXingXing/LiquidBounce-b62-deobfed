
package net.ccbluex.liquidbounce.ui.hud.element.elements;

import net.minecraft.item.ItemStack;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.hud.GuiHudDesigner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.block.material.Material;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.ui.hud.element.Element;

public class Armor extends Element
{
    private final Minecraft mc;
    
    public Armor() {
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawElement() {
        final int[] location = this.getLocationFromFacing();
        if (this.mc.playerController.isNotCreative()) {
            int x = location[0];
            GL11.glPushMatrix();
            for (int index = 3; index >= 0; --index) {
                final ItemStack stack = this.mc.thePlayer.inventory.armorInventory[index];
                if (stack != null) {
                    this.mc.getRenderItem().renderItemIntoGUI(stack, x, location[1] - (this.mc.thePlayer.isInsideOfMaterial(Material.water) ? 10 : 0));
                    this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, location[1] - (this.mc.thePlayer.isInsideOfMaterial(Material.water) ? 10 : 0));
                    x += 18;
                }
            }
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GL11.glPopMatrix();
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)location[0], (float)location[1], (float)(location[0] + 72), (float)(location[1] + 17), 3.0f, Integer.MIN_VALUE, 0);
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
        return mouseX >= location[0] && mouseY >= location[1] && mouseX <= location[0] + 72 && mouseY <= location[1] + 10;
    }
}
