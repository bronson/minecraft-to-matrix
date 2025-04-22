package io.papermc.testplugin;

import com.cosium.matrix_communication_client.MatrixResources;
import com.cosium.matrix_communication_client.Message;
import com.cosium.matrix_communication_client.RoomResource;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftToMatrix extends JavaPlugin implements Listener {
  private MatrixResources matrixResources;
  private RoomResource roomResource;

  @Override
  public void onEnable() {
    // creates the config file if it doesn't already exist
    saveDefaultConfig();

    initMatrixClient();

    Bukkit.getPluginManager().registerEvents(this, this);

    // Send startup message to Matrix
    if (roomResource != null) {
      serverEvent("server started");
      getLogger().info("Matrix integration enabled");
    } else {
      getLogger().warning("Matrix couldn't start - check configuration");
    }
  }

  @Override
  public void onDisable() {
    // Send shutdown message to Matrix
    if (roomResource != null) {
      try {
        serverEvent("server is shutting down");
        getLogger().info("Sent shutdown message to Matrix");
      } catch (Exception e) {
        getLogger().warning("Failed to send shutdown message to Matrix");
      }
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    playerEvent(event, "joined the game");
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    playerEvent(event, "left the game");
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    String message = PlainTextComponentSerializer.plainText().serialize(event.message());
    send("<b>" + event.getPlayer().getName() + "</b>: " + escapeHtml(message));
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    String deathMessage = event.getPlayer().getName() + " died";
    if (event.deathMessage() != null) {
      deathMessage = PlainTextComponentSerializer.plainText().serialize(event.deathMessage());
    }
    send("<i>" + escapeHtml(deathMessage) + "</i>", "m.notice");
  }

  private void initMatrixClient() {
    try {
      String server = getConfig().getString("matrix.server");
      String roomId = getConfig().getString("matrix.room");
      String username = getConfig().getString("matrix.username");
      String password = getConfig().getString("matrix.password");

      if (server == null || roomId == null || username == null || password == null) {
        getLogger().severe("Missing Matrix configuration values. Check your config.yml");
        return;
      }

      matrixResources = MatrixResources.factory()
              .builder()
              .uri(server)
              .usernamePassword(username, password)
              .build();

      roomResource = matrixResources.rooms().byId(roomId);
      debug("successfully connected to Matrix room");
    } catch (Exception e) {
      getLogger().severe("Failed to initialize Matrix client: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void serverEvent(String message) {
    send("<b><i>" + escapeHtml(message) + "</i></b>", "m.notice");
  }

  private void playerEvent(PlayerEvent event, String message) {
    send("<i>" + event.getPlayer().getName() + " " + escapeHtml(message) + "</i>", "m.notice");
  }
  
  private void send(String html) {
    send(html, "m.text");
  }

  private void send(String html, String msgType) {
    if (roomResource == null) {
      debug("Room resource is null, can't send: " + html);
      return;
    }

    try {
      roomResource.sendMessage(Message.builder()
          .type(msgType)
          .body(htmlToText(html))
          .formattedBody(html)
          .build());
      debug("Sent to Matrix: " + html);
    } catch (Exception e) {
      getLogger().warning("Failed to send <<" + html + ">> to Matrix: " + e.getMessage());
    }
  }

  private void debug(String msg) {
    getComponentLogger().debug(Component.text(msg));
  }

  private String escapeHtml(String text) {
    if (text == null) {
      return "";
    }

    return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
  }

  private String htmlToText(String html) {
    if (html == null) {
      return "";
    }

    String text = html.replaceAll("<[^>]*>", "");
    return text.replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'");
  }
}
