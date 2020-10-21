
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.util.Timer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.minecraft.client.gui.FontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.valuesystem.types.FontValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NameTags", description = "Changes the scale of the nametags so you can always read them.", category = ModuleCategory.RENDER)
public class NameTags extends Module
{
    private final BoolValue healthValue;
    private final BoolValue pingValue;
    private final BoolValue distanceValue;
    private final BoolValue armorValue;
    private final BoolValue clearNamesValue;
    private final FontValue fontValue;
    
    public NameTags() {
        this.healthValue = new BoolValue("Health", true);
        this.pingValue = new BoolValue("Ping", true);
        this.distanceValue = new BoolValue("Distance", false);
        this.armorValue = new BoolValue("Armor", true);
        this.clearNamesValue = new BoolValue("ClearNames", false);
        this.fontValue = new FontValue("Font", Fonts.font40);
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        NameTags.mc.theWorld.loadedEntityList.stream().filter(entity -> entity != NameTags.mc.thePlayer && EntityUtils.isSelected(entity, false)).forEach(entity -> this.renderNameTag(entity, this.clearNamesValue.asBoolean() ? ChatColor.stripColor(((Entity)entity).getDisplayName().getUnformattedText()) : ((Entity)entity).getDisplayName().getUnformattedText()));
    }
    
    private void renderNameTag(final EntityLivingBase entity, String tag) {
        final FontRenderer fontRenderer = (FontRenderer)this.fontValue.asObject();
        final boolean bot = AntiBot.isBot(entity);
        final ChatColor nameColor = bot ? ChatColor.DARK_BLUE : (entity.isInvisible() ? ChatColor.GOLD : (entity.isSneaking() ? ChatColor.DARK_RED : ChatColor.GRAY));
        final int ping = (entity instanceof EntityPlayer) ? EntityUtils.getPing((EntityPlayer)entity) : 0;
        tag = (this.distanceValue.asBoolean() ? ("§7" + Math.round(NameTags.mc.thePlayer.getDistanceToEntity((Entity)entity)) + "m ") : "") + ((this.pingValue.asBoolean() && entity instanceof EntityPlayer) ? (((ping > 200) ? "§c" : ((ping > 100) ? "§e" : "§a")) + ping + "ms §7") : "") + nameColor + tag + (this.healthValue.asBoolean() ? ("§7§c " + (int)entity.getHealth() + "\u2764") : ("" + (bot ? " §c§lBot" : "")));
        GL11.glPushMatrix();
        final RenderManager renderManager = NameTags.mc.getRenderManager();
        final Timer timer = NameTags.mc.timer;
        GL11.glTranslated(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.getEyeHeight() + 0.550000011920929, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ);
        GL11.glRotatef(-NameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(NameTags.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        float distance = NameTags.mc.thePlayer.getDistanceToEntity((Entity)entity) / 4.0f;
        if (distance < 1.0f) {
            distance = 1.0f;
        }
        final float scale = distance / 100.0f;
        GL11.glScalef(-scale, -scale, scale);
        RenderUtils.disableGlCap(2896, 2929);
        RenderUtils.enableGlCap(3042);
        GL11.glBlendFunc(770, 771);
        final int width = fontRenderer.getStringWidth(tag) / 2;
        RenderUtils.drawBorderedRect((float)(-width - 2), -2.0f, (float)(width + 2), (float)(fontRenderer.FONT_HEIGHT + 2), 2.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        fontRenderer.drawString(tag, (float)(-width), (fontRenderer == Fonts.minecraftFont) ? 0.0f : 1.5f, 16777215, true);
        if (this.armorValue.asBoolean() && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            for (int index = 0; index < 5; ++index) {
                if (player.getEquipmentInSlot(index) != null) {
                    NameTags.mc.getRenderItem().zLevel = -147.0f;
                    NameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(player.getEquipmentInSlot(index), -50 + index * 20, -22);
                }
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
        RenderUtils.resetCaps();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
