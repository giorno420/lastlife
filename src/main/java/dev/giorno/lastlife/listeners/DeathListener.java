package dev.giorno.lastlife.listeners;

import dev.giorno.lastlife.LivesMain;
import dev.giorno.lastlife.managers.Config;
import net.luckperms.api.LuckPerms;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.UUID;

public class DeathListener implements Listener {

    private final LuckPerms luckPerms = LivesMain.getLuckPerms();

    @EventHandler
    public void onDeath(PlayerRespawnEvent event){

        Config config = LivesMain.getInstance().getCfg();
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        config.add(playerUUID);

        int playerDeaths = config.getDeathsFor(playerUUID);

        if (playerDeaths <= config.getTotalDeathsInConfig()){

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.addPotionEffects(config.getEffectsFor(playerDeaths));
                    player.sendTitle("death numero " + playerDeaths, "", 10, 20, 5);
                }
            }.runTaskLater(LivesMain.getInstance(), 1);
        }

        else if (config.isCompletelyDead(playerDeaths)){
            int banDurationMillis = LivesMain.getInstance().getConfig().getInt("banduration") * 24 * 60 * 60 * 1000;

            Date unbanDate = new Date();
            unbanDate.setTime(unbanDate.getTime() + banDurationMillis);

            if (!player.isOp()) {
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), LivesMain.getInstance().getConfig().getString("banmessage"), unbanDate, null);
                player.kickPlayer("Bye");
            }
            else {
                player.sendRawMessage(ChatColor.MAGIC + "K" + ChatColor.RESET + " Cheater of death, master of the worlds. " + ChatColor.MAGIC + "K");
            }

            config.clearFor(playerUUID);
        }
    }
}
