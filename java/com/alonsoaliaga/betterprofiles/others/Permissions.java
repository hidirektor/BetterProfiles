package com.alonsoaliaga.betterprofiles.others;

import com.alonsoaliaga.betterprofiles.utils.AlonsoUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Permissions {
   private AlonsoUtils.AlonsoPlugin plugin;
   public String adminPermission;
   public String viewInteractPermission;
   public String viewCommandPermission;
   public String toggleProfilesPermission;
   public String toggleProfilePermission;
   public String bypassViewPermission;
   public String ownProfilePermission;

   public Permissions(AlonsoUtils.AlonsoPlugin plugin) {
      this.plugin = plugin;
      this.reloadMessages();
   }

   public void reloadMessages() {
      FileConfiguration config = this.plugin.getFiles().getConfig().get();
      this.adminPermission = config.getString("Permissions.Admin", "betterprofiles.admin");
      this.viewInteractPermission = config.getString("Permissions.View-interact", "none");
      if (this.viewInteractPermission.equalsIgnoreCase("none")) {
         this.viewInteractPermission = null;
      }

      this.viewCommandPermission = config.getString("Permissions.View-command", "betterprofiles.view.command");
      if (this.viewCommandPermission.equalsIgnoreCase("none")) {
         this.viewCommandPermission = null;
      }

      this.toggleProfilesPermission = config.getString("Permissions.Toggle-view-profiles", "none");
      if (this.toggleProfilesPermission.equalsIgnoreCase("none")) {
         this.toggleProfilesPermission = null;
      }

      this.toggleProfilePermission = config.getString("Permissions.Toggle-profile", "betterprofiles.toggle.profile");
      if (this.toggleProfilePermission.equalsIgnoreCase("none")) {
         this.toggleProfilePermission = null;
      }

      this.bypassViewPermission = config.getString("Permissions.Bypass", "betterprofiles.toggle.bypass");
      this.ownProfilePermission = config.getString("Permissions.Own-profile", "none");
      if (this.ownProfilePermission.equalsIgnoreCase("none")) {
         this.ownProfilePermission = null;
      }

   }

   public void addPermission(String permission, PermissionDefault permissionDefault) {
      try {
         Bukkit.getServer().getPluginManager().addPermission(new Permission(permission, permissionDefault));
      } catch (IllegalArgumentException var4) {
      }

   }
}
