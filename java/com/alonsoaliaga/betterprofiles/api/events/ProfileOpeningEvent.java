package com.alonsoaliaga.betterprofiles.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ProfileOpeningEvent extends PlayerEvent implements Cancellable {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private Player target;
   private ProfileOpeningEvent.Reason reason;
   private boolean cancelled = false;

   public ProfileOpeningEvent(Player player, Player target, ProfileOpeningEvent.Reason reason) {
      super(player);
      this.target = target;
      this.reason = reason;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public Player getTarget() {
      return this.target;
   }

   public ProfileOpeningEvent.Reason getReason() {
      return this.reason;
   }

   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }

   public static enum Reason {
      API,
      INTERACT,
      COMMAND;
   }
}
