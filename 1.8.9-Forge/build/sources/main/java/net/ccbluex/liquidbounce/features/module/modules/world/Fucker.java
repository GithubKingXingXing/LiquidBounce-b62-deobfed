//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.minecraft.util.MovingObjectPosition;
import java.util.function.Function;
import java.util.Map;
import java.util.Comparator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.BlockValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Fucker", description = "Destroys selected blocks around you. (aka.  IDNuker)", category = ModuleCategory.WORLD)
public class Fucker extends Module
{
    private final BlockValue blockValue;
    private final BoolValue instantValue;
    private final ListValue throughWallsValue;
    private final BoolValue swingValue;
    private final BoolValue rotationsValue;
    private final BoolValue surroundingsValue;
    private final ListValue actionValue;
    private final BoolValue noHitValue;
    private BlockPos pos;
    private BlockPos oldPos;
    static float currentDamage;
    private int blockHitDelay;
    private final MSTimer switchTimer;
    
    public Fucker() {
        this.blockValue = new BlockValue("id", 26);
        this.instantValue = new BoolValue("Instant", false);
        this.throughWallsValue = new ListValue("ThroughWalls", new String[] { "None", "Raycast", "Around" }, "None");
        this.swingValue = new BoolValue("Swing", true);
        this.rotationsValue = new BoolValue("Rotations", true);
        this.surroundingsValue = new BoolValue("Surroundings", true);
        this.actionValue = new ListValue("Action", new String[] { "Destroy", "Use" }, "Destroy");
        this.noHitValue = new BoolValue("NoHit", false);
        this.switchTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("fucker", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("select")) {
                        if (args.length > 2) {
                            int id;
                            try {
                                id = Integer.parseInt(args[2]);
                            }
                            catch (NumberFormatException exception) {
                                id = Block.getIdFromBlock(Block.getBlockFromName(args[2]));
                            }
                            if (id != 0) {
                                Fucker.this.blockValue.setValue(id);
                                this.chat("§aThe block was set to " + BlockUtils.getBlockName(id) + ".");
                                Fucker$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            }
                            else {
                                this.chat("Block was not found or the block is air.");
                            }
                            return;
                        }
                        this.chat("§7Current: §8" + BlockUtils.getBlockName(Fucker.this.blockValue.asInteger()));
                        this.chatSyntax(".fucker select <name/id>");
                        return;
                    }
                    else {
                        if (args[1].equalsIgnoreCase("instant")) {
                            Fucker.this.instantValue.setValue(!Fucker.this.instantValue.asBoolean());
                            this.chat("§7Fucker instant was toggled §8" + (Fucker.this.instantValue.asBoolean() ? "on" : "off") + "§7.");
                            Fucker$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            return;
                        }
                        if (args[1].equalsIgnoreCase("throughwalls")) {
                            if (args.length > 2 && Fucker.this.throughWallsValue.contains(args[2])) {
                                Fucker.this.throughWallsValue.setValue(args[2].toLowerCase());
                                this.chat("§7Fucker throughwalls was set to §8" + Fucker.this.throughWallsValue.asString().toUpperCase() + "§7.");
                                Fucker$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                            this.chatSyntax(".fucker throughwalls §c<§8" + Strings.join(Fucker.this.throughWallsValue.getValues(), "§7, §8") + "§c>");
                            return;
                        }
                        else {
                            if (args[1].equalsIgnoreCase("Surroundings")) {
                                Fucker.this.surroundingsValue.setValue(!Fucker.this.surroundingsValue.asBoolean());
                                this.chat("§7Fucker Surroundings was toggled §8" + (Fucker.this.surroundingsValue.asBoolean() ? "on" : "off") + "§7.");
                                Fucker$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                            if (args[1].equalsIgnoreCase("nohit")) {
                                Fucker.this.noHitValue.setValue(!Fucker.this.noHitValue.asBoolean());
                                this.chat("§7Fucker NoHit was toggled §8" + (Fucker.this.noHitValue.asBoolean() ? "on" : "off") + "§7.");
                                Fucker$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                                return;
                            }
                        }
                    }
                }
                this.chatSyntax(".fucker <select/instant/throughwalls/surroundings/nohit>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.noHitValue.asBoolean()) {
            final KillAura killAura = (KillAura)ModuleManager.getModule(KillAura.class);
            if (killAura.getState() && killAura.target != null) {
                return;
            }
        }
        final int targetId = this.blockValue.asInteger();
        if (this.pos == null || Block.getIdFromBlock(BlockUtils.getBlock(this.pos)) != targetId || Fucker.mc.thePlayer.getDistanceSq(this.pos) >= 22.399999618530273) {
            this.pos = this.find(targetId);
        }
        boolean surroundings = false;
        if (this.pos != null && this.surroundingsValue.asBoolean()) {
            final double diffX = this.pos.getX() + 0.5 - Fucker.mc.thePlayer.posX;
            final double diffY = this.pos.getY() + 0.5 - (Fucker.mc.thePlayer.getEntityBoundingBox().minY + Fucker.mc.thePlayer.getEyeHeight());
            final double diffZ = this.pos.getZ() + 0.5 - Fucker.mc.thePlayer.posZ;
            final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
            final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
            final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
            final float f4 = MathHelper.sin(-pitch * 0.017453292f);
            final Vec3 vec31 = new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
            final Vec3 vec32 = Fucker.mc.thePlayer.getPositionEyes(1.0f);
            final Vec3 vec33 = vec32.addVector(vec31.xCoord * Fucker.mc.playerController.getBlockReachDistance(), vec31.yCoord * Fucker.mc.playerController.getBlockReachDistance(), vec31.zCoord * Fucker.mc.playerController.getBlockReachDistance());
            final BlockPos blockPos = Fucker.mc.theWorld.rayTraceBlocks(vec32, vec33, false, false, true).getBlockPos();
            if (blockPos != null) {
                if (this.pos.getX() != blockPos.getX() || this.pos.getY() != blockPos.getY() || this.pos.getZ() != blockPos.getZ()) {
                    surroundings = true;
                }
                this.pos = blockPos;
            }
        }
        if (this.pos == null) {
            Fucker.currentDamage = 0.0f;
            return;
        }
        if (this.oldPos != null && !this.oldPos.equals((Object)this.pos)) {
            this.switchTimer.reset();
        }
        this.oldPos = this.pos;
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return;
        }
        if (!this.switchTimer.hasTimePassed(250L)) {
            return;
        }
        if (this.rotationsValue.asBoolean()) {
            RotationUtils.faceBlockPacket(this.pos);
        }
        final String s = surroundings ? "destroy" : this.actionValue.asString().toLowerCase();
        switch (s) {
            case "destroy": {
                final AutoTool autoTool = (AutoTool)ModuleManager.getModule(AutoTool.class);
                if (autoTool != null && autoTool.getState()) {
                    autoTool.switchSlot(this.pos);
                }
                if (this.instantValue.asBoolean()) {
                    Fucker.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    Fucker.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    Fucker.currentDamage = 0.0f;
                    this.pos = null;
                    break;
                }
                if (Fucker.currentDamage == 0.0f) {
                    Fucker.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    if (Fucker.mc.thePlayer.capabilities.isCreativeMode || BlockUtils.getBlock(this.pos).getPlayerRelativeBlockHardness((EntityPlayer)Fucker.mc.thePlayer, (World)Fucker.mc.theWorld, this.pos) >= 1.0f) {
                        Fucker.currentDamage = 0.0f;
                        if (this.swingValue.asBoolean()) {
                            Fucker.mc.thePlayer.swingItem();
                        }
                        Fucker.mc.playerController.onPlayerDestroyBlock(this.pos, EnumFacing.DOWN);
                        this.pos = null;
                        return;
                    }
                }
                if (this.swingValue.asBoolean()) {
                    Fucker.mc.thePlayer.swingItem();
                }
                Fucker.currentDamage += BlockUtils.getBlock(this.pos).getPlayerRelativeBlockHardness((EntityPlayer)Fucker.mc.thePlayer, (World)Fucker.mc.theWorld, this.pos);
                Fucker.mc.theWorld.sendBlockBreakProgress(Fucker.mc.thePlayer.getEntityId(), this.pos, (int)(Fucker.currentDamage * 10.0f) - 1);
                if (Fucker.currentDamage >= 1.0f) {
                    Fucker.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, EnumFacing.DOWN));
                    Fucker.mc.playerController.onPlayerDestroyBlock(this.pos, EnumFacing.DOWN);
                    this.blockHitDelay = 4;
                    Fucker.currentDamage = 0.0f;
                    this.pos = null;
                    break;
                }
                break;
            }
            case "use": {
                if (Fucker.mc.playerController.onPlayerRightClick(Fucker.mc.thePlayer, Fucker.mc.theWorld, Fucker.mc.thePlayer.getHeldItem(), this.pos, EnumFacing.DOWN, new Vec3((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ()))) {
                    if (this.swingValue.asBoolean()) {
                        Fucker.mc.thePlayer.swingItem();
                    }
                    this.blockHitDelay = 4;
                    Fucker.currentDamage = 0.0f;
                    this.pos = null;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (this.pos != null) {
            RenderUtils.drawBlockBox(this.pos, Color.RED, true);
        }
    }
    
    private BlockPos find(final int targetID) {
        return BlockUtils.searchBlocks(4).entrySet().stream().filter(entry -> Block.getIdFromBlock((Block)entry.getValue()) == targetID && Fucker.mc.thePlayer.getDistanceSq((BlockPos)entry.getKey()) < 22.3 && (this.isHitable((BlockPos)entry.getKey()) || this.surroundingsValue.asBoolean())).min(Comparator.comparingDouble(value -> BlockUtils.getCenterDistance(value.getKey()))).map((Function<? super Object, ? extends BlockPos>)Map.Entry::getKey).orElse(null);
    }
    
    @Override
    public String getTag() {
        return BlockUtils.getBlockName(this.blockValue.asInteger());
    }
    
    private boolean isHitable(final BlockPos blockPos) {
        final String lowerCase = this.throughWallsValue.asString().toLowerCase();
        switch (lowerCase) {
            case "none": {
                return true;
            }
            case "raycast": {
                final Vec3 eyesPos = RotationUtils.getEyesPos();
                final MovingObjectPosition movingObjectPosition = Fucker.mc.theWorld.rayTraceBlocks(eyesPos, new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), false, true, false);
                return movingObjectPosition != null && movingObjectPosition.getBlockPos().equals((Object)blockPos);
            }
            case "around": {
                return !BlockUtils.isFullBlock(blockPos.down()) || !BlockUtils.isFullBlock(blockPos.up()) || !BlockUtils.isFullBlock(blockPos.north()) || !BlockUtils.isFullBlock(blockPos.east()) || !BlockUtils.isFullBlock(blockPos.south()) || !BlockUtils.isFullBlock(blockPos.west());
            }
            default: {
                return true;
            }
        }
    }
}
