//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockLadder;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastClimb", description = "Allows you to climb up ladders and vines faster.", category = ModuleCategory.MOVEMENT)
public class FastClimb extends Module
{
    public final ListValue modeValue;
    private final FloatValue normalSpeedValue;
    
    public FastClimb() {
        this.modeValue = new ListValue("Mode", new String[] { "Normal", "InstantTP", "AAC", "AACv3", "OAAC", "LAAC" }, "Normal");
        this.normalSpeedValue = new FloatValue("NormalSpeed", 0.2872f, 0.01f, 5.0f);
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("fastclimb", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".fastclimb <mode>");
                    return;
                }
                if (args.length > 2 && FastClimb.this.modeValue.contains(args[2])) {
                    FastClimb.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7FastClimb mode was set to §8" + FastClimb.this.modeValue.asString().toUpperCase() + "§7.");
                    FastClimb$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".fastclimb mode §c<§8" + Strings.join(FastClimb.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        final String mode = this.modeValue.asString();
        if (mode.equalsIgnoreCase("Normal") && FastClimb.mc.thePlayer.isCollidedHorizontally && FastClimb.mc.thePlayer.isOnLadder()) {
            event.setY(this.normalSpeedValue.asFloat());
            FastClimb.mc.thePlayer.motionY = 0.0;
        }
        else if (mode.equalsIgnoreCase("AAC") && FastClimb.mc.thePlayer.isCollidedHorizontally) {
            final EnumFacing facing = FastClimb.mc.thePlayer.getHorizontalFacing();
            double x = 0.0;
            double z = 0.0;
            if (facing == EnumFacing.NORTH) {
                z = -0.99;
            }
            if (facing == EnumFacing.EAST) {
                x = 0.99;
            }
            if (facing == EnumFacing.SOUTH) {
                z = 0.99;
            }
            if (facing == EnumFacing.WEST) {
                x = -0.99;
            }
            final BlockPos blockPos = new BlockPos(FastClimb.mc.thePlayer.posX + x, FastClimb.mc.thePlayer.posY, FastClimb.mc.thePlayer.posZ + z);
            final Block block2 = BlockUtils.getBlock(blockPos);
            if (block2 instanceof BlockLadder || block2 instanceof BlockVine) {
                event.setY(0.5);
                FastClimb.mc.thePlayer.motionY = 0.0;
            }
        }
        else if (mode.equalsIgnoreCase("AACv3") && BlockUtils.collideBlockIntersects(FastClimb.mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLadder || block instanceof BlockVine) && FastClimb.mc.gameSettings.keyBindForward.isKeyDown()) {
            event.setY(0.5);
            event.setX(0.0);
            event.setZ(0.0);
            FastClimb.mc.thePlayer.motionY = 0.0;
            FastClimb.mc.thePlayer.motionX = 0.0;
            FastClimb.mc.thePlayer.motionZ = 0.0;
        }
        else if (mode.equalsIgnoreCase("OAAC") && FastClimb.mc.thePlayer.isCollidedHorizontally && FastClimb.mc.thePlayer.isOnLadder()) {
            event.setY(0.1649);
            FastClimb.mc.thePlayer.motionY = 0.0;
        }
        else if (mode.equalsIgnoreCase("LAAC") && FastClimb.mc.thePlayer.isCollidedHorizontally && FastClimb.mc.thePlayer.isOnLadder()) {
            event.setY(0.1699);
            FastClimb.mc.thePlayer.motionY = 0.0;
        }
        else if (mode.equalsIgnoreCase("InstantTP") && FastClimb.mc.thePlayer.isOnLadder() && FastClimb.mc.gameSettings.keyBindForward.isKeyDown()) {
            for (int i = (int)FastClimb.mc.thePlayer.posY; i < 256; ++i) {
                final Block block3 = BlockUtils.getBlock(new BlockPos(FastClimb.mc.thePlayer.posX, (double)i, FastClimb.mc.thePlayer.posZ));
                if (!(block3 instanceof BlockLadder)) {
                    final EnumFacing horizontalFacing = FastClimb.mc.thePlayer.getHorizontalFacing();
                    double x2 = 0.0;
                    double z2 = 0.0;
                    switch (horizontalFacing) {
                        case DOWN: {}
                        case NORTH: {
                            z2 = -1.0;
                            break;
                        }
                        case EAST: {
                            x2 = 1.0;
                            break;
                        }
                        case SOUTH: {
                            z2 = 1.0;
                            break;
                        }
                        case WEST: {
                            x2 = -1.0;
                            break;
                        }
                    }
                    FastClimb.mc.thePlayer.setPosition(FastClimb.mc.thePlayer.posX + x2, (double)i, FastClimb.mc.thePlayer.posZ + z2);
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if (FastClimb.mc.thePlayer != null && (event.getBlock() instanceof BlockLadder || event.getBlock() instanceof BlockVine) && this.modeValue.asString().equalsIgnoreCase("AACv3") && FastClimb.mc.thePlayer.isOnLadder()) {
            event.setBoundingBox(null);
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
