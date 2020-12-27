package com.alonsoaliaga.betterprofiles.others;

import org.bukkit.entity.Player;

public class PlayerData {
   private Player player;
   private boolean initialStateProfiles;
   private boolean blockedProfiles;
   private boolean initialStateProfile;
   private boolean blockedProfile;

   public PlayerData(Player player, boolean initialStateProfiles, boolean initialStateProfile) {
      this.player = player;
      this.initialStateProfiles = initialStateProfiles;
      this.blockedProfiles = initialStateProfiles;
      this.initialStateProfile = initialStateProfile;
      this.blockedProfile = initialStateProfile;
   }

   public Player getPlayer() {
      return this.player;
   }

   public void setProfileBlocked(boolean blocked) {
      this.blockedProfile = blocked;
   }

   public void setProfilesBlocked(boolean blocked) {
      this.blockedProfiles = blocked;
   }

   public boolean hasProfilesBlocked() {
      return this.blockedProfiles;
   }

   public boolean hasProfileBlocked() {
      return this.blockedProfile;
   }

   public void markUpdated() {
      this.initialStateProfiles = this.blockedProfiles;
      this.initialStateProfile = this.blockedProfile;
   }

   public boolean isModified() {
      return this.initialStateProfiles != this.blockedProfiles || this.initialStateProfile != this.blockedProfile;
   }
}
