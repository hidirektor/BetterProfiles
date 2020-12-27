package com.alonsoaliaga.betterprofiles.commands;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.api.events.ProfileOpeningEvent;
import com.alonsoaliaga.betterprofiles.others.PlayerData;
import com.alonsoaliaga.betterprofiles.others.Sounds;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand extends AbstractCommand {
   private BetterProfiles plugin;
   private List<String> adminList = Arrays.asList("view", "toggle", "reload");
   private List<String> userList = Arrays.asList("view", "toggle");
   private List<String> toggleList = Arrays.asList("viewer", "profile");
   private List<String> emptyList = Collections.emptyList();
   private boolean testServer = false;
   private String testError = LocalUtils.colorize("&cHey {PLAYER}! For security reasons, you can only use commands to modify your player in this test server!");
   private String testReloadError = LocalUtils.colorize("&cHey {PLAYER}! For security reasons, this command is disabled in this test server!");

   public MainCommand(BetterProfiles plugin, String command, List<String> aliases) {
      super(command, "/" + command, "BetterProfiles main command", aliases);
      this.plugin = plugin;
      this.register();
   }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      Player player;
      if (args.length >= 1) {
         if (args[0].equalsIgnoreCase("view")) {
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

               if (args.length >= 2) {
                  Player target = Bukkit.getPlayer(args[1]);
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
                  return true;
               }

               LocalUtils.send(sender, "&cUse: /betterprofiles view <player>");
            } else {
               LocalUtils.send(sender, "&cConsole cannot open profiles..");
            }

            return true;
         } else if (args[0].equalsIgnoreCase("toggle")) {
            if (sender instanceof Player) {
               player = (Player)sender;
               if (!this.plugin.getDataMap().containsKey(player.getUniqueId())) {
                  player.sendMessage(this.plugin.messages.pleaseReconnect);
                  return true;
               }

               PlayerData playerData = (PlayerData)this.plugin.getDataMap().get(player.getUniqueId());
               if (args.length >= 2) {
                  if (args[1].equalsIgnoreCase("viewer")) {
                     if (this.plugin.permissions.toggleProfilesPermission != null && !player.hasPermission(this.plugin.permissions.toggleProfilesPermission)) {
                        player.sendMessage(this.plugin.messages.toggleProfilesNoPermission);
                        return true;
                     }

                     if (playerData.hasProfilesBlocked()) {
                        playerData.setProfilesBlocked(false);
                        player.sendMessage(this.plugin.messages.toggleProfilesUnlocked);
                     } else {
                        playerData.setProfilesBlocked(true);
                        player.sendMessage(this.plugin.messages.toggleProfilesLocked);
                     }

                     player.playSound(player.getLocation(), Sounds.CLICK_ON.getSound(), 1.0F, 1.0F);
                     return true;
                  }

                  if (args[1].equalsIgnoreCase("profile")) {
                     if (this.plugin.permissions.toggleProfilePermission != null && !player.hasPermission(this.plugin.permissions.toggleProfilePermission)) {
                        player.sendMessage(this.plugin.messages.toggleProfileNoPermission);
                        return true;
                     }

                     if (playerData.hasProfileBlocked()) {
                        playerData.setProfileBlocked(false);
                        player.sendMessage(this.plugin.messages.toggleProfileUnlocked);
                     } else {
                        playerData.setProfileBlocked(true);
                        player.sendMessage(this.plugin.messages.toggleProfileLocked);
                     }

                     player.playSound(player.getLocation(), Sounds.CLICK_ON.getSound(), 1.0F, 1.0F);
                     return true;
                  }
               }

               LocalUtils.send(sender, "&cUse: /betterprofile <toggle> <viewer/profile>");
            } else {
               LocalUtils.send(sender, "&cConsole cannot toggle profiles..");
            }

            return true;
         } else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(this.plugin.permissions.adminPermission)) {
            if (this.testServer && sender instanceof Player && !sender.isOp()) {
               LocalUtils.send(sender, this.testReloadError.replace("{PLAYER}", sender.getName()));
               return true;
            } else {
               this.plugin.closeInventories();
               this.plugin.getOpenedProfilesMap().clear();
               this.plugin.getFiles().getConfig().reload();
               this.plugin.getFiles().getItems().reload();
               this.plugin.getFiles().getArmorequip().reload();
               this.plugin.reloadMessages();
               this.plugin.permissions.reloadMessages();
               this.plugin.messages.reloadMessages();
               this.plugin.profileCommand.reloadMessages();
               this.plugin.clickListener.reloadMessages();
               this.plugin.closeListener.reloadMessages();
               this.plugin.closeListener.reloadMessages();
               this.plugin.connectionListener.reloadMessages();
               if (this.plugin.armorEquipListener != null) {
                  this.plugin.armorEquipListener.reloadMessages();
               }

               if (this.plugin.playerHeldListener != null) {
                  this.plugin.playerHeldListener.reloadMessages();
               }

               if (this.plugin.swapHandListener != null) {
                  this.plugin.swapHandListener.reloadMessages();
               }

               if (this.plugin.interactListener != null) {
                  this.plugin.interactListener.reloadMessages();
               }

               if (this.plugin.consumeListener != null) {
                  this.plugin.consumeListener.reloadMessages();
               }

               this.plugin.loadItems();
               sender.sendMessage(this.plugin.messages.reloaded);
               return true;
            }
         } else {
            LocalUtils.send(sender, " ");
            LocalUtils.send(sender, "&6&lBetterProfiles &eby &6&lAlonsoAliaga &eVersion &6" + this.plugin.getDescription().getVersion());
            Iterator var9;
            String line;
            if (sender.hasPermission(this.plugin.permissions.adminPermission)) {
               var9 = this.plugin.messages.adminHelpMessage.iterator();

               while(var9.hasNext()) {
                  line = (String)var9.next();
                  sender.sendMessage(line);
               }
            } else {
               var9 = this.plugin.messages.userHelpMessage.iterator();

               while(var9.hasNext()) {
                  line = (String)var9.next();
                  sender.sendMessage(line);
               }
            }

            LocalUtils.send(sender, " ");
            if (sender instanceof Player) {
               player = (Player)sender;
               player.playSound(player.getLocation(), Sounds.PICKUP.getSound(), 1.0F, 1.0F);
            }

            return true;
         }
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
      if (args.length >= 1) {
         if (args.length == 1) {
            return sender.hasPermission(this.plugin.permissions.adminPermission) ? this.adminList : this.userList;
         }

         if (this.adminList.contains(args[0].toLowerCase())) {
            if (args[0].equalsIgnoreCase("reload")) {
               return sender.hasPermission(this.plugin.permissions.adminPermission) ? this.emptyList : this.onlinePlayers(sender, args);
            }

            if (args[0].equalsIgnoreCase("view")) {
               return this.onlinePlayers(sender, args);
            }

            return this.toggleList;
         }
      }

      return this.onlinePlayers(sender, args);
   }
}
