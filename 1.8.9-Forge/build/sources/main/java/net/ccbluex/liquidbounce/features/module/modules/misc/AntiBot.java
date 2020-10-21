
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.server.S14PacketEntity;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AntiBot", description = "Prevents KillAura from attacking AntiCheat bots.", category = ModuleCategory.MISC)
public class AntiBot extends Module
{
    private final BoolValue tabValue;
    private final ListValue tabModeValue;
    private final BoolValue entityIDValue;
    private final BoolValue colorValue;
    private final BoolValue livingTimeValue;
    private final BoolValue groundValue;
    private final BoolValue airValue;
    private final BoolValue invaildGroundValue;
    private final BoolValue swingValue;
    private final BoolValue healthValue;
    private final BoolValue derpValue;
    private final BoolValue wasInvisibleValue;
    private final BoolValue armorValue;
    private final BoolValue pingValue;
    private final BoolValue needHitValue;
    private final List<Integer> ground;
    private final List<Integer> air;
    private final Map<Integer, Integer> invaildGround;
    private final List<Integer> swing;
    private final List<Integer> invisible;
    private final List<Integer> hitted;
    
    public AntiBot() {
        this.tabValue = new BoolValue("Tab", true);
        this.tabModeValue = new ListValue("TabMode", new String[] { "Equals", "Contains" }, "Contains");
        this.entityIDValue = new BoolValue("EntityID", true);
        this.colorValue = new BoolValue("Color", false);
        this.livingTimeValue = new BoolValue("LivingTime", false);
        this.groundValue = new BoolValue("Ground", true);
        this.airValue = new BoolValue("Air", false);
        this.invaildGroundValue = new BoolValue("InvaildGround", true);
        this.swingValue = new BoolValue("Swing", false);
        this.healthValue = new BoolValue("Health", false);
        this.derpValue = new BoolValue("Derp", true);
        this.wasInvisibleValue = new BoolValue("WasInvisible", false);
        this.armorValue = new BoolValue("Armor", false);
        this.pingValue = new BoolValue("Ping", false);
        this.needHitValue = new BoolValue("NeedHit", false);
        this.ground = new ArrayList<Integer>();
        this.air = new ArrayList<Integer>();
        this.invaildGround = new HashMap<Integer, Integer>();
        this.swing = new ArrayList<Integer>();
        this.invisible = new ArrayList<Integer>();
        this.hitted = new ArrayList<Integer>();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("antibot", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("color")) {
                        AntiBot.this.colorValue.setValue(!AntiBot.this.colorValue.asBoolean());
                        this.chat("§7AntiBot color was toggled §8" + (AntiBot.this.colorValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("tab")) {
                        AntiBot.this.tabValue.setValue(!AntiBot.this.tabValue.asBoolean());
                        this.chat("§7AntiBot tab was toggled §8" + (AntiBot.this.tabValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("livingtime")) {
                        AntiBot.this.livingTimeValue.setValue(!AntiBot.this.livingTimeValue.asBoolean());
                        this.chat("§7AntiBot livingtime was toggled §8" + (AntiBot.this.livingTimeValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("ground")) {
                        AntiBot.this.groundValue.setValue(!AntiBot.this.groundValue.asBoolean());
                        this.chat("§7AntiBot ground was toggled §8" + (AntiBot.this.groundValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("entityid")) {
                        AntiBot.this.entityIDValue.setValue(!AntiBot.this.entityIDValue.asBoolean());
                        this.chat("§7AntiBot entityid was toggled §8" + (AntiBot.this.entityIDValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("swing")) {
                        AntiBot.this.swingValue.setValue(!AntiBot.this.swingValue.asBoolean());
                        this.chat("§7AntiBot swing was toggled §8" + (AntiBot.this.swingValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("health")) {
                        AntiBot.this.healthValue.setValue(!AntiBot.this.healthValue.asBoolean());
                        this.chat("§7AntiBot health was toggled §8" + (AntiBot.this.healthValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("derp")) {
                        AntiBot.this.derpValue.setValue(!AntiBot.this.derpValue.asBoolean());
                        this.chat("§7AntiBot derp was toggled §8" + (AntiBot.this.derpValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("WasInvisible")) {
                        AntiBot.this.wasInvisibleValue.setValue(!AntiBot.this.wasInvisibleValue.asBoolean());
                        this.chat("§7AntiBot WasInvisible was toggled §8" + (AntiBot.this.wasInvisibleValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("Armor")) {
                        AntiBot.this.armorValue.setValue(!AntiBot.this.armorValue.asBoolean());
                        this.chat("§7AntiBot Armor was toggled §8" + (AntiBot.this.armorValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("Ping")) {
                        AntiBot.this.pingValue.setValue(!AntiBot.this.pingValue.asBoolean());
                        this.chat("§7AntiBot Ping was toggled §8" + (AntiBot.this.pingValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                    if (args[1].equalsIgnoreCase("NeedHit")) {
                        AntiBot.this.needHitValue.setValue(!AntiBot.this.needHitValue.asBoolean());
                        this.chat("§7AntiBot needhit was toggled §8" + (AntiBot.this.needHitValue.asBoolean() ? "on" : "off") + "§7.");
                        AntiBot$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                        return;
                    }
                }
                this.chatSyntax(".antibot <color, tab, livingtime, ground, entityid, swing, health, derp, wasinvisible, armor, ping, needhit>");
            }
        });
    }
    
    @Override
    public void onDisable() {
        this.hitted.clear();
        this.swing.clear();
        this.ground.clear();
        this.invaildGround.clear();
        this.invisible.clear();
        super.onDisable();
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (AntiBot.mc.thePlayer == null || AntiBot.mc.theWorld == null) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof S14PacketEntity) {
            final S14PacketEntity packetEntity = (S14PacketEntity)event.getPacket();
            final Entity entity = packetEntity.getEntity((World)AntiBot.mc.theWorld);
            if (entity instanceof EntityPlayer) {
                if (packetEntity.getOnGround() && !this.ground.contains(entity.getEntityId())) {
                    this.ground.add(entity.getEntityId());
                }
                if (!packetEntity.getOnGround() && !this.air.contains(entity.getEntityId())) {
                    this.air.add(entity.getEntityId());
                }
                if (packetEntity.getOnGround()) {
                    if (entity.prevPosY != entity.posY) {
                        this.invaildGround.put(entity.getEntityId(), this.invaildGround.getOrDefault(entity.getEntityId(), 0) + 1);
                    }
                }
                else {
                    final int currentVL = this.invaildGround.getOrDefault(entity.getEntityId(), 0) / 2;
                    if (currentVL <= 0) {
                        this.invaildGround.remove(entity.getEntityId());
                    }
                    else {
                        this.invaildGround.put(entity.getEntityId(), currentVL);
                    }
                }
                if (entity.isInvisible() && !this.invisible.contains(entity.getEntityId())) {
                    this.invisible.add(entity.getEntityId());
                }
            }
        }
        if (packet instanceof S0BPacketAnimation) {
            final S0BPacketAnimation packetAnimation = (S0BPacketAnimation)event.getPacket();
            final Entity entity = AntiBot.mc.theWorld.getEntityByID(packetAnimation.getEntityID());
            if (entity instanceof EntityLivingBase && packetAnimation.getAnimationType() == 0 && !this.swing.contains(entity.getEntityId())) {
                this.swing.add(entity.getEntityId());
            }
        }
    }
    
    @EventTarget
    public void onAttack(final AttackEvent e) {
        final Entity entity = e.getTargetEntity();
        if (entity instanceof EntityLivingBase && !this.hitted.contains(entity.getEntityId())) {
            this.hitted.add(entity.getEntityId());
        }
    }
    
    public static boolean isBot(final EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        final AntiBot antiBot = (AntiBot)ModuleManager.getModule(AntiBot.class);
        if (antiBot == null || !antiBot.getState()) {
            return false;
        }
        if (antiBot.colorValue.asBoolean() && !entity.getDisplayName().getFormattedText().replace("§r", "").contains("§")) {
            return true;
        }
        if (antiBot.livingTimeValue.asBoolean() && entity.ticksExisted < 40) {
            return true;
        }
        if (antiBot.groundValue.asBoolean() && !antiBot.ground.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.airValue.asBoolean() && !antiBot.air.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.swingValue.asBoolean() && !antiBot.swing.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.healthValue.asBoolean() && entity.getHealth() > 20.0f) {
            return true;
        }
        if (antiBot.entityIDValue.asBoolean() && (entity.getEntityId() >= 1000000000 || entity.getEntityId() <= -1)) {
            return true;
        }
        if (antiBot.derpValue.asBoolean() && (entity.rotationPitch > 90.0f || entity.rotationPitch < -90.0f)) {
            return true;
        }
        if (antiBot.wasInvisibleValue.asBoolean() && antiBot.invisible.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.armorValue.asBoolean() && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.inventory.armorInventory[0] == null && player.inventory.armorInventory[1] == null && player.inventory.armorInventory[2] == null && player.inventory.armorInventory[3] == null) {
                return true;
            }
        }
        if (antiBot.pingValue.asBoolean() && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (AntiBot.mc.getNetHandler().getPlayerInfo(player.getUniqueID()).getResponseTime() == 0) {
                return true;
            }
        }
        if (antiBot.needHitValue.asBoolean() && !antiBot.hitted.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.invaildGroundValue.asBoolean() && antiBot.invaildGround.getOrDefault(entity.getEntityId(), 0) >= 10) {
            return true;
        }
        if (antiBot.tabValue.asBoolean()) {
            final boolean equals = antiBot.tabModeValue.asString().equalsIgnoreCase("Equals");
            final String targetName = ChatColor.stripColor(entity.getDisplayName().getFormattedText());
            for (final NetworkPlayerInfo networkPlayerInfo : AntiBot.mc.getNetHandler().getPlayerInfoMap()) {
                final String networkName = ChatColor.stripColor(EntityUtils.getName(networkPlayerInfo));
                if (equals) {
                    if (targetName.equals(networkName)) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (targetName.contains(networkName)) {
                        return false;
                    }
                    continue;
                }
            }
            return true;
        }
        return entity.getName().isEmpty() || entity.getName().equals(AntiBot.mc.thePlayer.getName());
    }
}
