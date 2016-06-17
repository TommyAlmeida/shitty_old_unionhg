package eu.union.dev.storage.sql;

import eu.union.dev.HG;
import eu.union.dev.PlayerManager;
import eu.union.dev.storage.KPlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Tomas on 17-06-2016.
 */
public class Database {

    String user = "";
    String database = "";
    String password = "";
    String port = "";
    String hostname = "";
    Connection c = null;

    public Database(String user, String password, String database, String port, String hostname) {
        this.user = user;
        this.database = database;
        this.password = password;
        if (port == null) {
            this.port = "3306";
        } else {
            this.port = port;
        }
        this.hostname = hostname;
    }


    /**
     * Open mysql connection
     *
     * @return
     */
    public Connection open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            System.out.println("MySQL connection successful");
            HG.getInstance().getLogger().log(Level.INFO, "MySQL connection successful");
            return c;
        } catch (SQLException e) {
            HG.getInstance().getLogger().log(Level.SEVERE, "MySQL connection has been aborted");
            HG.getInstance().getLogger().log(Level.SEVERE, "Reason:" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
        }
        return c;
    }

    public Connection getConnection() {
        return this.c;
    }

    /**
     * Close mysql connection
     */
    public Connection close(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            HG.getInstance().getLogger().log(Level.SEVERE, "MySQL can't close connection");
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Get all tables together and setup one by one
     */
    public synchronized void setupTables() {
        setupProfiles();
    }

    public synchronized void setupProfiles(){
        try {
            PreparedStatement profiles = c
                    .prepareStatement("CREATE TABLE IF NOT EXISTS `Profiles`(`id` int(11) NOT NULL auto_increment,"
                            + " `UUID` varchar(255) NOT NULL,"
                            + " `Kills` int(10) NOT NULL,"
                            + " `Deaths` int(10) NOT NULL,"
                            + "`Coins` bigint(255) NOT NULL,"
                            + "`UniCoins` bigint(255) NOT NULL,"
                            + "`Wins` int(10) NOT NULL,"
                            + "`Loses` int(10) NOT NULL,"
                            + "`KDR` int(10) NOT NULL,"
                            + " PRIMARY KEY(`id`));");
            profiles.execute();
            profiles.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Try to create a new player profile
     *
     * @param uuid
     * @return
     */
    public synchronized boolean createPlayerProfile(UUID uuid) {
        try {
            String PvPMainStatement = "SELECT * FROM Profiles WHERE UUID = '" + uuid + "';";
            ResultSet result = this.c.createStatement().executeQuery(PvPMainStatement);
            if (result.next()) {
                PlayerManager.addPlayerProfile(new KPlayer(
                        uuid,
                        result.getInt("Kills"),
                        result.getInt("Deaths"),
                        result.getLong("Coins"),
                        result.getLong("UniCoins"),
                        result.getInt("KDR"),
                        result.getInt("Wins"),
                        result.getInt("Loses")
                ));

            } else {
                PlayerManager.addPlayerProfile(new KPlayer(uuid, 0, 0, 0, 0, 0 , 0, 0));
            }
        } catch (SQLException e) {
            System.out.println("Error while trying to create PlayerDat! Reason: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Update all data from player profile to the sql database
     *
     * @param playerProfile
     * @return
     */
    public synchronized boolean updatePlayerProfileSQL(KPlayer playerProfile) {
        try {
            String PvPMainStatement = "SELECT * FROM Profiles WHERE UUID = '" + playerProfile.getUuid() + "'";
            ResultSet result = this.c.createStatement().executeQuery(PvPMainStatement);
            if (result.next()) {
                String PvPMainUpdate = "UPDATE Profiles SET Kills = " + playerProfile.getKills()
                        + ", Deaths = " + playerProfile.getDeaths()
                        + ", Coins = " + playerProfile.getCoins()
                        + ", UniCoins = " + playerProfile.getUnic()
                        + ", Wins = " + playerProfile.getWins()
                        + ", Loses = " + playerProfile.getLoses()
                        + " WHERE UUID = '"
                        + playerProfile.getUuid() + "';";
                this.c.createStatement().executeUpdate(PvPMainUpdate);
                //UUID, Kills, Deaths, Coins, UniCoins, KDR, Wins, Loses
            } else {
                String PvPMainUpdate = "INSERT INTO Profiles (UUID, Kills, Deaths, Coins, UniCoins, KDR, Wins, Loses) VALUES ('"
                        + playerProfile.getUuid() + "', "
                        + playerProfile.getKills() + ", "
                        + playerProfile.getDeaths() + ", "
                        + playerProfile.getCoins() + ", "
                        + playerProfile.getUnic() + ", "
                        + playerProfile.getKDR() + ", "
                        + playerProfile.getWins() + ", "
                        + playerProfile.getLoses() + "); ";
                this.c.createStatement().executeUpdate(PvPMainUpdate);
            }
        } catch (SQLException e) {
            System.out.println("Error while trying to update the MySQL! Reason: " + e.getMessage());
            return false;
        }
        return true;
    }


}
