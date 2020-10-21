//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemFood;
import net.minecraft.item.Item;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.minecraft.item.ItemSword;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoSlow", description = "Cancels slowness effects caused by soulsand and using items.", category = ModuleCategory.MOVEMENT)
public class NoSlow extends Module
{
    private final String[] modes;
    private final String defaultMode = "NCP";
    private final ListValue swordValue;
    private final ListValue consumeValue;
    private final ListValue bowValue;
    public final BoolValue soulsandValue;
    public final FloatValue customReducementValue;
    
    public NoSlow() {
        this.modes = new String[] { "Off", "Vanilla", "NCP", "AAC", "Spartan", "KillSwitch", "AntiAura", "NNC", "Custom" };
        this.swordValue = new ListValue("Sword", this.modes, "NCP");
        this.consumeValue = new ListValue("Consume", this.modes, "NCP");
        this.bowValue = new ListValue("Bow", this.modes, "NCP");
        this.soulsandValue = new BoolValue("Soulsand", true);
        this.customReducementValue = new FloatValue("CustomReducement", 0.6f, 0.0f, 1.0f);
    }
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (NoSlow.mc.thePlayer.getHeldItem() != null && NoSlow.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            final KillAura killAura = (KillAura)ModuleManager.getModule(KillAura.class);
            if (NoSlow.mc.thePlayer.isBlocking() || killAura.blocking) {
                final String mode = this.getMode(NoSlow.mc.thePlayer.getHeldItem().getItem());
                if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("NNC")) {
                    switch (event.getEventState()) {
                        case PRE: {
                            NoSlow.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            break;
                        }
                        case POST: {
                            NoSlow.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.inventory.getCurrentItem()));
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public String getMode(final Item item) {
        if (item instanceof ItemSword) {
            return this.swordValue.asString();
        }
        if (item instanceof ItemFood || item instanceof ItemPotion) {
            return this.consumeValue.asString();
        }
        if (item instanceof ItemBow) {
            return this.bowValue.asString();
        }
        return "OFF";
    }
}
