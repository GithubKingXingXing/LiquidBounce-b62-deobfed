//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.ChatUtils;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import java.util.Iterator;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "RemoteView", description = "Allows you to see from another player's perspective.", category = ModuleCategory.RENDER)
public class RemoteView extends Module
{
    private EntityPlayer targetPlayer;
    
    public RemoteView() {
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("remoteview", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length > 1) {
                    final String name = args[1];
                    for (final Entity entityLivingBase : RemoteView$1.mc.theWorld.loadedEntityList) {
                        if (entityLivingBase instanceof EntityPlayer && entityLivingBase.getName().equalsIgnoreCase(name)) {
                            RemoteView.this.targetPlayer = (EntityPlayer)entityLivingBase;
                            this.chat("§7RemoteView target was set to '§8" + name + "§7'.");
                            RemoteView$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                            RemoteView.this.setState(true);
                            return;
                        }
                    }
                    this.chat("§7No player with the name '§8" + name + "§7' found.");
                    return;
                }
                this.chatSyntax(".remoteview <target>");
            }
        });
    }
    
    @Override
    public void onDisable() {
        this.targetPlayer = null;
        RemoteView.mc.setRenderViewEntity((Entity)RemoteView.mc.thePlayer);
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (this.targetPlayer == null) {
            this.setState(false);
            ChatUtils.displayChatMessage("§8[§c§lRemote§a§lView§8] §aSelect a player with .remoteview <target>.");
            return;
        }
        RemoteView.mc.setRenderViewEntity((Entity)this.targetPlayer);
    }
}
