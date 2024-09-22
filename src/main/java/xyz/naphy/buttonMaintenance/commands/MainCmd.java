package xyz.naphy.buttonMaintenance.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import xyz.naphy.buttonMaintenance.ButtonMaintenance;

import java.io.File;

public class MainCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] Plugin version &b1.0&7, created by &bnaphy&7.\n&7[&bButtonMaintenance&7] &7Reload the configuration using &b/buttonmaintenance reload&7."));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            File file = new File(ButtonMaintenance.plugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                ButtonMaintenance.plugin.saveResource("config.yml", false);
            }
            ButtonMaintenance.configFile = YamlConfiguration.loadConfiguration(file);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] The config has been reloaded &asuccessfully&7!"));
        }
        return true;
    }
}
