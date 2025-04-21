package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftToMatrix extends JavaPlugin implements Listener {
  @Override
  public void onEnable() {
    // creates the config file if it doesn't already exist
    saveDefaultConfig();

    debug("Server is: " + getConfig().getString("matrix.server"));
    debug("Room is: " + getConfig().getString("matrix.room"));
    debug("User is: " + getConfig().getString("matrix.username"));
    debug("Password is: " + getConfig().getString("matrix.password"));

//    getConfig().set("gabage", "I<3TRASH");
//    saveConfig();

    Bukkit.getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
    debug("Sent Hello to " +  event.getPlayer().getName());
  }

  @SuppressWarnings("unused")
  public void debug(Component msg) {
    // TODO: change to `debug` before release
    getComponentLogger().info(msg);
  }

  public void debug(String msg) {
    // TODO: change to `debug` before release
    getComponentLogger().info(Component.text(msg));
  }
}