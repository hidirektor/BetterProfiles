package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ConsumeListener implements Listener {
   private BetterProfiles plugin;

   public ConsumeListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   @EventHandler
   public void onConsume(final PlayerItemConsumeEvent e) {
      if (this.plugin.getOpenedProfilesMap().containsKey(e.getPlayer().getUniqueId())) {
         final HashMap<UUID, Inventory> profileMap = (HashMap)this.plugin.getOpenedProfilesMap().get(e.getPlayer().getUniqueId());
         (new BukkitRunnable() {
            public void run() {
               ItemStack mainHandItem = e.getPlayer().getInventory().getItemInMainHand();
               ItemStack offHandItem = e.getPlayer().getInventory().getItemInOffHand();
               mainHandItem = mainHandItem != null && mainHandItem.getType() != Material.AIR ? mainHandItem : ConsumeListener.this.plugin.noMainHandItem;
               offHandItem = offHandItem != null && offHandItem.getType() != Material.AIR ? offHandItem : ConsumeListener.this.plugin.noOffHandItem;
               Iterator var3 = profileMap.values().iterator();

               while(var3.hasNext()) {
                  Inventory profileInventory = (Inventory)var3.next();
                  profileInventory.setItem(ConsumeListener.this.plugin.mainHandSlot, mainHandItem);
                  profileInventory.setItem(ConsumeListener.this.plugin.offHandSlot, offHandItem);
               }

            }
         }).runTaskLater(this.plugin, 1L);
      }

   }
}
