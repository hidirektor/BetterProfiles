package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SwapHandListener implements Listener {
   private BetterProfiles plugin;

   public SwapHandListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onSwapHand(final PlayerSwapHandItemsEvent e) {
      if (this.plugin.getOpenedProfilesMap().containsKey(e.getPlayer().getUniqueId())) {
         final HashMap<UUID, Inventory> profileMap = (HashMap)this.plugin.getOpenedProfilesMap().get(e.getPlayer().getUniqueId());
         (new BukkitRunnable() {
            public void run() {
               ItemStack mainHandItem = e.getPlayer().getInventory().getItemInMainHand();
               ItemStack offHandItem = e.getPlayer().getInventory().getItemInOffHand();
               mainHandItem = mainHandItem != null && mainHandItem.getType() != Material.AIR ? mainHandItem : SwapHandListener.this.plugin.noMainHandItem;
               offHandItem = offHandItem != null && offHandItem.getType() != Material.AIR ? offHandItem : SwapHandListener.this.plugin.noOffHandItem;
               Iterator iterator = profileMap.entrySet().iterator();

               while(iterator.hasNext()) {
                  Entry<UUID, Inventory> entry = (Entry)iterator.next();
                  Player viewer = Bukkit.getPlayer((UUID)entry.getKey());
                  if (viewer == null) {
                     iterator.remove();
                  } else {
                     SwapHandListener.this.addItem(viewer, (Inventory)entry.getValue(), SwapHandListener.this.plugin.mainHandSlot, mainHandItem, SwapHandListener.this.plugin.noMainHandHiddenItem, SwapHandListener.this.plugin.noMainHandItemPermission);
                     SwapHandListener.this.addItem(viewer, (Inventory)entry.getValue(), SwapHandListener.this.plugin.offHandSlot, offHandItem, SwapHandListener.this.plugin.noOffHandHiddenItem, SwapHandListener.this.plugin.noOffHandItemPermission);
                  }
               }

            }
         }).runTaskLater(this.plugin, 1L);
      }

   }

   private void addItem(Player viewer, Inventory profileInventory, int slot, ItemStack itemToDisplay, ItemStack hiddenItem, String permission) {
      profileInventory.setItem(slot, permission != null && !viewer.hasPermission(permission) ? hiddenItem : itemToDisplay);
   }
}