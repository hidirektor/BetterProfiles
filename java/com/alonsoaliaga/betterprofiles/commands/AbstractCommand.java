package com.alonsoaliaga.betterprofiles.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public abstract class AbstractCommand implements CommandExecutor, TabExecutor {
   protected final String command;
   protected final String description;
   protected final List<String> alias;
   protected final String usage;
   protected final String permMessage;
   protected static CommandMap cmap;

   public AbstractCommand(String command) {
      this(command, (String)null, (String)null, (String)null, (List)null);
   }

   public AbstractCommand(String command, String usage) {
      this(command, usage, (String)null, (String)null, (List)null);
   }

   public AbstractCommand(String command, String usage, String description) {
      this(command, usage, description, (String)null, (List)null);
   }

   public AbstractCommand(String command, String usage, String description, String permissionMessage) {
      this(command, usage, description, permissionMessage, (List)null);
   }

   public AbstractCommand(String command, String usage, String description, List<String> aliases) {
      this(command, usage, description, (String)null, aliases);
   }

   public AbstractCommand(String command, String usage, String description, String permissionMessage, List<String> aliases) {
      this.command = command.toLowerCase();
      this.usage = usage;
      this.description = description;
      this.permMessage = permissionMessage;
      this.alias = aliases;
   }

   public void register() {
      AbstractCommand.ReflectCommand cmd = new AbstractCommand.ReflectCommand(this.command);
      if (this.alias != null) {
         cmd.setAliases(this.alias);
      }

      if (this.description != null) {
         cmd.setDescription(this.description);
      }

      if (this.usage != null) {
         cmd.setUsage(this.usage);
      }

      if (this.permMessage != null) {
         cmd.setPermissionMessage(this.permMessage);
      }

      this.getCommandMap().register(this.command, cmd);
      cmd.setExecutor(this);
   }

   final CommandMap getCommandMap() {
      if (cmap == null) {
         try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            cmap = (CommandMap)f.get(Bukkit.getServer());
            return this.getCommandMap();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      } else if (cmap != null) {
         return cmap;
      }

      return this.getCommandMap();
   }

   public abstract boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4);

   public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
      return null;
   }

   public List<String> onlinePlayers(CommandSender sender, String[] args) {
      String lastWord = args[args.length - 1];
      Player senderPlayer = sender instanceof Player ? (Player)sender : null;
      List<String> matchedPlayers = new ArrayList();
      Iterator var6 = sender.getServer().getOnlinePlayers().iterator();

      while(true) {
         Player player;
         String name;
         do {
            if (!var6.hasNext()) {
               matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
               return matchedPlayers;
            }

            player = (Player)var6.next();
            name = player.getName();
         } while(senderPlayer != null && !senderPlayer.canSee(player));

         if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
            matchedPlayers.add(name);
         }
      }
   }

   private final class ReflectCommand extends Command {
      private AbstractCommand exe = null;

      protected ReflectCommand(String command) {
         super(command);
      }

      public void setExecutor(AbstractCommand exe) {
         this.exe = exe;
      }

      public boolean execute(CommandSender sender, String commandLabel, String[] args) {
         return this.exe != null ? this.exe.onCommand(sender, this, commandLabel, args) : false;
      }

      public List<String> tabComplete(CommandSender sender, String alais, String[] args) {
         return this.exe != null ? this.exe.onTabComplete(sender, this, alais, args) : null;
      }
   }
}
