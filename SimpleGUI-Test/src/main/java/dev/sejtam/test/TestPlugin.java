package dev.sejtam.test;

import dev.sejtam.gui.InputInventory;
import dev.sejtam.gui.InventoryManager;
import dev.sejtam.test.gui.FirstGUI;
import dev.sejtam.test.gui.PaginationGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    public void onEnable() {
        InventoryManager.setInstance(this);
        getServer().getPluginManager().registerEvents(new InventoryManager(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return false;

        Player player = (Player) sender;
        if (label.equalsIgnoreCase("gui")) {
            InventoryManager.put(player.getUniqueId(), new FirstGUI(player));
            InventoryManager.openInventory(player.getUniqueId());
        } else if (label.equalsIgnoreCase("pgui")) {
            InventoryManager.put(player.getUniqueId(), new PaginationGUI(player));
            InventoryManager.openInventory(player.getUniqueId());
        } else if (label.equalsIgnoreCase("igui")) {
            new InputInventory(player, null, ((player1, output) -> {
                player1.sendMessage(output);
                return null;
            }));
        }

        return false;
    }

}
