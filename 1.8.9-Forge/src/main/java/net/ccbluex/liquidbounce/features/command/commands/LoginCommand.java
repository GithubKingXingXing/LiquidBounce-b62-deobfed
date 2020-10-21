
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.ui.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;
import net.ccbluex.liquidbounce.features.command.Command;

public class LoginCommand extends Command
{
    public LoginCommand() {
        super("login", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            this.chatSyntax(".login <username/email> [password]");
            return;
        }
        String result;
        if (args.length > 2) {
            result = GuiAltManager.login(new MinecraftAccount(args[1], args[2]));
        }
        else {
            result = GuiAltManager.login(new MinecraftAccount(args[1]));
        }
        this.chat(result);
        if (result.startsWith("§cYour name is now")) {
            if (LoginCommand.mc.isIntegratedServerRunning()) {
                return;
            }
            LoginCommand.mc.theWorld.sendQuittingDisconnectingPacket();
            ServerUtils.connectToLastServer();
        }
    }
}
