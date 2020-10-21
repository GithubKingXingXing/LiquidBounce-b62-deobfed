
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Tower", description = "Automatically builds a tower beneath you.", category = ModuleCategory.WORLD, keyBind = 24)
public class Tower extends Module
{
    private final ListValue modeValue;
    private final BoolValue autoBlockValue;
    private final BoolValue stayAutoBlock;
    private final BoolValue swingValue;
    private final BoolValue stopWhenBlockAbove;
    private final FloatValue timerValue;
    private final FloatValue jumpMotionValue;
    private final IntegerValue jumpDelayValue;
    private final FloatValue constantMotionValue;
    private final FloatValue constantMotionJumpGroundValue;
    private final FloatValue teleportHeightValue;
    private final IntegerValue teleportDelayValue;
    private final BoolValue teleportGroundValue;
    private final BoolValue teleportNoMotionValue;
    private final BoolValue counterDisplayValue;
    private PlaceInfo placeInfo;
    private final TickTimer packetTimer;
    private final TickTimer teleportTimer;
    private final TickTimer jumpTimer;
    private double jumpGround;
    private int slot;
    
    public Tower() {
        this.modeValue = new ListValue("Mode", new String[] { "Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9" }, "Motion");
        this.autoBlockValue = new BoolValue("AutoBlock", true);
        this.stayAutoBlock = new BoolValue("StayAutoBlock", false);
        this.swingValue = new BoolValue("Swing", true);
        this.stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
        this.timerValue = new FloatValue("Timer", 1.0f, 0.0f, 10.0f);
        this.jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f);
        this.jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);
        this.constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f);
        this.constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f);
        this.teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f);
        this.teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
        this.teleportGroundValue = new BoolValue("TeleportGround", true);
        this.teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);
        this.counterDisplayValue = new BoolValue("Counter", true);
        this.packetTimer = new TickTimer();
        this.teleportTimer = new TickTimer();
        this.jumpTimer = new TickTimer();
        this.jumpGround = 0.0;
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("tower", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("mode")) {
                        if (args.length > 2 && Tower.this.modeValue.contains(args[2])) {
                            Tower.this.modeValue.setValue(args[2].toLowerCase());
                            this.chat("§7Tower mode was set to §8" + Tower.this.modeValue.asString().toUpperCase() + "§7.");
                            Tower$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        this.chatSyntax(".tower mode §c<§8" + Strings.join(Tower.this.modeValue.getValues(), "§7, §8") + "§c>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("autoblock")) {
                            Tower.this.autoBlockValue.setValue(!Tower.this.autoBlockValue.asBoolean());
                            this.chat("§7Tower autoblock was toggled §8" + (Tower.this.autoBlockValue.asBoolean() ? "on" : "off") + "§7.");
                            Tower$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("swing")) {
                            Tower.this.swingValue.setValue(!Tower.this.swingValue.asBoolean());
                            this.chat("§7Tower swing was toggled §8" + (Tower.this.swingValue.asBoolean() ? "on" : "off") + "§7.");
                            Tower$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                    }
                }
                this.chatSyntax(".tower <mode, autoblock, swing>");
            }
        });
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        Tower.mc.timer.timerSpeed = this.timerValue.asFloat();
        switch (event.getEventState()) {
            case PRE: {
                this.packetTimer.update();
                this.teleportTimer.update();
                this.jumpTimer.update();
                if (this.autoBlockValue.asBoolean()) {
                    if (this.findBlock() == -1) {
                        break;
                    }
                }
                else if (Tower.mc.thePlayer.getHeldItem() == null || !(Tower.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
                    break;
                }
                if (!this.stopWhenBlockAbove.asBoolean() || BlockUtils.getBlock(new BlockPos(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY + 2.0, Tower.mc.thePlayer.posZ)) instanceof BlockAir) {
                    final String lowerCase = this.modeValue.asString().toLowerCase();
                    switch (lowerCase) {
                        case "jump": {
                            if (Tower.mc.thePlayer.onGround && this.jumpTimer.hasTimePassed(this.jumpDelayValue.asInteger())) {
                                Tower.mc.thePlayer.motionY = this.jumpMotionValue.asFloat();
                                this.jumpTimer.reset();
                                break;
                            }
                            break;
                        }
                        case "motion": {
                            if (Tower.mc.thePlayer.onGround) {
                                Tower.mc.thePlayer.motionY = 0.42;
                                break;
                            }
                            if (Tower.mc.thePlayer.motionY < 0.1) {
                                Tower.mc.thePlayer.motionY = -0.3;
                                break;
                            }
                            break;
                        }
                        case "motiontp": {
                            if (Tower.mc.thePlayer.onGround) {
                                Tower.mc.thePlayer.motionY = 0.42;
                                break;
                            }
                            if (Tower.mc.thePlayer.motionY < 0.23) {
                                Tower.mc.thePlayer.setPosition(Tower.mc.thePlayer.posX, (double)(int)Tower.mc.thePlayer.posY, Tower.mc.thePlayer.posZ);
                                break;
                            }
                            break;
                        }
                        case "packet": {
                            if (Tower.mc.thePlayer.onGround && this.packetTimer.hasTimePassed(2)) {
                                Tower.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY + 0.42, Tower.mc.thePlayer.posZ, false));
                                Tower.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY + 0.753, Tower.mc.thePlayer.posZ, false));
                                Tower.mc.thePlayer.setPosition(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY + 1.0, Tower.mc.thePlayer.posZ);
                                this.packetTimer.reset();
                                break;
                            }
                            break;
                        }
                        case "teleport": {
                            if (this.teleportNoMotionValue.asBoolean()) {
                                Tower.mc.thePlayer.motionY = 0.0;
                            }
                            if ((Tower.mc.thePlayer.onGround || !this.teleportGroundValue.asBoolean()) && this.teleportTimer.hasTimePassed(this.teleportDelayValue.asInteger())) {
                                Tower.mc.thePlayer.setPositionAndUpdate(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY + this.teleportHeightValue.asFloat(), Tower.mc.thePlayer.posZ);
                                this.teleportTimer.reset();
                                break;
                            }
                            break;
                        }
                        case "constantmotion": {
                            if (Tower.mc.thePlayer.onGround) {
                                this.jumpGround = Tower.mc.thePlayer.posY;
                                Tower.mc.thePlayer.motionY = this.constantMotionValue.asFloat();
                            }
                            if (Tower.mc.thePlayer.posY > this.jumpGround + this.constantMotionJumpGroundValue.asFloat()) {
                                Tower.mc.thePlayer.setPosition(Tower.mc.thePlayer.posX, (double)(int)Tower.mc.thePlayer.posY, Tower.mc.thePlayer.posZ);
                                Tower.mc.thePlayer.motionY = this.constantMotionValue.asFloat();
                                this.jumpGround = Tower.mc.thePlayer.posY;
                                break;
                            }
                            break;
                        }
                        case "aac3.3.9": {
                            if (Tower.mc.thePlayer.onGround) {
                                Tower.mc.thePlayer.motionY = 0.4001;
                            }
                            Tower.mc.timer.timerSpeed = 1.0f;
                            if (Tower.mc.thePlayer.motionY < 0.0) {
                                final EntityPlayerSP thePlayer = Tower.mc.thePlayer;
                                thePlayer.motionY -= 9.45E-6;
                                Tower.mc.timer.timerSpeed = 1.6f;
                                break;
                            }
                            break;
                        }
                    }
                }
                final BlockPos blockPos = new BlockPos(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY - 0.5, Tower.mc.thePlayer.posZ);
                if (Tower.mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air && (this.placeInfo = PlaceInfo.get(blockPos)) != null) {
                    RotationUtils.faceBlockPacket(blockPos);
                }
                break;
            }
            case POST: {
                if (this.placeInfo == null) {
                    return;
                }
                int blockSlot = Integer.MAX_VALUE;
                ItemStack itemStack = Tower.mc.thePlayer.getHeldItem();
                if (Tower.mc.thePlayer.getHeldItem() == null || !(Tower.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
                    if (!this.autoBlockValue.asBoolean()) {
                        return;
                    }
                    blockSlot = this.findBlock();
                    if (blockSlot == -1) {
                        return;
                    }
                    Tower.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                    itemStack = Tower.mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
                }
                if (Tower.mc.playerController.onPlayerRightClick(Tower.mc.thePlayer, Tower.mc.theWorld, itemStack, this.placeInfo.getBlockPos(), this.placeInfo.getEnumFacing(), this.placeInfo.getVec3())) {
                    if (this.swingValue.asBoolean()) {
                        Tower.mc.thePlayer.swingItem();
                    }
                    else {
                        Tower.mc.getNetHandler().addToSendQueue((Packet)new C0APacketAnimation());
                    }
                }
                this.placeInfo = null;
                if (!this.stayAutoBlock.asBoolean() && blockSlot != Integer.MAX_VALUE) {
                    Tower.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(Tower.mc.thePlayer.inventory.currentItem));
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (Tower.mc.thePlayer == null) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.getSlotId();
        }
    }
    
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (this.counterDisplayValue.asBoolean()) {
            GlStateManager.pushMatrix();
            final BlockOverlay blockOverlay = (BlockOverlay)ModuleManager.getModule(BlockOverlay.class);
            if (blockOverlay.getState() && blockOverlay.infoValue.asBoolean() && Tower.mc.objectMouseOver != null && Tower.mc.objectMouseOver.getBlockPos() != null && Tower.mc.theWorld.getBlockState(Tower.mc.objectMouseOver.getBlockPos()).getBlock() != null && BlockUtils.canBeClicked(Tower.mc.objectMouseOver.getBlockPos()) && Tower.mc.theWorld.getWorldBorder().contains(Tower.mc.objectMouseOver.getBlockPos())) {
                GlStateManager.translate(0.0f, 15.0f, 0.0f);
            }
            final String info = "Blocks: §7" + this.getBlocksAmount();
            final ScaledResolution scaledResolution = new ScaledResolution(Tower.mc);
            RenderUtils.drawBorderedRect((float)(scaledResolution.getScaledWidth() / 2 - 2), (float)(scaledResolution.getScaledHeight() / 2 + 5), (float)(scaledResolution.getScaledWidth() / 2 + Fonts.font40.getStringWidth(info) + 2), (float)(scaledResolution.getScaledHeight() / 2 + 16), 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.resetColor();
            Fonts.font40.drawString(info, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 + 7, Color.WHITE.getRGB());
            GlStateManager.popMatrix();
        }
    }
    
    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Tower.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                amount += itemStack.stackSize;
            }
        }
        return amount;
    }
    
    @Override
    public void onDisable() {
        if (Tower.mc.thePlayer == null) {
            return;
        }
        Tower.mc.timer.timerSpeed = 1.0f;
        if (this.slot != Tower.mc.thePlayer.inventory.currentItem) {
            Tower.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(Tower.mc.thePlayer.inventory.currentItem));
        }
    }
    
    private int findBlock() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Tower.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
