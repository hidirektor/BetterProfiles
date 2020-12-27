package com.alonsoaliaga.betterprofiles.others;

import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FileManager {
   private JavaPlugin plugin;
   private FileManager.Configuration configuration;
   private FileManager.Configuration armorequip;
   private FileManager.Configuration items;

   public FileManager(JavaPlugin main) {
      this.plugin = main;
      this.configuration = this.setupConfigFile("config");
      this.armorequip = this.setupConfigFile("armorequip");
      this.items = this.setupConfigFile("items");
   }

   private FileManager.Configuration setupConfigFile(String configName) {
      File folder = new File(this.plugin.getDataFolder(), "/");
      if (!folder.exists()) {
         boolean created = folder.mkdir();
         if (created) {
            LocalUtils.logp("Creating datafolder '/BetterProfiles/ ..'");
         }
      }

      String fileName = configName.endsWith(".yml") ? configName : configName + ".yml";
      return new FileManager.Configuration(this.plugin, fileName);
   }

   public FileManager.Configuration getConfig() {
      return this.configuration;
   }

   public FileManager.Configuration getArmorequip() {
      return this.armorequip;
   }

   public FileManager.Configuration getItems() {
      return this.items;
   }

   public class Configuration {
      private JavaPlugin plugin;
      private String fileName;
      private FileConfiguration fileConfiguration = null;
      private File file = null;

      public Configuration(JavaPlugin plugin, String name) {
         this.plugin = plugin;
         this.fileName = name.endsWith(".yml") ? name : name + ".yml";
         this.init();
      }

      private void init() {
         this.file = new File(this.plugin.getDataFolder(), this.fileName);
         if (!this.file.exists()) {
            this.plugin.saveResource(this.fileName, false);
         }

         this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
      }

      public FileManager.Configuration saveDefaults() {
         this.file = new File(this.plugin.getDataFolder(), this.fileName);
         this.plugin.saveResource(this.fileName, true);
         return this;
      }

      public FileManager.Configuration save() {
         if (this.fileConfiguration != null && this.file != null) {
            try {
               if (this.fileConfiguration.getConfigurationSection("").getKeys(true).size() != 0) {
                  this.fileConfiguration.save(this.file);
               }
            } catch (IOException var2) {
               var2.printStackTrace();
            }

            return this;
         } else {
            return this;
         }
      }

      public FileManager.Configuration reload() {
         if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder(), this.fileName);
         }

         this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
         return this;
      }

      public FileConfiguration get() {
         return this.fileConfiguration;
      }
   }
}
