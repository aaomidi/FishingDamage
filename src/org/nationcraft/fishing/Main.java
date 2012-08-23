package org.nationcraft.fishing;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static final Logger log = Bukkit.getLogger();
    public static Main plugin;
    public int number;

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        getServer().getConsoleSender().sendMessage(String.format("%1$s[%2$s] **%2$s version %3$s has been disabled.**", ChatColor.RED, pdfFile.getName(), pdfFile.getVersion()));
    }

    @Override
    public void onEnable() {
        PluginDescriptionFile pdfFile = getDescription();
        getServer().getConsoleSender().sendMessage(String.format("%1$s[%2$s] **%2$s version %3$s has been enabled.**", ChatColor.GREEN, pdfFile.getName(), pdfFile.getVersion()));
        getConfig().options().copyDefaults(true);
        saveConfig();
        Main.plugin = this;
        getServer().getPluginManager().registerEvents(this, this);
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
    }

    @EventHandler
    public void fishing(PlayerFishEvent e) {
        Player player = e.getPlayer();
        if (e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            if (e.getCaught() instanceof Player) {
                Player target = (Player) e.getCaught();
                target.setHealth(target.getHealth() - this.getConfig().getInt("Damage"));

            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player player = (Player) sender;
        World world = player.getWorld();
        if (commandLabel.equalsIgnoreCase("fdreload")) {

            player.sendMessage(ChatColor.AQUA + "All the configuration has been reloaded");
            this.reloadConfig();
        } else if (commandLabel.equalsIgnoreCase("fdset")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.GOLD + "[Fishing Damage]" + ChatColor.DARK_RED + "Please specify the amount of Damage");
            } else if (args.length == 1) {
                double a = Double.parseDouble((args[0].toString()));//Damage value for the Fishing Rod
                player.sendMessage(ChatColor.GOLD + "[Fishing Damage]" + ChatColor.AQUA + "The Damage was set to: " + (new Double(a / 2)));
                this.getConfig().set("Damage", (Integer.parseInt(args[0].toString())));
                this.saveConfig();
                this.reloadConfig();
            }
        }
        return false;
    }
}
