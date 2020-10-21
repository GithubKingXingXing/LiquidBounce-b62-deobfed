
package net.ccbluex.liquidbounce.injection.forge.mixins.packets;

import org.spongepowered.asm.mixin.Overwrite;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.EnumConnectionState;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ C00Handshake.class })
public class MixinC00Handshake
{
    @Shadow
    private int protocolVersion;
    @Shadow
    private int port;
    @Shadow
    private EnumConnectionState requestedState;
    @Shadow
    private String ip;
    
    @Overwrite
    public void writePacketData(final PacketBuffer buf) {
        buf.writeVarIntToBuffer(this.protocolVersion);
        buf.writeString(this.ip + ((AntiForge.enabled && AntiForge.blockFML) ? "" : "\u0000FML\u0000"));
        buf.writeShort(this.port);
        buf.writeVarIntToBuffer(this.requestedState.getId());
    }
}
