package dev.giorno.lastlife.managers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;

import dev.giorno.lastlife.LivesMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Config {
    private final YamlConfiguration config;
    private final File configFile = new File("plugins/LastLife/config.yml");

    private final YamlConfiguration deathConfig;
    private final File deathFile = new File("plugins/LastLife/deaths.yml");
    private List<PotionEffect> potionEffectList;

    public Config() {
        File folder = new File("plugins/LastLife");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!folder.exists()) {
            try {
                this.configFile.createNewFile();
                this.deathFile.createNewFile();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.deathConfig = YamlConfiguration.loadConfiguration(this.deathFile);
    }

    public void add(UUID player) {
        int count = 1;
        if (this.deathConfig.isSet("players." + player)) {
            count = this.deathConfig.getInt("players." + player) + 1;
        }

        this.deathConfig.set("players." + player, count);
        ForkJoinPool.commonPool().submit(() -> {
            try {
                this.deathConfig.save(this.deathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public boolean hasPlayer(String uuid) {
        ConfigurationSection sec = this.deathConfig.getConfigurationSection("players");
        return sec != null && sec.contains(uuid);
    }

    public int getDeathsFor(UUID player) {
        return this.deathConfig.getInt("players." + player.toString());
    }

    public void clearFor(UUID player){
        this.deathConfig.set("players." + player, 0);
        ForkJoinPool.commonPool().submit(() -> {
            try {
                this.deathConfig.save(this.deathFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method to get effects and multipliers for each death
     */
    public List<PotionEffect> getEffectsFor(int deathNumero){

        List<PotionEffect> potionEffectList = new ArrayList<>();
        potionEffectList.clear();

        ConfigurationSection section = config.getConfigurationSection("deaths." + deathNumero);

        if (section ==  null) return potionEffectList;

        section.getValues(true).forEach((effect, value) -> {

            try {
                potionEffectList.add(
                        new PotionEffect(
                                PotionEffectType.getByName(effect),
                                Integer.MAX_VALUE,
                                (Integer) value
                        )
                );
            }
            catch (Exception e) {
                LivesMain.getInstance().getLogger().log(Level.INFO, "error happened: ");
                e.printStackTrace();
            }
        });

        return potionEffectList;
    }

    /**
     * Method to get amounts of deaths with commands assigned to them in the config file
     * @return total deaths assigned in config
     */
    public int getTotalDeathsInConfig(){
        return this.config.getConfigurationSection("deaths").getKeys(false).size();
    }


    /**
     * Whenever a string has the substring of [player] in it, its replaced with a new name
     * eg: `/gamemode creative [player]` will be changed to `/gamemode creative BeenTaken` if the
     *      supplied value is BeenTaken
     * @return the changed string
     */
    public String replacePlayer(String original, String newName){
        return original.replaceAll("[player]", newName);
    }

    /**
     * Checks if the player is on their last life
     * @return true if the player is on their last life, false if they're not
     */
    public boolean isLastLife(int deathNumero){
        return getTotalDeathsInConfig() - deathNumero == 1;
    }

    /**
     * Checks if the player has exhausted all their lives
     * @param deathNumero the number of deaths
     * @return true if they've died the maximum amount of times, false if they haven't
     */
    public boolean isCompletelyDead(int deathNumero){
        return getTotalDeathsInConfig() - deathNumero <= 0;
    }
}

