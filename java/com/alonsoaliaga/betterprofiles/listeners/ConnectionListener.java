package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.others.PlayerData;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
   private BetterProfiles plugin;

   public ConnectionListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      this.loadPlayer(e.getPlayer());
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent e) {
      if (this.plugin.getDataMap().containsKey(e.getPlayer().getUniqueId())) {
         PlayerData playerData = (PlayerData)this.plugin.getDataMap().get(e.getPlayer().getUniqueId());
         if (playerData.isModified()) {
            this.savePlayer(playerData);
            this.plugin.getDataMap().remove(e.getPlayer().getUniqueId());
         }
      }

   }

   public void loadPlayer(Player player) {
      try {
         PreparedStatement preparedStatement = this.plugin.getDatabase().getConnection().prepareStatement("SELECT * FROM " + this.plugin.getDatabase().getTable() + " WHERE uuid=?");
         preparedStatement.setString(1, player.getUniqueId().toString());
         ResultSet resultSet = preparedStatement.executeQuery();
         if (!resultSet.next()) {
            this.registerPlayer(player);
            return;
         }

         this.plugin.getDataMap().put(player.getUniqueId(), new PlayerData(player, resultSet.getInt("profiles_blocked") == 1, resultSet.getInt("profile_blocked") == 1));
      } catch (SQLException var4) {
         LocalUtils.logp("&cCouldn't load player '" + player.getName() + "' from database!");
         var4.printStackTrace();
      }

   }

   public void savePlayer(PlayerData playerData) {
      try {
         PreparedStatement preparedStatement = this.plugin.getDatabase().getConnection().prepareStatement("UPDATE " + this.plugin.getDatabase().getTable() + " SET profiles_blocked=?,profile_blocked=? WHERE uuid=?");
         preparedStatement.setInt(1, playerData.hasProfilesBlocked() ? 1 : 0);
         preparedStatement.setInt(2, playerData.hasProfileBlocked() ? 1 : 0);
         preparedStatement.setString(3, playerData.getPlayer().getUniqueId().toString());
         preparedStatement.executeUpdate();
         playerData.markUpdated();
      } catch (SQLException var3) {
         LocalUtils.logp("&cCouldn't save player '" + playerData.getPlayer().getName() + "' in database!");
         var3.printStackTrace();
      }

   }

   private void registerPlayer(Player player) {
      try {
         PreparedStatement preparedStatement = this.plugin.getDatabase().getConnection().prepareStatement("INSERT INTO " + this.plugin.getDatabase().getTable() + " (uuid) VALUES (?)");
         preparedStatement.setString(1, player.getUniqueId().toString());
         preparedStatement.execute();
         this.plugin.getDataMap().put(player.getUniqueId(), new PlayerData(player, false, false));
      } catch (SQLException var3) {
         LocalUtils.logp("&cCouldn't register player '" + player.getName() + "' in database!");
         var3.printStackTrace();
      }

   }
}
