//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockLiquid;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "LiquidWalk", description = "Allows you to walk on water.", category = ModuleCategory.MOVEMENT, keyBind = 36)
public class LiquidWalk extends Module
{
    private final ListValue modeValue;
    private boolean nextTick;
    
    public LiquidWalk() {
        this.modeValue = new ListValue("Mode", new String[] { "NCP", "AAC", "AAC3.3.11", "Spartan", "Dolphin" }, "NCP");
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("liquidwalk", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".liquidwalk <mode>");
                    return;
                }
                if (args.length > 2 && LiquidWalk.this.modeValue.contains(args[2])) {
                    LiquidWalk.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7LiquidWalk mode was set to §8" + LiquidWalk.this.modeValue.asString().toUpperCase() + "§7.");
                    LiquidWalk$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".liquidwalk mode §c<§8" + Strings.join(LiquidWalk.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (LiquidWalk.mc.thePlayer == null || LiquidWalk.mc.thePlayer.isSneaking()) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "ncp": {
                if (BlockUtils.collideBlock(LiquidWalk.mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) && LiquidWalk.mc.thePlayer.isInsideOfMaterial(Material.air) && !LiquidWalk.mc.thePlayer.isSneaking()) {
                    LiquidWalk.mc.thePlayer.motionY = 0.08;
                    break;
                }
                break;
            }
            case "aac": {
                final BlockPos blockPos = LiquidWalk.mc.thePlayer.getPosition().down();
                if ((!LiquidWalk.mc.thePlayer.onGround && BlockUtils.getBlock(blockPos) == Blocks.water) || LiquidWalk.mc.thePlayer.isInWater()) {
                    if (!LiquidWalk.mc.thePlayer.isSprinting()) {
                        final EntityPlayerSP thePlayer = LiquidWalk.mc.thePlayer;
                        thePlayer.motionX *= 0.99999;
                        final EntityPlayerSP thePlayer2 = LiquidWalk.mc.thePlayer;
                        thePlayer2.motionY *= 0.0;
                        final EntityPlayerSP thePlayer3 = LiquidWalk.mc.thePlayer;
                        thePlayer3.motionZ *= 0.99999;
                        if (LiquidWalk.mc.thePlayer.isCollidedHorizontally) {
                            LiquidWalk.mc.thePlayer.motionY = (int)(LiquidWalk.mc.thePlayer.posY - (int)(LiquidWalk.mc.thePlayer.posY - 1.0)) / 8.0f;
                        }
                    }
                    else {
                        final EntityPlayerSP thePlayer4 = LiquidWalk.mc.thePlayer;
                        thePlayer4.motionX *= 0.99999;
                        final EntityPlayerSP thePlayer5 = LiquidWalk.mc.thePlayer;
                        thePlayer5.motionY *= 0.0;
                        final EntityPlayerSP thePlayer6 = LiquidWalk.mc.thePlayer;
                        thePlayer6.motionZ *= 0.99999;
                        if (LiquidWalk.mc.thePlayer.isCollidedHorizontally) {
                            LiquidWalk.mc.thePlayer.motionY = (int)(LiquidWalk.mc.thePlayer.posY - (int)(LiquidWalk.mc.thePlayer.posY - 1.0)) / 8.0f;
                        }
                    }
                    if (LiquidWalk.mc.thePlayer.fallDistance >= 4.0f) {
                        LiquidWalk.mc.thePlayer.motionY = -0.004;
                    }
                    else if (LiquidWalk.mc.thePlayer.isInWater()) {
                        LiquidWalk.mc.thePlayer.motionY = 0.09;
                    }
                }
                if (LiquidWalk.mc.thePlayer.hurtTime != 0) {
                    LiquidWalk.mc.thePlayer.onGround = false;
                    break;
                }
                break;
            }
            case "spartan": {
                if (!LiquidWalk.mc.thePlayer.isInWater()) {
                    break;
                }
                if (LiquidWalk.mc.thePlayer.isCollidedHorizontally) {
                    final EntityPlayerSP thePlayer7 = LiquidWalk.mc.thePlayer;
                    thePlayer7.motionY += 0.15;
                    return;
                }
                final Block block2 = BlockUtils.getBlock(new BlockPos(LiquidWalk.mc.thePlayer.posX, LiquidWalk.mc.thePlayer.posY + 1.0, LiquidWalk.mc.thePlayer.posZ));
                final Block blockUp = BlockUtils.getBlock(new BlockPos(LiquidWalk.mc.thePlayer.posX, LiquidWalk.mc.thePlayer.posY + 1.1, LiquidWalk.mc.thePlayer.posZ));
                if (blockUp instanceof BlockLiquid) {
                    LiquidWalk.mc.thePlayer.motionY = 0.1;
                }
                else if (block2 instanceof BlockLiquid) {
                    LiquidWalk.mc.thePlayer.motionY = 0.0;
                }
                LiquidWalk.mc.thePlayer.onGround = true;
                final EntityPlayerSP thePlayer8 = LiquidWalk.mc.thePlayer;
                thePlayer8.motionX *= 1.085;
                final EntityPlayerSP thePlayer9 = LiquidWalk.mc.thePlayer;
                thePlayer9.motionZ *= 1.085;
                break;
            }
            case "dolphin": {
                if (LiquidWalk.mc.thePlayer.isInWater()) {
                    final EntityPlayerSP thePlayer10 = LiquidWalk.mc.thePlayer;
                    thePlayer10.motionY += 0.03999999910593033;
                    break;
                }
                break;
            }
            case "aac3.3.11": {
                if (!LiquidWalk.mc.thePlayer.isInWater()) {
                    break;
                }
                final EntityPlayerSP thePlayer11 = LiquidWalk.mc.thePlayer;
                thePlayer11.motionX *= 1.17;
                final EntityPlayerSP thePlayer12 = LiquidWalk.mc.thePlayer;
                thePlayer12.motionZ *= 1.17;
                if (LiquidWalk.mc.thePlayer.isCollidedHorizontally) {
                    LiquidWalk.mc.thePlayer.motionY = 0.24;
                    break;
                }
                if (LiquidWalk.mc.theWorld.getBlockState(new BlockPos(LiquidWalk.mc.thePlayer.posX, LiquidWalk.mc.thePlayer.posY + 1.0, LiquidWalk.mc.thePlayer.posZ)).getBlock() != Blocks.air) {
                    final EntityPlayerSP thePlayer13 = LiquidWalk.mc.thePlayer;
                    thePlayer13.motionY += 0.04;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if (LiquidWalk.mc.thePlayer == null || LiquidWalk.mc.thePlayer.getEntityBoundingBox() == null) {
            return;
        }
        if (event.getBlock() instanceof BlockLiquid && !BlockUtils.collideBlock(LiquidWalk.mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) && !LiquidWalk.mc.thePlayer.isSneaking()) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "ncp": {
                    event.setBoundingBox(AxisAlignedBB.fromBounds((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), (double)(event.getY() + 1), (double)(event.getZ() + 1)));
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (LiquidWalk.mc.thePlayer == null || !this.modeValue.asString().equalsIgnoreCase("NCP")) {
            return;
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)event.getPacket();
            if (BlockUtils.collideBlock(new AxisAlignedBB(LiquidWalk.mc.thePlayer.getEntityBoundingBox().maxX, LiquidWalk.mc.thePlayer.getEntityBoundingBox().maxY, LiquidWalk.mc.thePlayer.getEntityBoundingBox().maxZ, LiquidWalk.mc.thePlayer.getEntityBoundingBox().minX, LiquidWalk.mc.thePlayer.getEntityBoundingBox().minY - 0.01, LiquidWalk.mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid)) {
                this.nextTick = !this.nextTick;
                if (this.nextTick) {
                    final C03PacketPlayer c03PacketPlayer = packetPlayer;
                    c03PacketPlayer.y -= 0.001;
                }
            }
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
