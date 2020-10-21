
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "SuperKnockback", description = "Increases knockback dealt to other entities.", category = ModuleCategory.COMBAT)
public class SuperKnockback extends Module
{
    @EventTarget
    public void onAttack(final AttackEvent event) {
        if (SuperKnockback.mc.thePlayer.isSprinting()) {
            SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
        }
        SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
        SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        SuperKnockback.mc.thePlayer.setSprinting(true);
        SuperKnockback.mc.thePlayer.serverSprintState = true;
    }
}
