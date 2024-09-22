package xyz.naphy.buttonMaintenance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.naphy.buttonMaintenance.commands.*;
import xyz.naphy.buttonMaintenance.listeners.ButtonManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class ButtonMaintenance extends JavaPlugin {

    public static ButtonMaintenance plugin;
    public static YamlConfiguration buttonsFile, configFile;

    public static Map<String, Location> maintenanceButtons = new HashMap<>();
    public static Map<String, Location> restoreButtons = new HashMap<>();
    public static Map<Player, ItemStack[]> inventories = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        loadFiles();
        loadCommands();
        loadEvents();
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[ButtonMaintenance] The plugin has been enabled!"));
    }

    @Override
    public void onDisable() {
        if (!inventories.isEmpty()) {
            for (Map.Entry<Player, ItemStack[]> entry : inventories.entrySet()) {
                if (ButtonMaintenance.configFile.getBoolean("Give player op")) {
                    entry.getKey().setOp(false);
                }
                if (ButtonMaintenance.configFile.getBoolean("Switch player gamemode")) {
                    entry.getKey().setGameMode(GameMode.SURVIVAL);
                }
                if (ButtonMaintenance.configFile.getBoolean("Remember player inventory")) {
                    if (ButtonMaintenance.inventories.containsKey(entry.getKey())) {
                        entry.getKey().getInventory().setContents(ButtonMaintenance.inventories.get(entry.getKey()));
                        ButtonMaintenance.inventories.remove(entry.getKey());
                    }
                }
                if (!ButtonMaintenance.configFile.getStringList("Restore commands").isEmpty()) {
                    for (String cmd : ButtonMaintenance.configFile.getStringList("Restore commands")) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("%player%", entry.getKey().getName()));
                    }
                }
            }
        }
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[ButtonMaintenance] The plugin has been disabled!"));
    }

    @SuppressWarnings("ConstantConditions")
    private void loadCommands() {
        getCommand("getmaintenancebutton").setExecutor(new GetMaintenanceButton());
        getCommand("unregistermaintenancebutton").setExecutor(new UnregisterMaintenanceButton());
        getCommand("getrestorebutton").setExecutor(new GetRestoreButton());
        getCommand("unregisterrestorebutton").setExecutor(new UnregisterRestoreButton());
        getCommand("buttonmaintenance").setExecutor(new MainCmd());
    }

    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new ButtonManager(), this);
    }

    private void loadFiles() {
        loadButtons();
        loadConfigFile();
    }

    private void loadButtons() {
        File file = new File(getDataFolder(), "buttons.yml");
        if (!file.exists()) {
            saveResource("buttons.yml", false);
        }
        buttonsFile = YamlConfiguration.loadConfiguration(file);
        if (buttonsFile.getConfigurationSection("Maintenance buttons") != null) {
            for (String key : buttonsFile.getConfigurationSection("Maintenance buttons").getKeys(false)) {
                maintenanceButtons.put(key, (Location) buttonsFile.get("Maintenance buttons." + key));
            }
        }
        if (buttonsFile.getConfigurationSection("Restore buttons") != null) {
            for (String key : buttonsFile.getConfigurationSection("Restore buttons").getKeys(false)) {
                restoreButtons.put(key, (Location) buttonsFile.get("Restore buttons." + key));
            }
        }
    }

    private void loadConfigFile() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveResource("config.yml", false);
        }
        configFile = YamlConfiguration.loadConfiguration(file);
    }
}
