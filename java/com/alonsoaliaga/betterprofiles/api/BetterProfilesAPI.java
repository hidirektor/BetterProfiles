package com.alonsoaliaga.betterprofiles.api;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.api.events.ProfileOpeningEvent;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BetterProfilesAPI {
   @Nullable
   public static Inventory openProfile(Player viewer, Player target) {
      if (viewer != null && viewer.isOnline() && target != null && target.isOnline()) {
         ProfileOpeningEvent profileOpeningEvent = new ProfileOpeningEvent(viewer, target, ProfileOpeningEvent.Reason.API);
         Bukkit.getPluginManager().callEvent(profileOpeningEvent);
         return profileOpeningEvent.isCancelled() ? null : BetterProfiles.getInstance().openProfile(viewer, target);
      } else {
         return null;
      }
   }
}
