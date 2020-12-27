package com.alonsoaliaga.betterprofiles.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class ProfileOpenEvent extends Event {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private Player viewer;
   private Player target;
   private Inventory profileInventory;
   private boolean fillEmptySlots;

   public ProfileOpenEvent(Player viewer, Player target, Inventory profileInventory, boolean fillEmptySlots) {
      this.viewer = viewer;
      this.target = target;
      this.profileInventory = profileInventory;
      this.fillEmptySlots = fillEmptySlots;
   }

   public Player getViewer() {
      return this.viewer;
   }

   public Player getTarget() {
      return this.target;
   }

   public Inventory getProfileInventory() {
      return this.profileInventory;
   }

   public boolean isFillEmptySlots() {
      return this.fillEmptySlots;
   }

   public void setFillEmptySlots(boolean fillEmptySlots) {
      this.fillEmptySlots = fillEmptySlots;
   }

   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }
}
