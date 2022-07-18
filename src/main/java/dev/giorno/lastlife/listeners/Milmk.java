package dev.giorno.lastlife.listeners;

import dev.giorno.lastlife.LivesMain;
import dev.giorno.lastlife.managers.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Milmk implements Listener {

    private Config config = LivesMain.getInstance().getCfg();

    @EventHandler
    public void milmkDrink(PlayerItemConsumeEvent event){
        if (event.getItem().getType().equals(Material.MILK_BUCKET)){
            int playerDeaths = config.getDeathsFor(event.getPlayer().getUniqueId());
            Player player = event.getPlayer();

            new BukkitRunnable(){
              @Override
              public void run(){
                  player.addPotionEffects(config.getEffectsFor(playerDeaths));
              }
            }.runTaskLater(LivesMain.getInstance(), 20);
        }
    }
}
