
package net.ccbluex.liquidbounce.utils;

import net.minecraft.util.MathHelper;
import java.util.ArrayList;
import javax.vecmath.Vector3d;
import java.util.List;
import net.minecraft.client.Minecraft;

public final class PathUtils
{
    private static final Minecraft mc;
    
    public static List<Vector3d> findBlinkPath(final double tpX, final double tpY, final double tpZ) {
        final List<Vector3d> positions = new ArrayList<Vector3d>();
        double curX = PathUtils.mc.thePlayer.posX;
        double curY = PathUtils.mc.thePlayer.posY;
        double curZ = PathUtils.mc.thePlayer.posZ;
        double distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
        int count = 0;
        while (distance > 0.0) {
            distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
            final double diffX = curX - tpX;
            final double diffY = curY - tpY;
            final double diffZ = curZ - tpZ;
            final double offset = ((count & 0x1) == 0x0) ? 0.4 : 0.1;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                }
                else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                }
                else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY += 0.25;
                }
                else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY -= 0.25;
                }
                else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                }
                else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                }
                else {
                    curZ -= Math.abs(diffZ);
                }
            }
            positions.add(new Vector3d(curX, curY, curZ));
            ++count;
        }
        return positions;
    }
    
    public static List<Vector3d> findPath(final double tpX, final double tpY, final double tpZ, final double offset) {
        final List<Vector3d> positions = new ArrayList<Vector3d>();
        final float yaw = (float)(Math.atan2(tpZ - PathUtils.mc.thePlayer.posZ, tpX - PathUtils.mc.thePlayer.posX) * 180.0 / 3.141592653589793 - 90.0);
        final double steps = getDistance(PathUtils.mc.thePlayer.posX, PathUtils.mc.thePlayer.posY, PathUtils.mc.thePlayer.posZ, tpX, tpY, tpZ) / offset;
        double curY = PathUtils.mc.thePlayer.posY;
        for (double d = offset; d < getDistance(PathUtils.mc.thePlayer.posX, PathUtils.mc.thePlayer.posY, PathUtils.mc.thePlayer.posZ, tpX, tpY, tpZ); d += offset) {
            curY -= (PathUtils.mc.thePlayer.posY - tpY) / steps;
            positions.add(new Vector3d(PathUtils.mc.thePlayer.posX - Math.sin(Math.toRadians(yaw)) * d, curY, PathUtils.mc.thePlayer.posZ + Math.cos(Math.toRadians(yaw)) * d));
        }
        positions.add(new Vector3d(tpX, tpY, tpZ));
        return positions;
    }
    
    private static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double xDiff = x1 - x2;
        final double yDiff = y1 - y2;
        final double zDiff = z1 - z2;
        return MathHelper.sqrt_double(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
