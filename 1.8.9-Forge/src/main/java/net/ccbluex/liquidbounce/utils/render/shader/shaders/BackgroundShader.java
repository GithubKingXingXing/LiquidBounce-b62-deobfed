//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9 (1)"!

// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL20;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;

public final class BackgroundShader extends Shader
{
    public static final BackgroundShader BACKGROUND_SHADER;
    private float time;
    
    public BackgroundShader() {
        super("background.frag");
    }
    
    @Override
    public void setupUniforms() {
        this.setupUniform("iResolution");
        this.setupUniform("iTime");
    }
    
    @Override
    public void updateUniforms() {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final int resolutionID = this.getUniform("iResolution");
        if (resolutionID > -1) {
            GL20.glUniform3f(resolutionID, (float)scaledResolution.getScaledWidth(), (float)scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth() + (float)scaledResolution.getScaledWidth());
        }
        final int timeID = this.getUniform("iTime");
        if (timeID > -1) {
            GL20.glUniform1f(timeID, this.time);
        }
        this.time += 0.005f * RenderUtils.deltaTime;
    }
    
    static {
        BACKGROUND_SHADER = new BackgroundShader();
    }
}
