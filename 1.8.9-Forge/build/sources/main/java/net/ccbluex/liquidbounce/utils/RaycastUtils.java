//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public final class RaycastUtils
{
    private static final Minecraft mc;
    
    public static Entity raycastEntity(final double range) {
        return raycastEntity(range, entity -> true);
    }
    
    public static Entity raycastEntity(final double range, final IEntityFilter entityFilter) {
        return raycastEntity(range, RotationUtils.lastLook[0], RotationUtils.lastLook[1], entityFilter);
    }
    
    public static Entity raycastEntity(final double range, final float yaw, final float pitch, final IEntityFilter entityFilter) {
        final Entity renderViewEntity = RaycastUtils.mc.getRenderViewEntity();
        if (renderViewEntity != null && RaycastUtils.mc.theWorld != null) {
            double blockReachDistance = range;
            final Vec3 eyePosition = renderViewEntity.getPositionEyes(1.0f);
            final float yawCos = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
            final float yawSin = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
            final float pitchCos = -MathHelper.cos(-pitch * 0.017453292f);
            final float pitchSin = MathHelper.sin(-pitch * 0.017453292f);
            final Vec3 entityLook = new Vec3((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
            final Vec3 vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
            final List<Entity> entityList = (List<Entity>)RaycastUtils.mc.theWorld.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            Entity pointedEntity = null;
            for (final Entity entity : entityList) {
                if (!entityFilter.canRaycast(entity)) {
                    continue;
                }
                final float collisionBorderSize = entity.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
                final MovingObjectPosition movingObjectPosition = axisalignedbb.calculateIntercept(eyePosition, vector);
                if (axisalignedbb.isVecInside(eyePosition)) {
                    if (blockReachDistance < 0.0) {
                        continue;
                    }
                    pointedEntity = entity;
                    blockReachDistance = 0.0;
                }
                else {
                    if (movingObjectPosition == null) {
                        continue;
                    }
                    final double d3 = eyePosition.distanceTo(movingObjectPosition.hitVec);
                    if (d3 >= blockReachDistance && blockReachDistance != 0.0) {
                        continue;
                    }
                    if (entity == renderViewEntity.ridingEntity && !renderViewEntity.canRiderInteract()) {
                        if (blockReachDistance != 0.0) {
                            continue;
                        }
                        pointedEntity = entity;
                    }
                    else {
                        pointedEntity = entity;
                        blockReachDistance = d3;
                    }
                }
            }
            return pointedEntity;
        }
        return null;
    }
    
    public static Collection<Entity> raycastEntities(final double range) {
        final Collection<Entity> entities = new ArrayList<Entity>();
        final Entity renderViewEntity = RaycastUtils.mc.getRenderViewEntity();
        if (renderViewEntity != null && RaycastUtils.mc.theWorld != null) {
            final Vec3 eyePosition = renderViewEntity.getPositionEyes(1.0f);
            final float yaw = RotationUtils.lastLook[0];
            final float pitch = RotationUtils.lastLook[1];
            final float yawCos = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
            final float yawSin = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
            final float pitchCos = -MathHelper.cos(-pitch * 0.017453292f);
            final float pitchSin = MathHelper.sin(-pitch * 0.017453292f);
            final Vec3 entityLook = new Vec3((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
            final Vec3 vector = eyePosition.addVector(entityLook.xCoord * range, entityLook.yCoord * range, entityLook.zCoord * range);
            final List<Entity> entityList = (List<Entity>)RaycastUtils.mc.theWorld.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * range, entityLook.yCoord * range, entityLook.zCoord * range).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            for (final Entity entity : entityList) {
                final float collisionBorderSize = entity.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
                final MovingObjectPosition movingObjectPosition = axisalignedbb.calculateIntercept(eyePosition, vector);
                if (axisalignedbb.isVecInside(eyePosition)) {
                    if (range < 0.0) {
                        continue;
                    }
                    entities.add(entity);
                }
                else {
                    if (movingObjectPosition == null) {
                        continue;
                    }
                    final double d3 = eyePosition.distanceTo(movingObjectPosition.hitVec);
                    if (d3 >= range && range != 0.0) {
                        continue;
                    }
                    if (entity == renderViewEntity.ridingEntity && !renderViewEntity.canRiderInteract()) {
                        if (range != 0.0) {
                            continue;
                        }
                        entities.add(entity);
                    }
                    else {
                        entities.add(entity);
                    }
                }
            }
        }
        return entities;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public interface IEntityFilter
    {
        boolean canRaycast(final Entity p0);
    }
}
