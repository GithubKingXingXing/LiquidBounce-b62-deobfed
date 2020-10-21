
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;

public class VClipCommand extends Command
{
    public VClipCommand() {
        super("vclip", null);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            try {
                final double y = Double.parseDouble(args[1]);
                if (VClipCommand.mc.thePlayer.ridingEntity == null) {
                    VClipCommand.mc.thePlayer.setPosition(VClipCommand.mc.thePlayer.posX, VClipCommand.mc.thePlayer.posY + y, VClipCommand.mc.thePlayer.posZ);
                }
                else {
                    VClipCommand.mc.thePlayer.ridingEntity.setPosition(VClipCommand.mc.thePlayer.ridingEntity.posX, VClipCommand.mc.thePlayer.posY + y, VClipCommand.mc.thePlayer.ridingEntity.posZ);
                }
                this.chat("You were teleported.");
            }
            catch (NumberFormatException exception) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax(".vclip <id>");
    }
}
