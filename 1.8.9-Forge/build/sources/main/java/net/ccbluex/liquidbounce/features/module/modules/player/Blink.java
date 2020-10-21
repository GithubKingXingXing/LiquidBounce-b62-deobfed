//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import java.util.LinkedList;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import java.util.List;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Blink", description = "Suspends all movement packets.", category = ModuleCategory.PLAYER)
public class Blink extends Module
{
    private final List<Packet> packets;
    private EntityOtherPlayerMP fakePlayer;
    private int count;
    private boolean disableLogger;
    private final LinkedList<double[]> positions;
    private final BoolValue pulseValue;
    private final IntegerValue pulseDelayValue;
    private final MSTimer pulseTimer;
    
    public Blink() {
        this.packets = new ArrayList<Packet>();
        this.fakePlayer = null;
        this.positions = new LinkedList<double[]>();
        this.pulseValue = new BoolValue("Pulse", false);
        this.pulseDelayValue = new IntegerValue("PulseDelay", 1000, 500, 5000);
        this.pulseTimer = new MSTimer();
    }
    
    @Override
    public void onEnable() {
        if (Blink.mc.thePlayer == null) {
            return;
        }
        (this.fakePlayer = new EntityOtherPlayerMP((World)Blink.mc.theWorld, Blink.mc.thePlayer.getGameProfile())).clonePlayer((EntityPlayer)Blink.mc.thePlayer, true);
        this.fakePlayer.copyLocationAndAnglesFrom((Entity)Blink.mc.thePlayer);
        this.fakePlayer.rotationYawHead = Blink.mc.thePlayer.rotationYawHead;
        Blink.mc.theWorld.addEntityToWorld(-9100, (Entity)this.fakePlayer);
        synchronized (this.positions) {
            this.positions.add(new double[] { Blink.mc.thePlayer.posX, Blink.mc.thePlayer.getEntityBoundingBox().minY + Blink.mc.thePlayer.getEyeHeight() / 2.0f, Blink.mc.thePlayer.posZ });
            this.positions.add(new double[] { Blink.mc.thePlayer.posX, Blink.mc.thePlayer.getEntityBoundingBox().minY, Blink.mc.thePlayer.posZ });
        }
        this.pulseTimer.reset();
    }
    
    @Override
    public void onDisable() {
        if (Blink.mc.thePlayer == null || this.fakePlayer == null) {
            return;
        }
        this.blink();
        Blink.mc.theWorld.removeEntityFromWorld(this.fakePlayer.getEntityId());
        this.fakePlayer = null;
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (Blink.mc.thePlayer == null || !(packet instanceof C03PacketPlayer) || this.disableLogger) {
            return;
        }
        event.setCancelled(true);
        if (!(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
            return;
        }
        this.packets.add(packet);
        ++this.count;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        synchronized (this.positions) {
            this.positions.add(new double[] { Blink.mc.thePlayer.posX, Blink.mc.thePlayer.getEntityBoundingBox().minY, Blink.mc.thePlayer.posZ });
        }
        if (this.pulseValue.asBoolean() && this.pulseTimer.hasTimePassed(this.pulseDelayValue.asInteger())) {
            this.blink();
            this.pulseTimer.reset();
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final Breadcrumbs breadcrumbs = (Breadcrumbs)ModuleManager.getModule(Breadcrumbs.class);
        final Color color = breadcrumbs.colorRainbow.asBoolean() ? ColorUtils.rainbow() : new Color((int)breadcrumbs.colorRedValue.asObject(), (int)breadcrumbs.colorGreenValue.asObject(), (int)breadcrumbs.colorBlueValue.asObject());
        synchronized (this.positions) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            Blink.mc.entityRenderer.disableLightmap();
            GL11.glBegin(3);
            RenderUtils.glColor(color);
            final double renderPosX = Blink.mc.getRenderManager().viewerPosX;
            final double renderPosY = Blink.mc.getRenderManager().viewerPosY;
            final double renderPosZ = Blink.mc.getRenderManager().viewerPosZ;
            for (final double[] pos : this.positions) {
                GL11.glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);
            }
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glEnd();
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public String getTag() {
        return String.valueOf(this.count);
    }
    
    private void blink() {
        try {
            this.disableLogger = true;
            final Iterator<Packet> packetIterator = this.packets.iterator();
            while (packetIterator.hasNext()) {
                Blink.mc.getNetHandler().addToSendQueue((Packet)packetIterator.next());
                packetIterator.remove();
            }
            this.count = 0;
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
        synchronized (this.positions) {
            this.positions.clear();
        }
    }
}
