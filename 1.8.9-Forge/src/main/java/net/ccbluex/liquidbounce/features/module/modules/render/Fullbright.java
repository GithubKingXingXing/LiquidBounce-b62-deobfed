
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.PotionEffect;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Fullbright", description = "Brightens up the world around you.", category = ModuleCategory.RENDER)
public class Fullbright extends Module
{
    private final ListValue modeValue;
    private float prevGamma;
    
    public Fullbright() {
        this.modeValue = new ListValue("Mode", new String[] { "Gamma", "NightVision" }, "Gamma");
        this.prevGamma = -1.0f;
    }
    
    @Override
    public void onEnable() {
        this.prevGamma = Fullbright.mc.gameSettings.gammaSetting;
    }
    
    @Override
    public void onDisable() {
        if (this.prevGamma == -1.0f) {
            return;
        }
        Fullbright.mc.gameSettings.gammaSetting = this.prevGamma;
        this.prevGamma = -1.0f;
        Fullbright.mc.thePlayer.removePotionEffectClient(Potion.nightVision.id);
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (this.getState() || ModuleManager.getModule(XRay.class).getState()) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "gamma": {
                    if (Fullbright.mc.gameSettings.gammaSetting <= 100.0f) {
                        final GameSettings gameSettings = Fullbright.mc.gameSettings;
                        ++gameSettings.gammaSetting;
                        break;
                    }
                    break;
                }
                case "nightvision": {
                    Fullbright.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1337, 1));
                    break;
                }
            }
        }
        else if (this.prevGamma != -1.0f) {
            Fullbright.mc.gameSettings.gammaSetting = this.prevGamma;
            this.prevGamma = -1.0f;
        }
    }
}
