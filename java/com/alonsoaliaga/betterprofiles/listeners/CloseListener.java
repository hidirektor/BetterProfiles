package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.others.ProfileHolder;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class CloseListener implements Listener {
   private BetterProfiles plugin;
   private Sound closeSound;

   public CloseListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
      this.closeSound = LocalUtils.getSound(this.plugin.getFiles().getConfig().get().getString("Options.Sounds.Close", "CHEST_CLOSE"));
   }

   @EventHandler
   public void onClose(InventoryCloseEvent e) {
      if (e.getInventory().getHolder() instanceof ProfileHolder) {
         ProfileHolder profileHolder = (ProfileHolder)e.getInventory().getHolder();
         if (profileHolder.getType() == 0 && this.plugin.getOpenedProfilesMap().containsKey(profileHolder.getOwnerUUID())) {
            HashMap<UUID, Inventory> profileMap = (HashMap)this.plugin.getOpenedProfilesMap().get(profileHolder.getOwnerUUID());
            profileMap.remove(e.getPlayer().getUniqueId());
            if (profileMap.isEmpty()) {
               this.plugin.getOpenedProfilesMap().remove(profileHolder.getOwnerUUID());
            }

            if (this.closeSound != null) {
               Player player = (Player)e.getPlayer();
               player.playSound(player.getLocation(), this.closeSound, 1.0F, 1.0F);
            }
         }
      }

   }
}
