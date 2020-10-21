
package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.item.ItemStack;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class RenameCommand extends Command
{
    public RenameCommand() {
        super("rename", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            this.chatSyntax(".rename <displayName>");
            return;
        }
        if (RenameCommand.mc.playerController.isNotCreative()) {
            this.chat("§c§lError: §3You need creative mode.");
            return;
        }
        final ItemStack item = RenameCommand.mc.thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            this.chat("§c§lError: §3You need to hold a item.");
            return;
        }
        item.setStackDisplayName(ChatColor.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)));
        this.chat("§3Item renamed to '" + item.getDisplayName() + "§3'");
    }
}
