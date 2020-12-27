package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BaseListener implements Listener {
   private BetterProfiles plugin;

   public BaseListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   @EventHandler
   public void on() {
   }
}
