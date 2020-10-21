
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.Timer;
import net.minecraft.client.renderer.entity.RenderManager;
import java.util.Iterator;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "ESP", description = "Allows you to see targets through walls.", category = ModuleCategory.RENDER)
public class ESP extends Module
{
    public final ListValue modeValue;
    public final FloatValue outlineWidth;
    public final FloatValue wireframeWidth;
    private final FloatValue shaderOutlineRadius;
    private final FloatValue shaderGlowRadius;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final BoolValue colorRainbow;
    private final BoolValue colorTeam;
    public static boolean renderNameTags;
    
    public ESP() {
        this.modeValue = new ListValue("Mode", new String[] { "Box", "OtherBox", "WireFrame", "2D", "Outline", "ShaderOutline", "ShaderGlow" }, "Box");
        this.outlineWidth = new FloatValue("Outline-Width", 3.0f, 0.5f, 5.0f);
        this.wireframeWidth = new FloatValue("WireFrame-Width", 2.0f, 0.5f, 5.0f);
        this.shaderOutlineRadius = new FloatValue("ShaderOutline-Radius", 1.35f, 1.0f, 2.0f);
        this.shaderGlowRadius = new FloatValue("ShaderGlow-Radius", 2.3f, 2.0f, 3.0f);
        this.colorRedValue = new IntegerValue("R", 255, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 255, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.colorTeam = new BoolValue("Team", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("esp", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".esp <mode>");
                    return;
                }
                if (args.length > 2 && ESP.this.modeValue.contains(args[2])) {
                    ESP.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7ESP mode was set to §8" + ESP.this.modeValue.asString().toUpperCase() + "§7.");
                    ESP$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".esp mode §c<§8" + Strings.join(ESP.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final String mode = this.modeValue.asString();
        for (final Entity entity : ESP.mc.theWorld.loadedEntityList) {
            if (entity != null && entity != ESP.mc.thePlayer && EntityUtils.isSelected(entity, false)) {
                final EntityLivingBase entityLiving = (EntityLivingBase)entity;
                final String lowerCase = mode.toLowerCase();
                switch (lowerCase) {
                    case "box":
                    case "otherbox": {
                        RenderUtils.drawEntityBox(entity, this.getColor((Entity)entityLiving), !mode.equalsIgnoreCase("otherbox"));
                        continue;
                    }
                    case "2d": {
                        final RenderManager renderManager = ESP.mc.getRenderManager();
                        final Timer timer = ESP.mc.timer;
                        final double posX = entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
                        final double posY = entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
                        final double posZ = entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;
                        RenderUtils.draw2D(entityLiving, posX, posY, posZ, this.getColor((Entity)entityLiving).getRGB(), Color.BLACK.getRGB());
                        continue;
                    }
                }
            }
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "shaderoutline": {
                OutlineShader.OUTLINE_SHADER.startDraw(event.getPartialTicks());
                ESP.renderNameTags = false;
                ESP.mc.theWorld.loadedEntityList.stream().filter(entity -> EntityUtils.isSelected(entity, false)).forEach(entity -> ESP.mc.getRenderManager().renderEntityStatic(entity, ESP.mc.timer.renderPartialTicks, true));
                ESP.renderNameTags = true;
                OutlineShader.OUTLINE_SHADER.stopDraw(this.getColor(null), this.shaderOutlineRadius.asFloat(), 1.0f);
                break;
            }
            case "shaderglow": {
                GlowShader.GLOW_SHADER.startDraw(event.getPartialTicks());
                ESP.renderNameTags = false;
                ESP.mc.theWorld.loadedEntityList.stream().filter(entity -> EntityUtils.isSelected(entity, false)).forEach(entity -> ESP.mc.getRenderManager().renderEntityStatic(entity, ESP.mc.timer.renderPartialTicks, true));
                ESP.renderNameTags = true;
                GlowShader.GLOW_SHADER.stopDraw(this.getColor(null), this.shaderGlowRadius.asFloat(), 1.0f);
                break;
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
    
    public Color getColor(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            if (entityLivingBase.hurtTime > 0) {
                return Color.RED;
            }
            if (EntityUtils.isFriend((Entity)entityLivingBase)) {
                return Color.BLUE;
            }
            if (this.colorTeam.asBoolean()) {
                final char[] chars = entityLivingBase.getDisplayName().getFormattedText().toCharArray();
                int color = Integer.MAX_VALUE;
                final String colors = "0123456789abcdef";
                for (int i = 0; i < chars.length; ++i) {
                    if (chars[i] == '§') {
                        if (i + 1 < chars.length) {
                            final int index = "0123456789abcdef".indexOf(chars[i + 1]);
                            if (index != -1) {
                                color = ChatColor.colors[index];
                                break;
                            }
                        }
                    }
                }
                return new Color(color);
            }
        }
        return this.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger());
    }
    
    static {
        ESP.renderNameTags = true;
    }
}
