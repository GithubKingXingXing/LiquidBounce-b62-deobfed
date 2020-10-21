
package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.ccbluex.liquidbounce.features.command.Command;

public class EnchantCommand extends Command
{
    public EnchantCommand() {
        super("enchant", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 2) {
            this.chatSyntax(".enchant <type> [level]");
            return;
        }
        if (EnchantCommand.mc.playerController.isNotCreative()) {
            this.chat("§c§lError: §3You need creative mode.");
            return;
        }
        final ItemStack item = EnchantCommand.mc.thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            this.chat("§c§lError: §3You need to hold a item.");
            return;
        }
        try {
            int enchantID;
            try {
                enchantID = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e2) {
                final Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);
                if (enchantment == null) {
                    this.chat("There is no such enchantment with the name " + args[1]);
                    return;
                }
                enchantID = enchantment.effectId;
            }
            final Enchantment enchantment2 = Enchantment.getEnchantmentById(enchantID);
            if (enchantment2 == null) {
                this.chat("There is no such enchantment with ID " + enchantID);
                return;
            }
            int level;
            try {
                level = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e3) {
                this.chatSyntaxError();
                return;
            }
            item.addEnchantment(enchantment2, level);
            this.chat(enchantment2.getTranslatedName(level) + " added to " + item.getDisplayName() + ".");
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
