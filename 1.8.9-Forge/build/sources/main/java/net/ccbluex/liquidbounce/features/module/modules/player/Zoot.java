//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Zoot", description = "Removes all bad potion effects/fire.", category = ModuleCategory.PLAYER)
public class Zoot extends Module
{
    private final BoolValue badEffectsValue;
    private final BoolValue fireValue;
    private final BoolValue noAirValue;
    
    public Zoot() {
        this.badEffectsValue = new BoolValue("BadEffects", true);
        this.fireValue = new BoolValue("Fire", true);
        this.noAirValue = new BoolValue("NoAir", false);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.noAirValue.asBoolean() && !Zoot.mc.thePlayer.onGround) {
            return;
        }
        if (this.badEffectsValue.asBoolean()) {
            for (final PotionEffect potion : Zoot.mc.thePlayer.getActivePotionEffects()) {
                if (potion != null && (Zoot.mc.thePlayer.isPotionActive(Potion.hunger) || Zoot.mc.thePlayer.isPotionActive(Potion.moveSlowdown) || Zoot.mc.thePlayer.isPotionActive(Potion.digSlowdown) || Zoot.mc.thePlayer.isPotionActive(Potion.harm) || Zoot.mc.thePlayer.isPotionActive(Potion.confusion) || Zoot.mc.thePlayer.isPotionActive(Potion.blindness) || Zoot.mc.thePlayer.isPotionActive(Potion.weakness) || Zoot.mc.thePlayer.isPotionActive(Potion.wither) || Zoot.mc.thePlayer.isPotionActive(Potion.poison))) {
                    for (int i = 0; i < potion.getDuration() / 20; ++i) {
                        Zoot.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer());
                    }
                }
            }
        }
        if (this.fireValue.asBoolean() && !Zoot.mc.thePlayer.capabilities.isCreativeMode && Zoot.mc.thePlayer.isBurning()) {
            for (int j = 0; j < 10; ++j) {
                Zoot.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer());
            }
        }
    }
}
