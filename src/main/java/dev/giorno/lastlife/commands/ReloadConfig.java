package dev.giorno.lastlife.commands;

import dev.giorno.lastlife.LivesMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp()) {
            LivesMain.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "LastLife Config reloaded successfully!");
            return true;
        }

        else sender.sendMessage(ChatColor.RED + "You don't have permissions to run that command!");

        return true;
    }
}
