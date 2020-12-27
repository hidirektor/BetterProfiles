package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.others.ItemData;
import com.alonsoaliaga.betterprofiles.others.ProfileHolder;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickListener implements Listener {
   private BetterProfiles plugin;

   public ClickListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   @EventHandler
   public void onClick(InventoryClickEvent e) {
      if (e.getInventory().getHolder() instanceof ProfileHolder) {
         e.setCancelled(true);
         ProfileHolder profileHolder = (ProfileHolder)e.getInventory().getHolder();
         if (profileHolder.getType() == 0) {
            if (e.getRawSlot() == this.plugin.closeSlot) {
               e.getWhoClicked().closeInventory();
               String cmd = this.plugin.getConfig().getString("Items.Close.Command").replace("%player%", e.getWhoClicked().getName());
               Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            } else if (this.plugin.getItemsMap().containsKey(e.getRawSlot())) {
               LinkedList<ItemData> itemList = (LinkedList)this.plugin.getItemsMap().get(e.getRawSlot());
               Player player = (Player)e.getWhoClicked();
               Iterator var5 = itemList.iterator();

               while(true) {
                  ItemData itemData;
                  do {
                     if (!var5.hasNext()) {
                        return;
                     }

                     itemData = (ItemData)var5.next();
                     if (itemData.hasPlaceholderCondition()) {
                        if (!itemData.hasPermission() || player.hasPermission(itemData.getPermission())) {
                           Player profileOwner = Bukkit.getPlayer(profileHolder.getOwnerUUID());
                           if (profileOwner != null && itemData.checkPlaceholder(profileOwner)) {
                              if (itemData.hasCloseOnClick()) {
                                 player.closeInventory();
                              }

                              if (itemData.hasCommands()) {
                                 Iterator var11 = itemData.getCommands().iterator();

                                 while(var11.hasNext()) {
                                    String command = (String)var11.next();
                                    this.plugin.getServer().dispatchCommand(player, command.replace("{PLAYER}", player.getName()).replace("{TARGET}", profileHolder.getOwnerName()).replace("{PLAYER_WORLD}", player.getWorld().getName()).replace("{PLAYER_UUID}", player.getUniqueId().toString()).replace("{TARGET_UUID}", profileHolder.getOwnerUUID().toString()));
                                 }
                              }

                              return;
                           }
                        }

                        return;
                     }
                  } while(itemData.hasPermission() && !player.hasPermission(itemData.getPermission()));

                  Iterator var7;
                  String command;
                  if (itemData.isPrivateItem()) {
                     if (player.getUniqueId().equals(profileHolder.getOwnerUUID())) {
                        if (itemData.hasCloseOnClick()) {
                           player.closeInventory();
                        }

                        if (itemData.hasCommands()) {
                           var7 = itemData.getCommands().iterator();

                           while(var7.hasNext()) {
                              command = (String)var7.next();
                              this.plugin.getServer().dispatchCommand(player, command.replace("{PLAYER}", player.getName()).replace("{TARGET}", profileHolder.getOwnerName()).replace("{PLAYER_WORLD}", player.getWorld().getName()).replace("{PLAYER_UUID}", player.getUniqueId().toString()).replace("{TARGET_UUID}", profileHolder.getOwnerUUID().toString()));
                           }
                        }
                        break;
                     }
                  } else {
                     if (!itemData.isViewerItem()) {
                        if (itemData.hasCloseOnClick()) {
                           player.closeInventory();
                        }

                        if (itemData.hasCommands()) {
                           var7 = itemData.getCommands().iterator();

                           while(var7.hasNext()) {
                              command = (String)var7.next();
                              this.plugin.getServer().dispatchCommand(player, command.replace("{PLAYER}", player.getName()).replace("{TARGET}", profileHolder.getOwnerName()).replace("{PLAYER_WORLD}", player.getWorld().getName()).replace("{PLAYER_UUID}", player.getUniqueId().toString()).replace("{TARGET_UUID}", profileHolder.getOwnerUUID().toString()));
                           }
                        }
                        break;
                     }

                     if (!player.getUniqueId().equals(profileHolder.getOwnerUUID())) {
                        if (itemData.hasCloseOnClick()) {
                           player.closeInventory();
                        }

                        if (itemData.hasCommands()) {
                           var7 = itemData.getCommands().iterator();

                           while(var7.hasNext()) {
                              command = (String)var7.next();
                              this.plugin.getServer().dispatchCommand(player, command.replace("{PLAYER}", player.getName()).replace("{TARGET}", profileHolder.getOwnerName()).replace("{PLAYER_WORLD}", player.getWorld().getName()).replace("{PLAYER_UUID}", player.getUniqueId().toString()).replace("{TARGET_UUID}", profileHolder.getOwnerUUID().toString()));
                           }
                        }
                        break;
                     }
                  }
               }
            }
         }
      }

   }
}
