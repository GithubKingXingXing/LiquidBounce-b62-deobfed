//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.features.command.Command;

public class SayCommand extends Command
{
    public SayCommand() {
        super("say", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            SayCommand.mc.thePlayer.sendChatMessage(StringUtils.toCompleteString(args, 1));
            this.chat("Message was send to the chat.");
            return;
        }
        this.chatSyntax(".say <message...>");
    }
}
