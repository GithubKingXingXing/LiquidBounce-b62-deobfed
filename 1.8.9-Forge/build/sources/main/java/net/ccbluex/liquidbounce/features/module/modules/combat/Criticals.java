
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import joptsimple.internal.Strings;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Criticals", description = "Automatically deals critical hits.", category = ModuleCategory.COMBAT)
public class Criticals extends Module
{
    private final ListValue modeValue;
    public final IntegerValue delayValue;
    public final MSTimer msTimer;
    
    public Criticals() {
        this.modeValue = new ListValue("Mode", new String[] { "Packet", "HypixelPacket", "NoGround", "Hop", "TPHop", "Jump", "LowJump" }, "packet");
        this.delayValue = new IntegerValue("Delay", 0, 0, 500);
        this.msTimer = new MSTimer();
        LiquidBounce.CLIENT.commandManager.registerCommand(new Command("criticals", null) {
            @Override
            public void execute(final String[] args) {
                if (args.length <= 1 || !args[1].equalsIgnoreCase("mode")) {
                    this.chatSyntax(".criticals <mode>");
                    return;
                }
                if (args.length > 2 && Criticals.this.modeValue.contains(args[2])) {
                    Criticals.this.modeValue.setValue(args[2].toLowerCase());
                    this.chat("§7Criticals mode was set to §8" + Criticals.this.modeValue.asString().toUpperCase() + "§7.");
                    Criticals$1.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("random.anvil_use"), 1.0f));
                    return;
                }
                this.chatSyntax(".criticals mode §c<§8" + Strings.join(Criticals.this.modeValue.getValues(), "§7, §8") + "§c>");
            }
        });
    }
    
    @Override
    public void onEnable() {
        if (this.modeValue.asString().equalsIgnoreCase("NoGround")) {
            Criticals.mc.thePlayer.jump();
        }
    }
    
    @EventTarget
    public void onAttack(final AttackEvent event) {
        if (event.getTargetEntity() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase)event.getTargetEntity();
            if (entity == null || !Criticals.mc.thePlayer.onGround || Criticals.mc.thePlayer.isOnLadder() || Criticals.mc.thePlayer.isInWeb || Criticals.mc.thePlayer.isInWater() || Criticals.mc.thePlayer.isInLava() || Criticals.mc.thePlayer.ridingEntity != null || ModuleManager.getModule(Fly.class).getState() || !this.msTimer.hasTimePassed(this.delayValue.asInteger())) {
                return;
            }
            final double x = Criticals.mc.thePlayer.posX;
            final double y = Criticals.mc.thePlayer.posY;
            final double z = Criticals.mc.thePlayer.posZ;
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "packet": {
                    if (entity.hurtTime >= 7) {
                        break;
                    }
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, true));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    Criticals.mc.thePlayer.onCriticalHit((Entity)entity);
                    break;
                }
                case "hypixelpacket": {
                    if (Criticals.mc.thePlayer.ticksExisted % 9 == 0) {
                        Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + RandomUtils.nextDouble(0.01, 0.06), z, false));
                        Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        Criticals.mc.thePlayer.onCriticalHit((Entity)entity);
                        break;
                    }
                    break;
                }
                case "hop": {
                    Criticals.mc.thePlayer.motionY = 0.10000000149011612;
                    Criticals.mc.thePlayer.fallDistance = 0.1f;
                    Criticals.mc.thePlayer.onGround = false;
                    break;
                }
                case "tphop": {
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.02, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01, z, false));
                    Criticals.mc.thePlayer.setPosition(x, y + 0.01, z);
                    break;
                }
                case "jump": {
                    Criticals.mc.thePlayer.motionY = 0.41999998688697815;
                    break;
                }
                case "lowjump": {
                    Criticals.mc.thePlayer.motionY = 0.3425000011920929;
                    break;
                }
            }
            this.msTimer.reset();
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer && this.modeValue.asString().equalsIgnoreCase("NoGround")) {
            ((C03PacketPlayer)packet).onGround = false;
        }
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
