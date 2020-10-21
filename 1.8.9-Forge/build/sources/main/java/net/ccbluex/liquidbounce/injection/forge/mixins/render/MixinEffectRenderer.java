
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.particle.EntityParticleEmitter;
import java.util.List;
import net.minecraft.client.particle.EffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ EffectRenderer.class })
public abstract class MixinEffectRenderer
{
    @Shadow
    private List<EntityParticleEmitter> particleEmitters;
    
    @Shadow
    protected abstract void updateEffectLayer(final int p0);
    
    @Overwrite
    public void updateEffects() {
        try {
            for (int i = 0; i < 4; ++i) {
                this.updateEffectLayer(i);
            }
            final Iterator<EntityParticleEmitter> it = this.particleEmitters.iterator();
            while (it.hasNext()) {
                final EntityParticleEmitter entityParticleEmitter = it.next();
                entityParticleEmitter.onUpdate();
                if (entityParticleEmitter.isDead) {
                    it.remove();
                }
            }
        }
        catch (ConcurrentModificationException ex) {}
    }
}
