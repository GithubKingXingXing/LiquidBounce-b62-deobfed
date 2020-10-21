//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.features.command.Command;

public class DamageCommand extends Command
{
    public DamageCommand() {
        super("damage", null);
    }
    
    @Override
    public void execute(final String[] args) {
        int damage = 1;
        if (args.length > 1) {
            try {
                damage = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException ex) {}
        }
        final double x = DamageCommand.mc.thePlayer.posX;
        final double y = DamageCommand.mc.thePlayer.posY;
        final double z = DamageCommand.mc.thePlayer.posZ;
        for (int i = 0; i < 65 * damage; ++i) {
            DamageCommand.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
            DamageCommand.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        }
        DamageCommand.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        this.chat("You got damage.");
    }
}
