package net.ccbluex.liquidbounce.features.command

import net.ccbluex.liquidbounce.features.command.commands.*
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
@SideOnly(Side.CLIENT)
class CommandManager {

    val commands = mutableListOf<Command>()
    var prefix = '.'

    /**
     * Register all default commands
     */
    fun registerCommands() {
        registerCommand(BindCommand())
        registerCommand(VClipCommand())
        registerCommand(HClipCommand())
        registerCommand(HelpCommand())
        registerCommand(SayCommand())
        registerCommand(FriendCommand())
        registerCommand(AutoSettingsCommand())
        registerCommand(LocalAutoSettingsCommand())
        registerCommand(ServerInfoCommand())
        registerCommand(ToggleCommand())
        registerCommand(GiveCommand())
        registerCommand(UsernameCommand())
        registerCommand(TargetCommand())
        registerCommand(TacoCommand())
        registerCommand(BindsCommand())
        registerCommand(HoloStandCommand())
        registerCommand(PanicCommand())
        registerCommand(PingCommand())
        registerCommand(RenameCommand())
        registerCommand(EnchantCommand())
        registerCommand(ReloadCommand())
        registerCommand(LoginCommand())
        registerCommand(ScriptManagerCommand())
    }

    /**
     * Execute command by given [input]
     */
    fun executeCommands(input: String) {
        for (command in commands) {
            val args = input.split(" ").toTypedArray()

            if (args[0].equals(prefix.toString() + command.command, ignoreCase = true)) {
                command.execute(args)
                return
            }

            for (alias in command.alias) {
                if (!args[0].equals(prefix.toString() + alias, ignoreCase = true))
                    continue

                command.execute(args)
                return
            }
        }

        ClientUtils.displayChatMessage("§cCommand not found. Type ${prefix}help to view all commands.")
    }

    /**
     * Get command instance by given [name]
     */
    fun getCommand(name: String): Command? {
        for (command in commands)
            if (command.command.equals(name, ignoreCase = true))
                return command

        return null
    }

    /**
     * Register [command] by just adding it to the commands registry
     */
    fun registerCommand(command: Command) = commands.add(command)

    /**
     * Unregister [command] by just removing it from the commands registry
     */
    fun unregisterCommand(command: Command?) = commands.remove(command)
}