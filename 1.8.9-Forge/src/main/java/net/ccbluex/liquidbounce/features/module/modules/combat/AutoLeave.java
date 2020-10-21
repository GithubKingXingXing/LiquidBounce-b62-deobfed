
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoLeave", description = "Automatically makes you leave the server whenever your health is low.", category = ModuleCategory.COMBAT)
public class AutoLeave extends Module
{
    private final FloatValue healthValue;
    private final ListValue modeValue;
    
    public AutoLeave() {
        this.healthValue = new FloatValue("Health", 8.0f, 0.0f, 20.0f);
        this.modeValue = new ListValue("Mode", new String[] { "Quit", "InvaildPacket", "SelfHurt", "IllegalChat" }, "Quit");
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("autoleave", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("health")) {
                    this.chatSyntax(".autoleave <health>");
                    return;
                }
                if (args.length > 2) {
                    try {
                        final float value = Float.parseFloat(args[2]);
                        AutoLeave.this.healthValue.setValue(value);
                        this.chat("§7AutoLeave health was set to §8" + value + "§7.");
                        AutoLeave$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax("autoleave health <value>");
            }
        });
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (AutoLeave.mc.thePlayer.getHealth() <= this.healthValue.asFloat() && !AutoLeave.mc.thePlayer.capabilities.isCreativeMode && !AutoLeave.mc.isIntegratedServerRunning()) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "quit": {
                    AutoLeave.mc.theWorld.sendQuittingDisconnectingPacket();
                    break;
                }
                case "invaildpacket": {
                    AutoLeave.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, !AutoLeave.mc.thePlayer.onGround));
                    break;
                }
                case "selfhurt": {
                    AutoLeave.mc.getNetHandler().addToSendQueue((Packet)new C02PacketUseEntity((Entity)AutoLeave.mc.thePlayer, C02PacketUseEntity.Action.ATTACK));
                    break;
                }
                case "illegalchat": {
                    AutoLeave.mc.thePlayer.sendChatMessage(RandomUtils.getRandom().nextInt() + "§§§" + RandomUtils.getRandom().nextInt());
                    break;
                }
            }
            this.setState(false);
        }
    }
}
