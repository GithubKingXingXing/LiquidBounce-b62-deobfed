
package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MultiActions;
import net.ccbluex.liquidbounce.event.events.WorldEvent;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import java.nio.ByteBuffer;
import net.ccbluex.liquidbounce.utils.render.IconUtils;
import net.minecraft.util.Util;
import net.ccbluex.liquidbounce.event.events.ClickBlockEvent;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.event.events.KeyEvent;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.event.events.TickEvent;
import org.lwjgl.Sys;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.ScreenEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.ccbluex.liquidbounce.ui.mainmenu.GuiOldMainMenu;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.opengl.Display;
import net.ccbluex.liquidbounce.ui.GuiWelcome;
import net.ccbluex.liquidbounce.ui.GuiUpdate;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ Minecraft.class })
public abstract class MixinMinecraft
{
    @Shadow
    public GuiScreen currentScreen;
    @Shadow
    public boolean skipRenderWorld;
    @Shadow
    private int leftClickCounter;
    @Shadow
    public MovingObjectPosition objectMouseOver;
    @Shadow
    public WorldClient theWorld;
    @Shadow
    public EntityPlayerSP thePlayer;
    @Shadow
    public EffectRenderer effectRenderer;
    @Shadow
    public PlayerControllerMP playerController;
    @Shadow
    public int displayWidth;
    @Shadow
    public int displayHeight;
    @Shadow
    private int rightClickDelayTimer;
    private long lastFrame;
    
    public MixinMinecraft() {
        this.lastFrame = this.getTime();
    }
    
    @Inject(method = { "run" }, at = { @At("HEAD") })
    private void init(final CallbackInfo callbackInfo) {
        if (this.displayWidth < 1067) {
            this.displayWidth = 1067;
        }
        if (this.displayHeight < 622) {
            this.displayHeight = 622;
        }
    }
    
    @Inject(method = { "startGame" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER) })
    private void startGame(final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.startClient();
    }
    
    @Inject(method = { "startGame" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", shift = At.Shift.AFTER) })
    private void afterMainScreen(final CallbackInfo callbackInfo) {
        if (LiquidBounce.CLIENT.latestVersion > 62) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiUpdate());
        }
        else if (LiquidBounce.CLIENT.firstStart) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiWelcome());
        }
    }
    
    @Inject(method = { "createDisplay" }, at = { @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER) })
    private void createDisplay(final CallbackInfo callbackInfo) {
        Display.setTitle("LiquidBounce b62 | 1.8.9");
    }
    
    @Inject(method = { "displayGuiScreen" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", shift = At.Shift.AFTER) })
    private void displayGuiScreen(final CallbackInfo callbackInfo) {
        if (this.currentScreen instanceof GuiMainMenu || (this.currentScreen != null && this.currentScreen.getClass().getName().startsWith("net.labymod") && this.currentScreen.getClass().getSimpleName().equals("ModGuiMainMenu"))) {
            final String lowerCase = LiquidBounce.CLIENT.mainMenuStyle.toLowerCase();
            switch (lowerCase) {
                case "new": {
                    this.currentScreen = new net.ccbluex.liquidbounce.ui.mainmenu.GuiMainMenu();
                    break;
                }
                case "old": {
                    this.currentScreen = new GuiOldMainMenu();
                    break;
                }
            }
            final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            this.currentScreen.setWorldAndResolution(Minecraft.getMinecraft(), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            this.skipRenderWorld = false;
        }
        LiquidBounce.CLIENT.eventManager.callEvent(new ScreenEvent(this.currentScreen));
    }
    
    @Inject(method = { "runGameLoop" }, at = { @At("HEAD") })
    private void runGameLoop(final CallbackInfo callbackInfo) {
        final long currentTime = this.getTime();
        final int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        RenderUtils.deltaTime = deltaTime;
    }
    
    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
    
    @Inject(method = { "runTick" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;joinPlayerCounter:I", shift = At.Shift.BEFORE) })
    private void onTick(final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new TickEvent());
    }
    
    @Inject(method = { "runTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER) })
    private void onKey(final CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState() && this.currentScreen == null) {
            LiquidBounce.CLIENT.eventManager.callEvent(new KeyEvent((Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey()));
        }
    }
    
    @Inject(method = { "sendClickBlockToController" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/MovingObjectPosition;getBlockPos()Lnet/minecraft/util/BlockPos;") })
    private void onClickBlock(final CallbackInfo callbackInfo) {
        if (this.leftClickCounter == 0 && this.theWorld.getBlockState(this.objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air) {
            LiquidBounce.CLIENT.eventManager.callEvent(new ClickBlockEvent(this.objectMouseOver.getBlockPos(), this.objectMouseOver.sideHit));
        }
    }
    
    @Inject(method = { "setWindowIcon" }, at = { @At("HEAD") }, cancellable = true)
    private void setWindowIcon(final CallbackInfo callbackInfo) {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            final ByteBuffer[] liquidBounceFavicon = IconUtils.getFavicon();
            if (liquidBounceFavicon != null) {
                Display.setIcon(liquidBounceFavicon);
                callbackInfo.cancel();
            }
        }
    }
    
    @Inject(method = { "shutdown" }, at = { @At("HEAD") })
    private void shutdown(final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.stopClient();
    }
    
    @Inject(method = { "clickMouse" }, at = { @At("HEAD") })
    private void clickMouse(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(AutoClicker.class).getState()) {
            this.leftClickCounter = 0;
        }
    }
    
    @Inject(method = { "rightClickMouse" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelayTimer:I", shift = At.Shift.AFTER) })
    private void rightClickMouse(final CallbackInfo callbackInfo) {
        final FastPlace fastPlace = (FastPlace)ModuleManager.getModule(FastPlace.class);
        if (fastPlace.getState()) {
            this.rightClickDelayTimer = fastPlace.speedValue.asInteger();
        }
    }
    
    @Inject(method = { "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V" }, at = { @At("HEAD") })
    private void loadWorld(final WorldClient p_loadWorld_1_, final String p_loadWorld_2_, final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new WorldEvent(p_loadWorld_1_));
    }
    
    @Overwrite
    private void sendClickBlockToController(final boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }
        if (this.leftClickCounter <= 0 && (!this.thePlayer.isUsingItem() || ModuleManager.getModule(MultiActions.class).getState())) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockPos = this.objectMouseOver.getBlockPos();
                if (this.leftClickCounter == 0) {
                    LiquidBounce.CLIENT.eventManager.callEvent(new ClickBlockEvent(blockPos, this.objectMouseOver.sideHit));
                }
                if (this.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air && this.playerController.onPlayerDamageBlock(blockPos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockPos, this.objectMouseOver.sideHit);
                    this.thePlayer.swingItem();
                }
            }
            else if (!ModuleManager.getModule(AbortBreaking.class).getState()) {
                this.playerController.resetBlockRemoving();
            }
        }
    }
}
