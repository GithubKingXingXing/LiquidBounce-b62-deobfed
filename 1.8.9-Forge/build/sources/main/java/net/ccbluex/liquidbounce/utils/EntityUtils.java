//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockSlab;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.ArrayList;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityAnimal;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import java.util.Objects;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class EntityUtils
{
    private static final Minecraft mc;
    public static boolean targetInvisible;
    public static boolean targetPlayer;
    public static boolean targetMobs;
    public static boolean targetAnimals;
    public static boolean targetDead;
    
    public static boolean isSelected(final Entity entity, final boolean canAttackCheck) {
        if (!(entity instanceof EntityLivingBase) || (!EntityUtils.targetDead && !entity.isEntityAlive()) || entity == EntityUtils.mc.thePlayer || (!EntityUtils.targetInvisible && entity.isInvisible())) {
            return false;
        }
        if (!EntityUtils.targetPlayer || !(entity instanceof EntityPlayer)) {
            return (EntityUtils.targetMobs && isMob(entity)) || (EntityUtils.targetAnimals && isAnimal(entity));
        }
        final EntityPlayer entityPlayer = (EntityPlayer)entity;
        if (!canAttackCheck) {
            return true;
        }
        if (AntiBot.isBot((EntityLivingBase)entityPlayer)) {
            return false;
        }
        if (isFriend((Entity)entityPlayer) && !Objects.requireNonNull(ModuleManager.getModule(NoFriends.class)).getState()) {
            return false;
        }
        if (entityPlayer.isSpectator()) {
            return false;
        }
        final Teams teams = (Teams)ModuleManager.getModule(Teams.class);
        return !teams.getState() || !teams.isInYourTeam((EntityLivingBase)entityPlayer);
    }
    
    public static boolean isFriend(final Entity entity) {
        return entity instanceof EntityPlayer && entity.getName() != null && LiquidBounce.CLIENT.fileManager.friendsConfig.isFriend(ChatColor.stripColor(entity.getName()));
    }
    
    public static boolean isAnimal(final Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem || entity instanceof EntityBat;
    }
    
    public static boolean isMob(final Entity entity) {
        return entity instanceof EntityMob || entity instanceof EntityVillager || entity instanceof EntitySlime || entity instanceof EntityGhast || entity instanceof EntityDragon;
    }
    
    public static EntityLivingBase getEntity(final boolean see, final PriorityMode priorityMode) {
        if (priorityMode == null) {
            return null;
        }
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity : EntityUtils.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && isSelected(entity, true) && (!see || EntityUtils.mc.thePlayer.canEntityBeSeen(entity))) {
                targets.add((EntityLivingBase)entity);
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        switch (priorityMode) {
            case DISTANCE: {
                targets.sort(Comparator.comparingDouble(value -> EntityUtils.mc.thePlayer.getDistanceToEntity(value)));
                break;
            }
            case DIRECTION: {
                targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)RotationUtils::getRotationDifference));
                break;
            }
            case HEALTH: {
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
        }
        return targets.get(0);
    }
    
    public static String getName(final NetworkPlayerInfo networkPlayerInfoIn) {
        return (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }
    
    public static boolean isNearBlock() {
        final EntityPlayerSP thePlayer = EntityUtils.mc.thePlayer;
        final WorldClient theWorld = EntityUtils.mc.theWorld;
        final List<BlockPos> blocks = new ArrayList<BlockPos>();
        blocks.add(new BlockPos(thePlayer.posX, thePlayer.posY + 1.0, thePlayer.posZ - 0.7));
        blocks.add(new BlockPos(thePlayer.posX + 0.7, thePlayer.posY + 1.0, thePlayer.posZ));
        blocks.add(new BlockPos(thePlayer.posX, thePlayer.posY + 1.0, thePlayer.posZ + 0.7));
        blocks.add(new BlockPos(thePlayer.posX - 0.7, thePlayer.posY + 1.0, thePlayer.posZ));
        for (final BlockPos blockPos : blocks) {
            if ((theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMaxY() == theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMinY() + 1.0 && !theWorld.getBlockState(blockPos).getBlock().isTranslucent() && theWorld.getBlockState(blockPos).getBlock() != Blocks.water && !(theWorld.getBlockState(blockPos).getBlock() instanceof BlockSlab)) || theWorld.getBlockState(blockPos).getBlock() == Blocks.barrier) {
                return true;
            }
        }
        return false;
    }
    
    public static int getPing(final EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return 0;
        }
        final NetworkPlayerInfo networkPlayerInfo = EntityUtils.mc.getNetHandler().getPlayerInfo(entityPlayer.getUniqueID());
        return (networkPlayerInfo == null) ? 0 : networkPlayerInfo.getResponseTime();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        EntityUtils.targetInvisible = false;
        EntityUtils.targetPlayer = true;
        EntityUtils.targetMobs = true;
        EntityUtils.targetAnimals = false;
        EntityUtils.targetDead = false;
    }
    
    public enum PriorityMode
    {
        DISTANCE("Distance"), 
        DIRECTION("Direction"), 
        HEALTH("Health");
        
        private final String name;
        
        private PriorityMode(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static PriorityMode fromString(final String name) {
            for (final PriorityMode priorityMode : values()) {
                if (priorityMode.getName().equalsIgnoreCase(name)) {
                    return priorityMode;
                }
            }
            return null;
        }
    }
}
