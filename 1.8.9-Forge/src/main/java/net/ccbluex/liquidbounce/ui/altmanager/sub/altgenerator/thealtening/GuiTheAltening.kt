package net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator

import com.mojang.authlib.Agent.MINECRAFT
import com.mojang.authlib.exceptions.AuthenticationException
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
import com.thealtening.AltService
import com.thealtening.api.TheAltening
import com.thealtening.utilities.SSLVerification
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.misc.MiscUtils
import net.mcleaks.MCLeaks
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import net.minecraft.util.Session
import org.lwjgl.input.Keyboard
import java.net.Proxy.NO_PROXY
import javax.naming.AuthenticationException

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
class GuiTheAltening(private val prevGui : GuiAltManager) : GuiScreen() {

    // Data Storage
    companion object {
        var apiKey : String = ""
    }

    // Buttons
    private lateinit var loginButton: GuiButton
    private lateinit var generateButton: GuiButton

    // User Input Fields
    private lateinit var apiKeyField : GuiTextField
    private lateinit var tokenField : GuiTextField

    // Status
    private var status = ""

    // TheAltening

    /**
     * Initialize The Altening Generator GUI
     */
    override fun initGui() {
        // Enable keyboard repeat events
        Keyboard.enableRepeatEvents(true)

        // Add buttons to screen
        loginButton = GuiButton(2, width / 2 - 100, 105, "Login")
        buttonList.add(loginButton)
        generateButton = GuiButton(1, width / 2 - 100, 175, "Generate")
        buttonList.add(generateButton)
        buttonList.add(GuiButton(3, width / 2 - 100, height - 83, "Buy"))
        buttonList.add(GuiButton(0, width / 2 - 100, height - 60, "Back"))

        // Add fields to screen
        tokenField = GuiTextField(666, Fonts.font40, width / 2 - 100, 80, 200, 20)
        tokenField.isFocused = true
        tokenField.maxStringLength = Integer.MAX_VALUE

        apiKeyField = GuiPasswordField(1337, Fonts.font40, width / 2 - 100, 150, 200, 20)
        apiKeyField.maxStringLength = 18
        apiKeyField.text = apiKey

        // Verify SSL
        val sslVerification = SSLVerification()
        sslVerification.verify()

        // Call sub method
        super.initGui()
    }

    /**
     * Draw screen
     */
    override fun drawScreen(mouseX : Int, mouseY : Int, partialTicks : Float) {
        // Draw background to screen
        drawBackground(0)
        Gui.drawRect(30, 30, width - 30, height - 30, Integer.MIN_VALUE)

        // Draw title and status
        drawCenteredString(Fonts.font35, "TheAltening", width / 2, 36, 0xffffff)
        drawCenteredString(Fonts.font35, status, width / 2, height - 95, 0xffffff)

        // Draw fields
        apiKeyField.drawTextBox()
        tokenField.drawTextBox()

        drawCenteredString(Fonts.font40, "§7API-Key:", width / 2 - 78, 137, 0xffffff)
        drawCenteredString(Fonts.font40, "§7Token:", width / 2 - 84, 66, 0xffffff)
        drawCenteredString(Fonts.font40, "§7Use coupon code 'liquidbounce' for 20% off!", width / 2, height - 110, 0xffffff);

        // Call sub method
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    /**
     * Handle button actions
     */
    override fun actionPerformed(button : GuiButton) {
        if(!button.enabled) return

        when(button.id) {
            0 -> mc.displayGuiScreen(prevGui)
            1 -> {
                loginButton.enabled = false
                generateButton.enabled = false

                apiKey = apiKeyField.text

                val altening = TheAltening(apiKey)
                val asynchronous = TheAltening.Asynchronous(altening)

                status = "§cGenerating account..."

                asynchronous.accountData.thenAccept { account ->
                    status = "§aGenerated account: §b§l${account.username}"

                    try {
                        status = "§cSwitching Alt Service..."

                        // Change Alt Service
                        GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING)

                        status = "§cLogging in..."

                        // Set token as username
                        val yggdrasilUserAuthentication = YggdrasilUserAuthentication(YggdrasilAuthenticationService(NO_PROXY, ""), MINECRAFT)

                        yggdrasilUserAuthentication.setUsername(account.token)
                        yggdrasilUserAuthentication.setPassword(LiquidBounce.CLIENT_NAME)

                        status = try {
                            yggdrasilUserAuthentication.logIn()

                            mc.session = Session(yggdrasilUserAuthentication.selectedProfile.name, yggdrasilUserAuthentication
                                    .selectedProfile.id.toString(),
                                    yggdrasilUserAuthentication.authenticatedToken, "mojang")
                            MCLeaks.remove()
                            prevGui.status = "§aYour name is now §b§l${yggdrasilUserAuthentication.selectedProfile.name}§c."
                            mc.displayGuiScreen(prevGui)
                            "§aYour name is now §b§l${yggdrasilUserAuthentication.selectedProfile.name}§c."
                        } catch (e: AuthenticationException) {
                            GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG)

                            ClientUtils.getLogger().error("Failed to login.", e)
                            "§cFailed to login: ${e.message}"
                        }
                    } catch (throwable: Throwable) {
                        status = "§cFailed to login. Unknown error."
                        ClientUtils.getLogger().error("Failed to login.", throwable)
                    }

                    loginButton.enabled = true
                    generateButton.enabled = true
                }.handle { _, err ->
                    status = "§cFailed to generate account."
                    ClientUtils.getLogger().error("Failed to generate account.", err)
                }.whenComplete { _, _ ->
                    loginButton.enabled = true
                    generateButton.enabled = true
                }
            }
            2 -> {
                loginButton.enabled = false
                generateButton.enabled = false

                Thread(Runnable {
                    try {
                        status = "§cSwitching Alt Service..."

                        // Change Alt Service
                        GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING)

                        status = "§cLogging in..."

                        // Set token as username
                        val yggdrasilUserAuthentication = YggdrasilUserAuthentication(YggdrasilAuthenticationService(NO_PROXY, ""), MINECRAFT)

                        yggdrasilUserAuthentication.setUsername(tokenField.text)
                        yggdrasilUserAuthentication.setPassword(LiquidBounce.CLIENT_NAME)

                        status = try {
                            yggdrasilUserAuthentication.logIn()

                            mc.session = Session(yggdrasilUserAuthentication.selectedProfile.name, yggdrasilUserAuthentication
                                    .selectedProfile.id.toString(),
                                    yggdrasilUserAuthentication.authenticatedToken, "mojang")
                            LiquidBounce.CLIENT.eventManager.callEvent(SessionEvent())
                            MCLeaks.remove()
                            prevGui.status = "§aYour name is now §b§l${yggdrasilUserAuthentication.selectedProfile.name}§c."
                            mc.displayGuiScreen(prevGui)
                            "§aYour name is now §b§l${yggdrasilUserAuthentication.selectedProfile.name}§c."
                        } catch(e : AuthenticationException) {
                            GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG)

                            ClientUtils.getLogger().error("Failed to login.", e)
                            "§cFailed to login: ${e.message}"
                        }
                    } catch(throwable : Throwable) {
                        ClientUtils.getLogger().error("Failed to login.", throwable)
                        status = "§cFailed to login. Unknown error."
                    }

                    loginButton.enabled = true
                    generateButton.enabled = true
                }).start()
            }
            3 -> {
                MiscUtils.showURL("https://thealtening.com/?ref=liquidbounce")
            }
        }

        // Call sub method
        super.actionPerformed(button)
    }

    /**
     * Handle key typed
     */
    override fun keyTyped(typedChar : Char, keyCode : Int) {
        // Check if user want to escape from screen
        if(Keyboard.KEY_ESCAPE == keyCode) {
            // Send back to prev screen
            mc.displayGuiScreen(prevGui)

            // Quit
            return
        }

        // Check if field is focused, then call key typed
        if(apiKeyField.isFocused) apiKeyField.textboxKeyTyped(typedChar, keyCode)
        if(tokenField.isFocused) tokenField.textboxKeyTyped(typedChar, keyCode)

        // Call sub method
        super.keyTyped(typedChar, keyCode)
    }

    /**
     * Handle mouse clicked
     */
    override fun mouseClicked(mouseX : Int, mouseY : Int, mouseButton : Int) {
        // Call mouse clicked to field
        apiKeyField.mouseClicked(mouseX, mouseY, mouseButton)
        tokenField.mouseClicked(mouseX, mouseY, mouseButton)

        // Call sub method
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    /**
     * Handle screen update
     */
    override fun updateScreen() {
        apiKeyField.updateCursorCounter()
        tokenField.updateCursorCounter()
        super.updateScreen()
    }

    /**
     * Handle gui closed
     */
    override fun onGuiClosed() {
        // Disable keyboard repeat events
        Keyboard.enableRepeatEvents(false)

        // Set API key
        apiKey = apiKeyField.text

        // Call sub method
        super.onGuiClosed()
    }
}