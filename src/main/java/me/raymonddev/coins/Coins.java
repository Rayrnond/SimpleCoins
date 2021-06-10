package me.raymonddev.coins;

import me.raymonddev.coins.commands.CoinsCommand;
import me.raymonddev.coins.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class Coins extends JavaPlugin {

    private static final HashMap<Player, Integer> coinMap = new HashMap<>();
    private static Coins instance;

    @Override
    public void onEnable() {
        instance=this;
        getDataFolder().mkdir();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("coins").setExecutor(new CoinsCommand());
        for(Player player : Bukkit.getOnlinePlayers()) {
            setupPlayer(player);
        }

    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers())
            removePlayer(player);
    }

    public void setupPlayer(Player player) {
        File file = new File(getDataFolder() + File.separator + player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        try {
            if(!file.exists())
                file.createNewFile();
            if(!config.contains("coins")) {
                config.set("coins", 0);
                config.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCoins(player, config.getInt("coins"));
    }

    public void removePlayer(Player player) {
        File file = new File(getDataFolder() + File.separator + player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        try {
            if(!file.exists())
                file.createNewFile();
            config.set("coins", getCoins(player));
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        coinMap.remove(player);
    }

    public int getCoins(Player player) {
        return coinMap.get(player);
    }

    public void setCoins(Player player, int amount) {
        coinMap.put(player, amount);
    }

    public static Coins getInstance() {
        return instance;
    }

}
