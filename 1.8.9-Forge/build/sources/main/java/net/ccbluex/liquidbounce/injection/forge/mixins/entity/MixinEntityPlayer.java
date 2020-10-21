//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.minecraft.util.FoodStats;
import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerCapabilities;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends MixinEntityLivingBase
{
    @Shadow
    protected int flyToggleTimer;
    @Shadow
    public PlayerCapabilities capabilities;
    
    @Shadow
    @Override
    public abstract ItemStack getHeldItem();
    
    @Shadow
    public abstract GameProfile getGameProfile();
    
    @Shadow
    protected abstract boolean canTriggerWalking();
    
    @Shadow
    protected abstract String getSwimSound();
    
    @Shadow
    public abstract FoodStats getFoodStats();
    
    @Shadow
    public abstract int getItemInUseDuration();
    
    @Shadow
    public abstract ItemStack getItemInUse();
    
    @Shadow
    public abstract boolean isUsingItem();
}
