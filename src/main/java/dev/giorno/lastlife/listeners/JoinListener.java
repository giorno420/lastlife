package dev.giorno.lastlife.listeners;

import dev.giorno.lastlife.LivesMain;
import dev.giorno.lastlife.managers.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class JoinListener implements Listener {

    private final LuckPerms luckPerms = LivesMain.getLuckPerms();
    private final Config config = LivesMain.getInstance().getCfg();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = "&f";
        int totalSetDeaths = config.getTotalDeathsInConfig();

        if (!player.hasPlayedBefore()) {
            if (totalSetDeaths > 3){
                prefix = "&2";
            }
            else if (totalSetDeaths == 3){
                prefix = "&a";
            }
            else if (totalSetDeaths == 2){
                prefix = "&e";
            }
            else if (totalSetDeaths == 1){
                prefix = "&e";
            }

            String finalPrefix = prefix;

            luckPerms.getUserManager().modifyUser(event.getPlayer().getUniqueId(), (User user) -> {
                user.data().clear(NodeType.META::matches);

                Map<Integer, String> inheritedPrefixes = user.getCachedData().getMetaData(QueryOptions.nonContextual()).getPrefixes();
                int priority = inheritedPrefixes.keySet().stream().mapToInt(i -> i + 10).max().orElse(10);

                Node node = PrefixNode.builder(finalPrefix, priority).build();

                user.data().add(node);
            });
        }
    }
}
