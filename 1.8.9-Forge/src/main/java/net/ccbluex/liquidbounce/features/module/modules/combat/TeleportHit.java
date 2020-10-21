
package net.ccbluex.liquidbounce.features.module.modules.combat;

import javax.vecmath.Vector3d;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.PathUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "TeleportHit", description = "Allows to hit entities from far away.", category = ModuleCategory.COMBAT)
public class TeleportHit extends Module
{
    private EntityLivingBase targetEntity;
    private boolean shouldHit;
    
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (event.getEventState() != EventState.PRE) {
            return;
        }
        final Entity facedEntity = RaycastUtils.raycastEntity(100.0, raycastedEntity -> raycastedEntity instanceof EntityLivingBase);
        if (TeleportHit.mc.gameSettings.keyBindAttack.isKeyDown() && EntityUtils.isSelected(facedEntity, true) && facedEntity.getDistanceSqToEntity((Entity)TeleportHit.mc.thePlayer) >= 1.0) {
            this.targetEntity = (EntityLivingBase)facedEntity;
        }
        if (this.targetEntity != null) {
            if (!this.shouldHit) {
                this.shouldHit = true;
                return;
            }
            if (TeleportHit.mc.thePlayer.fallDistance > 0.0f) {
                final Vec3 rotationVector = RotationUtils.getVectorForRotation(0.0f, TeleportHit.mc.thePlayer.rotationYaw);
                final double x = TeleportHit.mc.thePlayer.posX + rotationVector.xCoord * (TeleportHit.mc.thePlayer.getDistanceToEntity((Entity)this.targetEntity) - 1.0f);
                final double z = TeleportHit.mc.thePlayer.posZ + rotationVector.zCoord * (TeleportHit.mc.thePlayer.getDistanceToEntity((Entity)this.targetEntity) - 1.0f);
                final double y = this.targetEntity.getPosition().getY() + 0.25;
                PathUtils.findPath(x, y + 1.0, z, 4.0).forEach(pos -> TeleportHit.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(pos.getX(), pos.getY(), pos.getZ(), false)));
                TeleportHit.mc.thePlayer.swingItem();
                TeleportHit.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity((Entity)this.targetEntity, C02PacketUseEntity.Action.ATTACK));
                TeleportHit.mc.thePlayer.onCriticalHit((Entity)this.targetEntity);
                this.shouldHit = false;
                this.targetEntity = null;
            }
            else if (TeleportHit.mc.thePlayer.onGround) {
                TeleportHit.mc.thePlayer.jump();
            }
        }
        else {
            this.shouldHit = false;
        }
    }
}
