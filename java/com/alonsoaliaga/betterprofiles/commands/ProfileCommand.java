package com.alonsoaliaga.betterprofiles.commands;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.api.events.ProfileOpeningEvent;
import com.alonsoaliaga.betterprofiles.others.PlayerData;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand extends AbstractCommand {
   private BetterProfiles plugin;
   private List<String> emptyList = Collections.emptyList();
   private boolean testServer = false;
   private String testError = LocalUtils.colorize("&cHey {PLAYER}! For security reasons, you can only use commands to modify your player in this test server!");
   private String testReloadError = LocalUtils.colorize("&cHey {PLAYER}! For security reasons, this command is disabled in this test server!");

   public ProfileCommand(BetterProfiles plugin, String command, List<String> aliases) {
      super(command, "/" + command, "Profile command", aliases);
      this.plugin = plugin;
      this.register();
      this.reloadMessages();
   }

   public void reloadMessages() {
   }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      Player player;
      if (args.length >= 1) {
         if (sender instanceof Player) {
            if (this.plugin.permissions.viewCommandPermission != null && !sender.hasPermission(this.plugin.permissions.viewCommandPermission)) {
               sender.sendMessage(this.plugin.messages.viewCommandNoPermission);
               return true;
            }

            if (!this.plugin.profileOnCommand && !sender.hasPermission(this.plugin.permissions.adminPermission)) {
               sender.sendMessage(this.plugin.messages.profileOnCommandNotEnabled);
               return true;
            }

            player = (Player)sender;
            if (this.plugin.disabledWorlds.contains(player.getWorld().getName()) && !player.hasPermission(this.plugin.permissions.adminPermission)) {
               sender.sendMessage(this.plugin.messages.disabledWorld);
               return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
               sender.sendMessage(this.plugin.messages.invalidTarget);
               return true;
            }

            if (this.plugin.checkVisibility && !player.canSee(target)) {
               sender.sendMessage(this.plugin.messages.invalidTarget);
               return true;
            }

            if (!this.plugin.getDataMap().containsKey(target.getUniqueId())) {
               sender.sendMessage(this.plugin.messages.pleaseReconnectOthers);
               return true;
            }

            PlayerData targetData = (PlayerData)this.plugin.getDataMap().get(target.getUniqueId());
            if (targetData.hasProfileBlocked() && !player.hasPermission(this.plugin.permissions.bypassViewPermission)) {
               player.sendMessage(this.plugin.messages.profileIsLocked);
               return true;
            }

            ProfileOpeningEvent profileOpeningEvent = new ProfileOpeningEvent(player, target, ProfileOpeningEvent.Reason.COMMAND);
            this.plugin.getServer().getPluginManager().callEvent(profileOpeningEvent);
            if (profileOpeningEvent.isCancelled()) {
               return true;
            }

            this.plugin.openProfile(player, target);
         } else {
            LocalUtils.send(sender, "&cConsole cannot open profiles..");
         }

         return true;
      } else {
         if (sender instanceof Player) {
            player = (Player)sender;
            if (this.plugin.permissions.ownProfilePermission != null && player.hasPermission(this.plugin.permissions.ownProfilePermission)) {
               player.sendMessage(this.plugin.messages.noPermission);
               return true;
            }

            ProfileOpeningEvent profileOpeningEvent = new ProfileOpeningEvent(player, player, ProfileOpeningEvent.Reason.COMMAND);
            this.plugin.getServer().getPluginManager().callEvent(profileOpeningEvent);
            if (profileOpeningEvent.isCancelled()) {
               return true;
            }

            this.plugin.openProfile(player, player);
         } else {
            LocalUtils.send(sender, "&cConsole doesn't have a profile..");
         }

         return true;
      }
   }

   public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
      if (this.plugin.permissions.viewCommandPermission != null && !sender.hasPermission(this.plugin.permissions.viewCommandPermission)) {
         return this.emptyList;
      } else {
         return !this.plugin.profileOnCommand && !sender.hasPermission(this.plugin.permissions.adminPermission) ? this.emptyList : this.onlinePlayers(sender, args);
      }
   }
}
