package xyz.naphy.buttonMaintenance.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.naphy.buttonMaintenance.ButtonMaintenance;

import java.util.Arrays;

public class GetMaintenanceButton implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] You need to provide an ID!\n&7[&bButtonMaintenance&7] Use command: &b/getmaintenancebutton <ID>&7!"));
            return true;
        }
        if (ButtonMaintenance.maintenanceButtons.containsKey(args[0])) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] The provided ID has already been used!"));
            return true;
        }
        player.getInventory().addItem(getMaintenanceButton(args[0]));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] You were given a &cMaintenance button &7with the ID &b" + args[0] + "&7!"));
        return true;
    }

    public static ItemStack getMaintenanceButton(String id) {
        ItemStack item = new ItemStack(Material.CHERRY_BUTTON, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setItemName(ChatColor.translateAlternateColorCodes('&', "&cMaintenance button"));
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Place this button somewhere and"), ChatColor.translateAlternateColorCodes('&', "&7it will get registered."), " ", ChatColor.translateAlternateColorCodes('&', "&7ID: &b" + id)));
        item.setItemMeta(meta);
        return item;
    }
}
