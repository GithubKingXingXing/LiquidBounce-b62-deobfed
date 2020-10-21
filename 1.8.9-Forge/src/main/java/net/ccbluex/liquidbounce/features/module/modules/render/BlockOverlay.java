
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BlockOverlay", description = "Allows you to change the design of the block overlay.", category = ModuleCategory.RENDER)
public class BlockOverlay extends Module
{
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final BoolValue colorRainbow;
    public final BoolValue infoValue;
    
    public BlockOverlay() {
        this.colorRedValue = new IntegerValue("R", 68, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 117, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.infoValue = new BoolValue("Info", false);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("blockoverlay", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1 && args[1].equalsIgnoreCase("color")) {
                    if (args.length > 2) {
                        if (args[2].equalsIgnoreCase("rainbow")) {
                            BlockOverlay.this.colorRainbow.setValue(!BlockOverlay.this.colorRainbow.asBoolean());
                            this.chat("§a§lRainbow §7was toggled §c§l" + (BlockOverlay.this.colorRainbow.asBoolean() ? "on" : "off") + "§7.");
                            BlockOverlay$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args.length > 4) {
                            try {
                                final int r = Integer.parseInt(args[2]);
                                final int g = Integer.parseInt(args[3]);
                                final int b = Integer.parseInt(args[4]);
                                if (r > 255 || g > 255 || b > 255) {
                                    this.chatSyntaxError();
                                    return;
                                }
                                BlockOverlay.this.colorRedValue.setValue(r);
                                BlockOverlay.this.colorGreenValue.setValue(g);
                                BlockOverlay.this.colorBlueValue.setValue(b);
                                this.chat("§a§lRGB §7was set to §c§l" + r + ", " + g + ", " + b + "§7.");
                                BlockOverlay$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException e) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                    }
                    this.chatSyntax(".blockoverlay color <r> <g> <b> / .blockoverlay color <rainbow>");
                    return;
                }
                this.chatSyntax(".blockoverlay <color>");
            }
        });
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (BlockOverlay.mc.objectMouseOver != null && BlockOverlay.mc.objectMouseOver.getBlockPos() != null && BlockOverlay.mc.theWorld.getBlockState(BlockOverlay.mc.objectMouseOver.getBlockPos()).getBlock() != null && BlockUtils.canBeClicked(BlockOverlay.mc.objectMouseOver.getBlockPos())) {
            final float partialTicks = event.getPartialTicks();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final Color color = this.colorRainbow.asBoolean() ? ColorUtils.rainbow(0.4f) : new Color(this.colorRedValue.asInteger(), this.colorGreenValue.asInteger(), this.colorBlueValue.asInteger(), 102);
            RenderUtils.glColor(color);
            GL11.glLineWidth(2.0f);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            final BlockPos blockPos = BlockOverlay.mc.objectMouseOver.getBlockPos();
            final Block block = BlockOverlay.mc.theWorld.getBlockState(blockPos).getBlock();
            if (BlockOverlay.mc.theWorld.getWorldBorder().contains(blockPos)) {
                block.setBlockBoundsBasedOnState((IBlockAccess)BlockOverlay.mc.theWorld, blockPos);
                final double d0 = BlockOverlay.mc.thePlayer.lastTickPosX + (BlockOverlay.mc.thePlayer.posX - BlockOverlay.mc.thePlayer.lastTickPosX) * partialTicks;
                final double d2 = BlockOverlay.mc.thePlayer.lastTickPosY + (BlockOverlay.mc.thePlayer.posY - BlockOverlay.mc.thePlayer.lastTickPosY) * partialTicks;
                final double d3 = BlockOverlay.mc.thePlayer.lastTickPosZ + (BlockOverlay.mc.thePlayer.posZ - BlockOverlay.mc.thePlayer.lastTickPosZ) * partialTicks;
                final AxisAlignedBB axisAlignedBB = block.getSelectedBoundingBox((World)BlockOverlay.mc.theWorld, blockPos).expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(-d0, -d2, -d3);
                RenderGlobal.drawSelectionBoundingBox(axisAlignedBB);
                RenderUtils.drawFilledBox(axisAlignedBB);
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.resetColor();
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (BlockOverlay.mc.objectMouseOver != null && BlockOverlay.mc.objectMouseOver.getBlockPos() != null && BlockOverlay.mc.theWorld.getBlockState(BlockOverlay.mc.objectMouseOver.getBlockPos()).getBlock() != null && BlockUtils.canBeClicked(BlockOverlay.mc.objectMouseOver.getBlockPos()) && this.infoValue.asBoolean()) {
            final BlockPos blockPos = BlockOverlay.mc.objectMouseOver.getBlockPos();
            final Block block = BlockOverlay.mc.theWorld.getBlockState(blockPos).getBlock();
            if (BlockOverlay.mc.theWorld.getWorldBorder().contains(blockPos)) {
                final String info = block.getLocalizedName() + " §7ID: " + Block.getIdFromBlock(block);
                final ScaledResolution scaledResolution = new ScaledResolution(BlockOverlay.mc);
                RenderUtils.drawBorderedRect((float)(scaledResolution.getScaledWidth() / 2 - 2), (float)(scaledResolution.getScaledHeight() / 2 + 5), (float)(scaledResolution.getScaledWidth() / 2 + Fonts.font40.getStringWidth(info) + 2), (float)(scaledResolution.getScaledHeight() / 2 + 16), 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
                GlStateManager.resetColor();
                Fonts.font40.drawString(info, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 + 7, Color.WHITE.getRGB());
            }
        }
    }
}
