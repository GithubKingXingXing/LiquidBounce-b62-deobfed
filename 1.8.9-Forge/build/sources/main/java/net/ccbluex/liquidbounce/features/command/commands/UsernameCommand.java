
package net.ccbluex.liquidbounce.features.command.commands;

import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import net.ccbluex.liquidbounce.features.command.Command;

public class UsernameCommand extends Command
{
    public UsernameCommand() {
        super("username", null);
    }
    
    @Override
    public void execute(final String[] args) {
        this.chat("Username: " + UsernameCommand.mc.thePlayer.getName());
        final StringSelection stringSelection = new StringSelection(UsernameCommand.mc.thePlayer.getName());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
    }
}
