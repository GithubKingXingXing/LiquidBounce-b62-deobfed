
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;

public class PingCommand extends Command
{
    public PingCommand() {
        super("ping", null);
    }
    
    @Override
    public void execute(final String[] args) {
        this.chat("§3Your ping is §a" + PingCommand.mc.getNetHandler().getPlayerInfo(PingCommand.mc.thePlayer.getUniqueID()).getResponseTime() + "§3ms.");
    }
}
