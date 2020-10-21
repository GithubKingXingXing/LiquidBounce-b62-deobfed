//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class GiveCommand extends Command
{
    public GiveCommand() {
        super("give", new String[] { "item", "i", "get" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (GiveCommand.mc.playerController.isNotCreative()) {
            this.chat("Creative mode only.");
            return;
        }
        if (args.length <= 1) {
            this.chatSyntax(".give <item> [amount] [data] [datatag]");
            return;
        }
        final ItemStack itemStack = ItemUtils.createItem(StringUtils.toCompleteString(args, 1));
        if (itemStack == null) {
            this.chatSyntaxError();
            return;
        }
        int emptySlot = -1;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = GiveCommand.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null) {
                emptySlot = i;
            }
        }
        if (emptySlot != -1) {
            GiveCommand.mc.getNetHandler().addToSendQueue((Packet)new C10PacketCreativeInventoryAction(emptySlot, itemStack));
            this.chat("§7Given [§8" + itemStack.getDisplayName() + "§7] * §8" + itemStack.stackSize + "§7 to §8" + GiveCommand.mc.getSession().getUsername() + "§7.");
        }
        else {
            this.chat("Your inventory is full.");
        }
    }
}
