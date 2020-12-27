package com.alonsoaliaga.betterprofiles.others;

import com.alonsoaliaga.betterprofiles.utils.AlonsoUtils;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {
   private AlonsoUtils.AlonsoPlugin plugin;
   public String noPermission;
   public String viewCommandNoPermission;
   public String viewInteractNoPermission;
   public String invalidTarget;
   public String reloaded;
   public String profileTitle;
   public String profileOnCommandNotEnabled;
   public String profileIsLocked;
   public String pleaseReconnect;
   public String pleaseReconnectOthers;
   public String disabledWorld;
   public String toggleProfilesNoPermission;
   public String toggleProfilesLocked;
   public String toggleProfileNoPermission;
   public String toggleProfilesUnlocked;
   public String toggleProfileLocked;
   public String toggleProfileUnlocked;
   public List<String> adminHelpMessage;
   public List<String> userHelpMessage;

   public Messages(AlonsoUtils.AlonsoPlugin plugin) {
      this.plugin = plugin;
      this.reloadMessages();
   }

   public void reloadMessages() {
      FileConfiguration config = this.plugin.getFiles().getConfig().get();
      this.noPermission = LocalUtils.colorize(config.getString("Messages.No-permission"));
      this.viewCommandNoPermission = LocalUtils.colorize(config.getString("Messages.View-command-no-permission"));
      this.viewInteractNoPermission = LocalUtils.colorize(config.getString("Messages.View-interact-no-permission", "none"));
      if (this.viewInteractNoPermission.equalsIgnoreCase("none")) {
         this.viewInteractNoPermission = null;
      }

      this.invalidTarget = LocalUtils.colorize(config.getString("Messages.Invalid-player"));
      this.reloaded = LocalUtils.colorize(config.getString("Messages.Reloaded"));
      this.profileTitle = LocalUtils.colorize(config.getString("Messages.Profile.Title", "&8Profile: {PLAYER}"));
      this.profileOnCommandNotEnabled = LocalUtils.colorize(config.getString("Messages.Profile-disabled-command"));
      this.profileIsLocked = LocalUtils.colorize(config.getString("Messages.Profile.Profile-is-locked"));
      this.pleaseReconnect = LocalUtils.colorize(config.getString("Messages.Please-reconnect"));
      this.pleaseReconnectOthers = LocalUtils.colorize(config.getString("Messages.Please-reconnect-other"));
      this.disabledWorld = LocalUtils.colorize(config.getString("Messages.Disabled-world"));
      this.toggleProfilesNoPermission = LocalUtils.colorize(config.getString("Messages.Toggle-profiles.No-permission"));
      this.toggleProfilesLocked = LocalUtils.colorize(config.getString("Messages.Toggle-profiles.Profiles-locked"));
      this.toggleProfilesUnlocked = LocalUtils.colorize(config.getString("Messages.Toggle-profiles.Profiles-unlocked"));
      this.toggleProfileNoPermission = LocalUtils.colorize(config.getString("Messages.Toggle-profile.No-permission"));
      this.toggleProfileLocked = LocalUtils.colorize(config.getString("Messages.Toggle-profile.Profile-locked"));
      this.toggleProfileUnlocked = LocalUtils.colorize(config.getString("Messages.Toggle-profile.Profile-unlocked"));
      this.adminHelpMessage = LocalUtils.colorize(config.getStringList("Messages.Help.Admin"));
      this.userHelpMessage = LocalUtils.colorize(config.getStringList("Messages.Help.User"));
   }
}
