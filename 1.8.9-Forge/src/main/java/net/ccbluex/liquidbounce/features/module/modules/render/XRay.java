
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.List;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "XRay", description = "Allows you to see ores through walls.", category = ModuleCategory.RENDER)
public class XRay extends Module
{
    public final List<Block> xrayBlocks;
    
    public XRay() {
        this.xrayBlocks = new ArrayList<Block>(Arrays.asList(Blocks.coal_ore, Blocks.iron_ore, Blocks.gold_ore, Blocks.redstone_ore, Blocks.lapis_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.quartz_ore, Blocks.clay, Blocks.glowstone, Blocks.crafting_table, Blocks.torch, Blocks.ladder, Blocks.tnt, Blocks.coal_block, Blocks.iron_block, Blocks.gold_block, Blocks.diamond_block, Blocks.emerald_block, Blocks.redstone_block, Blocks.lapis_block, (Block)Blocks.fire, Blocks.mossy_cobblestone, Blocks.mob_spawner, Blocks.end_portal_frame, Blocks.enchanting_table, Blocks.bookshelf, Blocks.command_block, (Block)Blocks.lava, (Block)Blocks.flowing_lava, (Block)Blocks.water, (Block)Blocks.flowing_water, Blocks.furnace, Blocks.lit_furnace));
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("xray", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length > 2) {
                            try {
                                final Block block2 = Block.getBlockById(Integer.parseInt(args[2]));
                                if (XRay.this.xrayBlocks.contains(block2)) {
                                    this.chat("The block is already in the list.");
                                    return;
                                }
                                XRay.this.xrayBlocks.add(block2);
                                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.xrayConfig);
                                this.chat("§7Added block §8" + block2.getLocalizedName() + "§7.");
                                XRay$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".xray add <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length > 2) {
                            try {
                                final Block block2 = Block.getBlockById(Integer.parseInt(args[2]));
                                if (!XRay.this.xrayBlocks.contains(block2)) {
                                    this.chat("The block is not in the list.");
                                    return;
                                }
                                XRay.this.xrayBlocks.remove(block2);
                                LiquidBounce.CLIENT.fileManager.saveConfig(LiquidBounce.CLIENT.fileManager.xrayConfig);
                                this.chat("§7Removed block §8" + block2.getLocalizedName() + "§7.");
                                XRay$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            catch (NumberFormatException exception) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax(".xray remove <value>");
                        return;
                    }
                    else if (args[1].equalsIgnoreCase("list")) {
                        this.chat("§8XRAY-Blocks:");
                        XRay.this.xrayBlocks.forEach(block -> this.chat("§8" + block.getLocalizedName() + " §7-§c " + Block.getIdFromBlock(block)));
                        return;
                    }
                }
                this.chatSyntax(".xray <add, remove, list>");
            }
        });
    }
    
    @Override
    public void onToggle(final boolean state) {
        XRay.mc.renderGlobal.loadRenderers();
    }
}
