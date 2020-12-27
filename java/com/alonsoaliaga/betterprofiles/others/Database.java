package com.alonsoaliaga.betterprofiles.others;

import com.alonsoaliaga.betterprofiles.BetterProfiles;
import com.alonsoaliaga.betterprofiles.utils.AlonsoUtils;
import com.alonsoaliaga.betterprofiles.utils.LocalUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
   public static String SQLITE_PLAYERS_QUERY = "uuid VARCHAR(36) PRIMARY KEY,profile_blocked INTEGER DEFAULT 0,profiles_blocked INTEGER DEFAULT 0";
   public static String MYSQL_PLAYERS_QUERY = "uuid VARCHAR(36) PRIMARY KEY,profile_blocked INTEGER DEFAULT 0,profiles_blocked INTEGER DEFAULT 0";

   public class MySQL implements Database.Data {
      private Connection connection;
      private String host;
      private String database;
      private String username;
      private String password;
      private String table;
      private String query;
      private int port;
      private AlonsoUtils.AlonsoPlugin plugin;

      public MySQL(AlonsoUtils.AlonsoPlugin plugin) {
         this.plugin = plugin;
         this.table = plugin.getFiles().getConfig().get().getString("Database.Table");
         this.query = Database.MYSQL_PLAYERS_QUERY;
         this.firstConnection();
      }

      public Connection getConnection() {
         try {
            if (this.connection == null || this.connection.isClosed()) {
               Class.forName("com.mysql.jdbc.Driver");
               LocalUtils.logp("[MySQL] Attempting to connect to database..");
               this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", this.host, this.port, this.database), this.username, this.password);
               LocalUtils.logp("[MySQL] Successfully connected to database!");
            }

            return this.connection;
         } catch (ClassNotFoundException var2) {
            LocalUtils.log(String.format("&c%s[MySQL] Drivers for MySQL couldn't be found!", "[BetterProfiles] "));
            var2.printStackTrace();
            return null;
         } catch (SQLException var3) {
            LocalUtils.log(String.format("&c%s[MySQL] Error connecting to database!", "[BetterProfiles] "));
            var3.printStackTrace();
            return null;
         }
      }

      public String getTable() {
         return this.table;
      }

      public void closeConnection() {
         try {
            if (this.connection != null && !this.connection.isClosed()) {
               this.connection.close();
            }

            LocalUtils.logp("[MySQL] Forcing connection close..");
         } catch (SQLException var2) {
         }

      }

      public void firstConnection() {
         this.host = this.plugin.getFiles().getConfig().get().getString("Database.Host");
         this.port = this.plugin.getFiles().getConfig().get().getInt("Database.Port");
         this.database = this.plugin.getFiles().getConfig().get().getString("Database.Database");
         this.username = this.plugin.getFiles().getConfig().get().getString("Database.Username");
         this.password = this.plugin.getFiles().getConfig().get().getString("Database.Password");

         try {
            LocalUtils.log(String.format("[MySQL] Attempting to create '%s' table..", this.table));
            PreparedStatement statement = this.getConnection().prepareStatement(String.format("CREATE TABLE IF NOT EXISTS %s (%s)", this.table, this.query));
            statement.executeUpdate();
            LocalUtils.log(String.format("[MySQL] Table '%s' has been created if didn't exist.", this.table));
         } catch (SQLException var2) {
            LocalUtils.log(String.format("&c%s[MySQL] Error creating '%s' table.", "[BetterProfiles] ", this.table));
            var2.printStackTrace();
         }

      }
   }

   public class SQLite implements Database.Data {
      private BetterProfiles plugin;
      private Connection connection;
      private String database;
      private String table;
      private String gamesTable;
      private String url;
      private String query;
      private String gamesQuery;

      public SQLite(BetterProfiles plugin) {
         this.plugin = plugin;
         this.table = plugin.getFiles().getConfig().get().getString("Database.Table");
         this.query = Database.SQLITE_PLAYERS_QUERY;
         this.firstConnection();
      }

      public Connection getConnection() {
         try {
            if (this.connection == null || this.connection.isClosed()) {
               Class.forName("org.sqlite.JDBC");
               LocalUtils.log(String.format("[SQLite] Connecting to database '%s'!", this.database));
               this.connection = DriverManager.getConnection(this.url);
               DatabaseMetaData meta = this.connection.getMetaData();
               LocalUtils.log(String.format("[SQLite] Driver available: %s", meta.getDriverName()));
               LocalUtils.log("[SQLite] Successfully connected to database!");
            }

            return this.connection;
         } catch (ClassNotFoundException var2) {
            LocalUtils.log(String.format("&c%s[SQLite] Drivers for SQLite couldn't be found!", "[BetterProfiles] "));
            var2.printStackTrace();
            return null;
         } catch (SQLException var3) {
            LocalUtils.log(String.format("&c%s[SQLite] Error connecting to database!", "[BetterProfiles] "));
            var3.printStackTrace();
            return null;
         }
      }

      public String getTable() {
         return this.table;
      }

      public void closeConnection() {
         try {
            if (this.connection != null && !this.connection.isClosed()) {
               this.connection.close();
            }

            LocalUtils.log("[SQLite] Forcing connection close..");
         } catch (SQLException var2) {
         }

      }

      public void firstConnection() {
         String dataname = this.plugin.getFiles().getConfig().get().getString("Database.File");
         this.database = dataname.endsWith(".db") ? dataname : dataname + ".db";
         this.url = "jdbc:sqlite:" + this.plugin.getDataFolder() + File.separator + this.database;

         try {
            LocalUtils.log(String.format("[SQLite] Attempting to create '%s' table..", this.table));
            PreparedStatement statement = this.getConnection().prepareStatement(String.format("CREATE TABLE IF NOT EXISTS %s (%s)", this.table, this.query));
            statement.executeUpdate();
            LocalUtils.log(String.format("[SQLite] Table '%s' has been created if didn't exist.", this.table));
         } catch (SQLException var3) {
            LocalUtils.log(String.format("&c%s[SQLite] Error creating '%s' table.", "[BetterProfiles] ", this.table));
            var3.printStackTrace();
         }

      }
   }

   public interface Data {
      Connection getConnection();

      String getTable();

      void closeConnection();

      void firstConnection();
   }
}
