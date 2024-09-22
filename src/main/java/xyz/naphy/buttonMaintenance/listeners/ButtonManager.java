package xyz.naphy.buttonMaintenance.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.naphy.buttonMaintenance.ButtonMaintenance;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ButtonManager implements Listener {

    @EventHandler
    public void onButtonPlace(BlockPlaceEvent event) {
        if (Objects.requireNonNull(event.getItemInHand().getItemMeta()).getItemName().equals(ChatColor.translateAlternateColorCodes('&', "&cMaintenance button")) && event.getItemInHand().getType().equals(Material.CHERRY_BUTTON)) {
            String id = Objects.requireNonNull(event.getItemInHand().getItemMeta().getLore()).get(3).substring(8);
            try {
                ButtonMaintenance.buttonsFile.set("Maintenance buttons." + id, event.getBlockPlaced().getLocation());
                ButtonMaintenance.buttonsFile.save(new File(ButtonMaintenance.plugin.getDataFolder(), "buttons.yml"));
                ButtonMaintenance.maintenanceButtons.put(id, event.getBlockPlaced().getLocation());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] The &cMaintenance button &7with the ID &b" + id + "&7 has been placed!"));
            event.getPlayer().getInventory().remove(event.getItemInHand());
        }
        else if (Objects.requireNonNull(event.getItemInHand().getItemMeta()).getItemName().equals(ChatColor.translateAlternateColorCodes('&', "&aRestore button")) && event.getItemInHand().getType().equals(Material.POLISHED_BLACKSTONE_BUTTON)) {
            String id = Objects.requireNonNull(event.getItemInHand().getItemMeta().getLore()).get(3).substring(8);
            try {
                ButtonMaintenance.buttonsFile.set("Restore buttons." + id, event.getBlockPlaced().getLocation());
                ButtonMaintenance.buttonsFile.save(new File(ButtonMaintenance.plugin.getDataFolder(), "buttons.yml"));
                ButtonMaintenance.restoreButtons.put(id, event.getBlockPlaced().getLocation());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] The &aRestore button &7with the ID &b" + id + "&7 has been placed!"));
            event.getPlayer().getInventory().remove(event.getItemInHand());
        }
    }

    @EventHandler
    public void onButtonClick(PlayerInteractEvent event) {
        if (Objects.requireNonNull(event.getClickedBlock()).getType() == Material.CHERRY_BUTTON) {
            if (ButtonMaintenance.maintenanceButtons.containsValue(event.getClickedBlock().getLocation())) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player player = event.getPlayer();
                    if (ButtonMaintenance.configFile.getBoolean("Give player op")) {
                        player.setOp(true);
                    }
                    if (ButtonMaintenance.configFile.getBoolean("Switch player gamemode")) {
                        player.setGameMode(GameMode.CREATIVE);
                    }
                    if (ButtonMaintenance.configFile.getBoolean("Remember player inventory")) {
                        if (!ButtonMaintenance.inventories.containsKey(player)) {
                            ButtonMaintenance.inventories.put(player, player.getInventory().getContents());
                            player.getInventory().clear();
                        }
                    }
                    if (!ButtonMaintenance.configFile.getStringList("Maintenance commands").isEmpty()) {
                        for (String cmd : ButtonMaintenance.configFile.getStringList("Maintenance commands")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("%player%", player.getName()));
                        }
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] You are now in &aMaintenance Mode&7!"));
                }
            }
        }
        else if (Objects.requireNonNull(event.getClickedBlock()).getType() == Material.POLISHED_BLACKSTONE_BUTTON) {
            if (ButtonMaintenance.restoreButtons.containsValue(event.getClickedBlock().getLocation())) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player player = event.getPlayer();
                    if (ButtonMaintenance.configFile.getBoolean("Give player op")) {
                        player.setOp(false);
                    }
                    if (ButtonMaintenance.configFile.getBoolean("Switch player gamemode")) {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                    if (ButtonMaintenance.configFile.getBoolean("Remember player inventory")) {
                        if (ButtonMaintenance.inventories.containsKey(player)) {
                            player.getInventory().setContents(ButtonMaintenance.inventories.get(player));
                            ButtonMaintenance.inventories.remove(player);
                        }
                    }
                    if (!ButtonMaintenance.configFile.getStringList("Restore commands").isEmpty()) {
                        for (String cmd : ButtonMaintenance.configFile.getStringList("Restore commands")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("%player%", player.getName()));
                        }
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] You are now out of &cMaintenance Mode&7!"));
                }
            }
        }
    }

    @EventHandler
    public void onButtonDestroy(BlockBreakEvent event) {
        if (ButtonMaintenance.maintenanceButtons.containsValue(event.getBlock().getLocation()) || ButtonMaintenance.restoreButtons.containsValue(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bButtonMaintenance&7] Unregister the button properly to be able to destroy it!\n&7[&bButtonMaintenance&7] Use the command: &b" + (event.getBlock().getType() == Material.CHERRY_BUTTON ? "/unregistermaintenancebutton" : "/unregisterrestorebutton" + "&7!")));
        }
    }

}
