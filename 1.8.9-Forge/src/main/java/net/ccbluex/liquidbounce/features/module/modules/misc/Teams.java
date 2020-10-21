
package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Teams", description = "Prevents Killaura from attacking team mates.", category = ModuleCategory.MISC)
public class Teams extends Module
{
    private final BoolValue scoreboardValue;
    private final BoolValue colorValue;
    
    public Teams() {
        this.scoreboardValue = new BoolValue("ScoreboardTeam", true);
        this.colorValue = new BoolValue("Color", true);
    }
    
    public boolean isInYourTeam(final EntityLivingBase entity) {
        if (Teams.mc.thePlayer != null && entity != null) {
            if (this.scoreboardValue.asBoolean() && Teams.mc.thePlayer.getTeam() != null && entity.getTeam() != null && Teams.mc.thePlayer.getTeam().isSameTeam(entity.getTeam())) {
                return true;
            }
            if (this.colorValue.asBoolean() && Teams.mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
                final String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
                final String clientName = Teams.mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
                return targetName.startsWith("§" + clientName.charAt(1));
            }
        }
        return false;
    }
}
