package com.alonsoaliaga.betterprofiles.libraries.armorequipevent;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {
   private final List<String> blockedMaterials;

   public ArmorListener(List<String> blockedMaterials) {
      this.blockedMaterials = blockedMaterials;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public final void inventoryClick(InventoryClickEvent e) {
      boolean shift = false;
      boolean numberkey = false;
      if (!e.isCancelled()) {
         if (e.getAction() != InventoryAction.NOTHING) {
            if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
               shift = true;
            }

            if (e.getClick().equals(ClickType.NUMBER_KEY)) {
               numberkey = true;
            }

            if (e.getSlotType() == SlotType.ARMOR || e.getSlotType() == SlotType.QUICKBAR || e.getSlotType() == SlotType.CONTAINER) {
               if (e.getClickedInventory() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                  if (e.getInventory().getType().equals(InventoryType.CRAFTING) || e.getInventory().getType().equals(InventoryType.PLAYER)) {
                     if (e.getWhoClicked() instanceof Player) {
                        ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
                        if (shift || newArmorType == null || e.getRawSlot() == newArmorType.getSlot()) {
                           if (shift) {
                              newArmorType = ArmorType.matchType(e.getCurrentItem());
                              if (newArmorType != null) {
                                 boolean equipping = true;
                                 if (e.getRawSlot() == newArmorType.getSlot()) {
                                    equipping = false;
                                 }

                                 label167: {
                                    if (newArmorType.equals(ArmorType.HELMET)) {
                                       if (equipping) {
                                          if (isAirOrNull(e.getWhoClicked().getInventory().getHelmet())) {
                                             break label167;
                                          }
                                       } else if (!isAirOrNull(e.getWhoClicked().getInventory().getHelmet())) {
                                          break label167;
                                       }
                                    }

                                    if (newArmorType.equals(ArmorType.CHESTPLATE)) {
                                       if (equipping) {
                                          if (isAirOrNull(e.getWhoClicked().getInventory().getChestplate())) {
                                             break label167;
                                          }
                                       } else if (!isAirOrNull(e.getWhoClicked().getInventory().getChestplate())) {
                                          break label167;
                                       }
                                    }

                                    if (newArmorType.equals(ArmorType.LEGGINGS)) {
                                       if (equipping) {
                                          if (isAirOrNull(e.getWhoClicked().getInventory().getLeggings())) {
                                             break label167;
                                          }
                                       } else if (!isAirOrNull(e.getWhoClicked().getInventory().getLeggings())) {
                                          break label167;
                                       }
                                    }

                                    if (!newArmorType.equals(ArmorType.BOOTS)) {
                                       return;
                                    }

                                    if (equipping) {
                                       if (!isAirOrNull(e.getWhoClicked().getInventory().getBoots())) {
                                          return;
                                       }
                                    } else if (isAirOrNull(e.getWhoClicked().getInventory().getBoots())) {
                                       return;
                                    }
                                 }

                                 ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)e.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
                                 Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                                 if (armorEquipEvent.isCancelled()) {
                                    e.setCancelled(true);
                                 }
                              }
                           } else {
                              ItemStack newArmorPiece = e.getCursor();
                              ItemStack oldArmorPiece = e.getCurrentItem();
                              if (numberkey) {
                                 if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                                    ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                                    if (!isAirOrNull(hotbarItem)) {
                                       newArmorType = ArmorType.matchType(hotbarItem);
                                       newArmorPiece = hotbarItem;
                                       oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                                    } else {
                                       newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
                                    }
                                 }
                              } else if (isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem())) {
                                 newArmorType = ArmorType.matchType(e.getCurrentItem());
                              }

                              if (newArmorType != null && e.getRawSlot() == newArmorType.getSlot()) {
                                 ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
                                 if (e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) {
                                    method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                                 }

                                 ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)e.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
                                 Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                                 if (armorEquipEvent.isCancelled()) {
                                    e.setCancelled(true);
                                 }
                              }
                           }

                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void playerInteractEvent(PlayerInteractEvent e) {
      if (!e.useItemInHand().equals(Result.DENY)) {
         if (e.getAction() != Action.PHYSICAL) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
               Player player = e.getPlayer();
               if (!e.useInteractedBlock().equals(Result.DENY) && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) {
                  Material mat = e.getClickedBlock().getType();
                  Iterator var4 = this.blockedMaterials.iterator();

                  while(var4.hasNext()) {
                     String s = (String)var4.next();
                     if (mat.name().equalsIgnoreCase(s)) {
                        return;
                     }
                  }
               }

               ArmorType newArmorType = ArmorType.matchType(e.getItem());
               if (newArmorType != null && (newArmorType.equals(ArmorType.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet()) || newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate()) || newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings()) || newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots()))) {
                  ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), (ItemStack)null, e.getItem());
                  Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                  if (armorEquipEvent.isCancelled()) {
                     e.setCancelled(true);
                     player.updateInventory();
                  }
               }
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void inventoryDrag(InventoryDragEvent event) {
      ArmorType type = ArmorType.matchType(event.getOldCursor());
      if (!event.getRawSlots().isEmpty()) {
         if (type != null && type.getSlot() == (Integer)event.getRawSlots().stream().findFirst().orElse(0)) {
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player)event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, (ItemStack)null, event.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
               event.setResult(Result.DENY);
               event.setCancelled(true);
            }
         }

      }
   }

   @EventHandler
   public void itemBreakEvent(PlayerItemBreakEvent e) {
      ArmorType type = ArmorType.matchType(e.getBrokenItem());
      if (type != null) {
         Player p = e.getPlayer();
         ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), (ItemStack)null);
         Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
         if (armorEquipEvent.isCancelled()) {
            ItemStack i = e.getBrokenItem().clone();
            i.setAmount(1);
            i.setDurability((short)(i.getDurability() - 1));
            if (type.equals(ArmorType.HELMET)) {
               p.getInventory().setHelmet(i);
            } else if (type.equals(ArmorType.CHESTPLATE)) {
               p.getInventory().setChestplate(i);
            } else if (type.equals(ArmorType.LEGGINGS)) {
               p.getInventory().setLeggings(i);
            } else if (type.equals(ArmorType.BOOTS)) {
               p.getInventory().setBoots(i);
            }
         }
      }

   }

   @EventHandler
   public void playerDeathEvent(PlayerDeathEvent e) {
      Player p = e.getEntity();
      if (!e.getKeepInventory()) {
         ItemStack[] var3 = p.getInventory().getArmorContents();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ItemStack i = var3[var5];
            if (!isAirOrNull(i)) {
               Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, (ItemStack)null));
            }
         }

      }
   }

   public static boolean isAirOrNull(ItemStack item) {
      return item == null || item.getType().equals(Material.AIR);
   }
}
