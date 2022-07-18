package dev.giorno.lastlife.commands;

import dev.giorno.lastlife.LivesMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GetDeaths implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lastlife.getdeaths")){
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }

        if (args.length > 0) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if (LivesMain.getInstance().getCfg().hasPlayer(player.getUniqueId().toString())) {
                String msg = ChatColor.of("#d13bff") + "Deaths for " + args[0] + ": " + ChatColor.of("#00ccff") + LivesMain.getInstance().getCfg().getDeathsFor(player.getUniqueId());
                sender.sendMessage(msg);
            }
            else {
                sender.sendMessage(ChatColor.of("#d13bff") + "That player has not died yet");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "Supply a player to check the deaths of.");
        }


        return true;
    }
}
