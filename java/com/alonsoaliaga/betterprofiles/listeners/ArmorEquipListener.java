package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.libraries.armorequipevent.ArmorEquipEvent;
import com.alonsoaliaga.betterprofiles.libraries.armorequipevent.ArmorType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorEquipListener implements Listener {
   private BetterProfiles plugin;

   public ArmorEquipListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onArmorEquip(final ArmorEquipEvent e) {
      if (this.plugin.getOpenedProfilesMap().containsKey(e.getPlayer().getUniqueId())) {
         final HashMap<UUID, Inventory> profileMap = (HashMap)this.plugin.getOpenedProfilesMap().get(e.getPlayer().getUniqueId());
         ItemStack itemToReplace = e.getNewArmorPiece();
         Iterator iterator;
         Entry entry;
         Player viewer;
         if (e.getType() == ArmorType.HELMET) {
            if (this.plugin.helmetSlot == -1) {
               return;
            }

            itemToReplace = itemToReplace != null && itemToReplace.getType() != Material.AIR ? itemToReplace : this.plugin.noHelmetItem;
            iterator = profileMap.entrySet().iterator();

            while(iterator.hasNext()) {
               entry = (Entry)iterator.next();
               viewer = Bukkit.getPlayer((UUID)entry.getKey());
               if (viewer == null) {
                  iterator.remove();
               } else {
                  this.addItem(viewer, (Inventory)entry.getValue(), this.plugin.helmetSlot, itemToReplace, this.plugin.noHelmetHiddenItem, this.plugin.noHelmetItemPermission);
               }
            }
         } else if (e.getType() == ArmorType.CHESTPLATE) {
            if (this.plugin.chestplateSlot == -1) {
               return;
            }

            itemToReplace = itemToReplace != null && itemToReplace.getType() != Material.AIR ? itemToReplace : this.plugin.noChestplateItem;
            iterator = profileMap.entrySet().iterator();

            while(iterator.hasNext()) {
               entry = (Entry)iterator.next();
               viewer = Bukkit.getPlayer((UUID)entry.getKey());
               if (viewer == null) {
                  iterator.remove();
               } else {
                  this.addItem(viewer, (Inventory)entry.getValue(), this.plugin.chestplateSlot, itemToReplace, this.plugin.noChestplateHiddenItem, this.plugin.noChestplateItemPermission);
               }
            }
         } else if (e.getType() == ArmorType.LEGGINGS) {
            if (this.plugin.leggingsSlot == -1) {
               return;
            }

            itemToReplace = itemToReplace != null && itemToReplace.getType() != Material.AIR ? itemToReplace : this.plugin.noLeggingsItem;
            iterator = profileMap.entrySet().iterator();

            while(iterator.hasNext()) {
               entry = (Entry)iterator.next();
               viewer = Bukkit.getPlayer((UUID)entry.getKey());
               if (viewer == null) {
                  iterator.remove();
               } else {
                  this.addItem(viewer, (Inventory)entry.getValue(), this.plugin.leggingsSlot, itemToReplace, this.plugin.noLeggingsHiddenItem, this.plugin.noLeggingsItemPermission);
               }
            }
         } else if (e.getType() == ArmorType.BOOTS) {
            if (this.plugin.bootsSlot == -1) {
               return;
            }

            itemToReplace = itemToReplace != null && itemToReplace.getType() != Material.AIR ? itemToReplace : this.plugin.noBootsItem;
            iterator = profileMap.entrySet().iterator();

            while(iterator.hasNext()) {
               entry = (Entry)iterator.next();
               viewer = Bukkit.getPlayer((UUID)entry.getKey());
               if (viewer == null) {
                  iterator.remove();
               } else {
                  this.addItem(viewer, (Inventory)entry.getValue(), this.plugin.bootsSlot, itemToReplace, this.plugin.noBootsHiddenItem, this.plugin.noBootsItemPermission);
               }
            }
         }

         if (this.plugin.mainHandSlot == -1 && this.plugin.offHandSlot == -1) {
            return;
         }

         (new BukkitRunnable() {
            public void run() {
               ItemStack mainHandItem;
               Iterator iteratorx;
               Entry entryx;
               Player viewer;
               if (ArmorEquipListener.this.plugin.getPluginUtils().isV1_8()) {
                  if (ArmorEquipListener.this.plugin.mainHandSlot == -1) {
                     return;
                  }

                  mainHandItem = e.getPlayer().getInventory().getItemInHand();
                  mainHandItem = mainHandItem != null && mainHandItem.getType() != Material.AIR ? mainHandItem : ArmorEquipListener.this.plugin.noMainHandItem;
                  iteratorx = profileMap.entrySet().iterator();

                  while(iteratorx.hasNext()) {
                     entryx = (Entry)iteratorx.next();
                     viewer = Bukkit.getPlayer((UUID)entryx.getKey());
                     if (viewer == null) {
                        iteratorx.remove();
                     } else {
                        ArmorEquipListener.this.addItem(viewer, (Inventory)entryx.getValue(), ArmorEquipListener.this.plugin.mainHandSlot, mainHandItem, ArmorEquipListener.this.plugin.noMainHandHiddenItem, ArmorEquipListener.this.plugin.noMainHandItemPermission);
                     }
                  }
               } else if (ArmorEquipListener.this.plugin.mainHandSlot == -1) {
                  mainHandItem = e.getPlayer().getInventory().getItemInOffHand();
                  mainHandItem = mainHandItem != null && mainHandItem.getType() != Material.AIR ? mainHandItem : ArmorEquipListener.this.plugin.noOffHandItem;
                  iteratorx = profileMap.entrySet().iterator();

                  while(iteratorx.hasNext()) {
                     entryx = (Entry)iteratorx.next();
                     viewer = Bukkit.getPlayer((UUID)entryx.getKey());
                     if (viewer == null) {
                        iteratorx.remove();
                     } else {
                        ArmorEquipListener.this.addItem(viewer, (Inventory)entryx.getValue(), ArmorEquipListener.this.plugin.offHandSlot, mainHandItem, ArmorEquipListener.this.plugin.noOffHandHiddenItem, ArmorEquipListener.this.plugin.noOffHandItemPermission);
                     }
                  }
               } else if (ArmorEquipListener.this.plugin.offHandSlot == -1) {
                  mainHandItem = e.getPlayer().getInventory().getItemInMainHand();
                  mainHandItem = mainHandItem != null && mainHandItem.getType() != Material.AIR ? mainHandItem : ArmorEquipListener.this.plugin.noMainHandItem;
                  iteratorx = profileMap.entrySet().iterator();

                  while(iteratorx.hasNext()) {
                     entryx = (Entry)iteratorx.next();
                     viewer = Bukkit.getPlayer((UUID)entryx.getKey());
                     if (viewer == null) {
                        iteratorx.remove();
                     } else {
                        ArmorEquipListener.this.addItem(viewer, (Inventory)entryx.getValue(), ArmorEquipListener.this.plugin.mainHandSlot, mainHandItem, ArmorEquipListener.this.plugin.noMainHandHiddenItem, ArmorEquipListener.this.plugin.noMainHandItemPermission);
                     }
                  }
               } else {
                  mainHandItem = e.getPlayer().getInventory().getItemInMainHand();
                  mainHandItem = mainHandItem != null && mainHandItem.getType() != Material.AIR ? mainHandItem : ArmorEquipListener.this.plugin.noMainHandItem;
                  ItemStack offHandItem = e.getPlayer().getInventory().getItemInOffHand();
                  offHandItem = offHandItem != null && offHandItem.getType() != Material.AIR ? offHandItem : ArmorEquipListener.this.plugin.noOffHandItem;
                  Iterator iterator = profileMap.entrySet().iterator();

                  while(iterator.hasNext()) {
                     Entry<UUID, Inventory> entry = (Entry)iterator.next();
                     Player viewerx = Bukkit.getPlayer((UUID)entry.getKey());
                     if (viewerx == null) {
                        iterator.remove();
                     } else {
                        ArmorEquipListener.this.addItem(viewerx, (Inventory)entry.getValue(), ArmorEquipListener.this.plugin.mainHandSlot, mainHandItem, ArmorEquipListener.this.plugin.noMainHandHiddenItem, ArmorEquipListener.this.plugin.noMainHandItemPermission);
                        ArmorEquipListener.this.addItem(viewerx, (Inventory)entry.getValue(), ArmorEquipListener.this.plugin.offHandSlot, offHandItem, ArmorEquipListener.this.plugin.noOffHandHiddenItem, ArmorEquipListener.this.plugin.noOffHandItemPermission);
                     }
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
