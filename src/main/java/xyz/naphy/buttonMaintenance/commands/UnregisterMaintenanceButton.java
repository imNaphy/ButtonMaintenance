package xyz.naphy.buttonMaintenance.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.naphy.buttonMaintenance.ButtonMaintenance;

import java.io.File;
import java.io.IOException;

public class UnregisterMaintenanceButton implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] You need to provide an ID!\n&7[&bButtonMaintenance&7] Use command: &b/unregistermaintenancebutton <ID>&7!"));
            return true;
        }
        if (!ButtonMaintenance.maintenanceButtons.containsKey(args[0])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] The provided ID does not exist in the database!"));
            return true;
        }
        Location blockLoc = ButtonMaintenance.maintenanceButtons.get(args[0]);
        blockLoc.getBlock().setType(Material.AIR);
        try {
            ButtonMaintenance.buttonsFile.set("Maintenance buttons." + args[0], null);
            ButtonMaintenance.buttonsFile.save(new File(ButtonMaintenance.plugin.getDataFolder(), "buttons.yml"));
            ButtonMaintenance.maintenanceButtons.remove(args[0]);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] &7The maintenance button with the ID &b" + args[0] + " &7has been unregistered!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
