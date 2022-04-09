package me.clowr__.antispammer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public final class AntiSpammer extends JavaPlugin  {



    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("cEnable").setExecutor(new CoolDownRun(this));
        getCommand("cDisable").setExecutor(new CoolDownRun(this));

        getServer().getPluginManager().registerEvents(new CoolDownRun(this) , this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out .println("Plugin disabled");
    }



}
