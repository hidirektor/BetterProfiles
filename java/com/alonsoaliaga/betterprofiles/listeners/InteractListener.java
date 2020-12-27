package com.alonsoaliaga.betterprofiles.listeners;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.api.events.ProfileOpeningEvent;
import com.alonsoaliaga.betterprofiles.others.PlayerData;
import com.alonsoaliaga.betterrevive.api.BetterReviveAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {
   private BetterProfiles plugin;

   public InteractListener(BetterProfiles plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   @EventHandler
   public void onRightClickPlayer(PlayerInteractAtEntityEvent e) {
      if (!this.plugin.getPluginUtils().isV1_8()) {
         if (e.getHand() != EquipmentSlot.HAND) {
            return;
         }

         if (this.plugin.preventShield) {
            ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
            if (mainHand != null && mainHand.getType() == Material.SHIELD) {
               return;
            }

            ItemStack offHand = e.getPlayer().getInventory().getItemInOffHand();
            if (offHand != null && offHand.getType() == Material.SHIELD) {
               return;
            }
         }
      }

      if (e.getRightClicked() instanceof Player) {
         Player target = (Player)e.getRightClicked();
         if (this.plugin.betterReviveHooked && BetterReviveAPI.isBleeding(target)) {
            return;
         }

         if (this.plugin.getDataMap().containsKey(target.getUniqueId()) && this.plugin.getDataMap().containsKey(e.getPlayer().getUniqueId())) {
            if (this.plugin.onlySneak && !e.getPlayer().isSneaking()) {
               return;
            }

            if (this.plugin.checkVisibility && !e.getPlayer().canSee(target)) {
               return;
            }

            PlayerData playerData = (PlayerData)this.plugin.getDataMap().get(e.getPlayer().getUniqueId());
            PlayerData targetData = (PlayerData)this.plugin.getDataMap().get(target.getUniqueId());
            if (playerData.hasProfilesBlocked()) {
               return;
            }

            if (this.plugin.permissions.viewInteractPermission != null && !e.getPlayer().hasPermission(this.plugin.permissions.viewInteractPermission)) {
               if (this.plugin.messages.viewInteractNoPermission != null) {
                  e.getPlayer().sendMessage(this.plugin.messages.viewInteractNoPermission);
               }

               return;
            }

            if (targetData.hasProfileBlocked() && !e.getPlayer().hasPermission(this.plugin.permissions.bypassViewPermission)) {
               e.getPlayer().sendMessage(this.plugin.messages.profileIsLocked);
               return;
            }

            if (this.plugin.disabledWorlds.contains(e.getPlayer().getWorld().getName()) && !e.getPlayer().hasPermission(this.plugin.permissions.adminPermission)) {
               e.getPlayer().sendMessage(this.plugin.messages.disabledWorld);
               return;
            }

            ProfileOpeningEvent profileOpeningEvent = new ProfileOpeningEvent(e.getPlayer(), target, ProfileOpeningEvent.Reason.INTERACT);
            this.plugin.getServer().getPluginManager().callEvent(profileOpeningEvent);
            if (profileOpeningEvent.isCancelled()) {
               return;
            }

            if (this.plugin.preventInteract) {
               e.setCancelled(true);
            }

            this.plugin.openProfile(e.getPlayer(), target);
         }
      }

   }
}
