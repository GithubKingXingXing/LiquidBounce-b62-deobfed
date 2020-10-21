
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.events.MotionEvent;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoWeapon", description = "Automatically selects the best weapon in your hotbar.", category = ModuleCategory.COMBAT)
public class AutoWeapon extends Module
{
    private final BoolValue silentValue;
    private C02PacketUseEntity packetUseEntity;
    private boolean spoofedSlot;
    private boolean gotIt;
    private int tick;
    
    public AutoWeapon() {
        this.silentValue = new BoolValue("SpoofItem", false);
    }
    
    @EventTarget
    public void onAttack(final AttackEvent event) {
        this.gotIt = true;
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && this.gotIt) {
            this.gotIt = false;
            int slot = -1;
            double bestDamage = 0.0;
            for (int i = 0; i < 9; ++i) {
                final ItemStack itemStack = AutoWeapon.mc.thePlayer.inventory.getStackInSlot(i);
                if (itemStack != null && (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool)) {
                    for (final AttributeModifier attributeModifier : itemStack.getAttributeModifiers().get((Object)"generic.attackDamage")) {
                        final double damage = attributeModifier.getAmount() + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.sharpness);
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            slot = i;
                        }
                    }
                }
            }
            if (slot != -1 && slot != AutoWeapon.mc.thePlayer.inventory.currentItem) {
                if (this.silentValue.asBoolean()) {
                    AutoWeapon.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(slot));
                    this.spoofedSlot = true;
                }
                else {
                    AutoWeapon.mc.thePlayer.inventory.currentItem = slot;
                    AutoWeapon.mc.playerController.updateController();
                }
                event.setCancelled(true);
                this.packetUseEntity = (C02PacketUseEntity)event.getPacket();
                this.tick = 0;
            }
        }
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final MotionEvent event) {
        if (this.tick < 1) {
            ++this.tick;
            return;
        }
        if (this.packetUseEntity != null) {
            AutoWeapon.mc.getNetHandler().getNetworkManager().sendPacket((Packet)this.packetUseEntity);
            if (this.spoofedSlot) {
                AutoWeapon.mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(AutoWeapon.mc.thePlayer.inventory.currentItem));
            }
            this.packetUseEntity = null;
        }
    }
}
