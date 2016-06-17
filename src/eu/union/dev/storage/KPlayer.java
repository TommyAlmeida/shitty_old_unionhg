package eu.union.dev.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Termologia:
 *
 * - UniC = UniCoins - Moedas pagas
 */
public class KPlayer {

    private UUID uuid;
    private int deaths, kills, kdr, wins, loses;
    private long coins, unic;

    public HashMap<UUID, Integer> killstreak = new HashMap<>();


    public KPlayer(UUID uuid, int kills, int deaths, long coins, long unic, int kdr, int wins, int loses) {
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
        this.kdr = kdr;
        this.wins = wins;
        this.loses = loses;
        this.coins = coins;
        this.unic = unic;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Retrieve the player deaths
     * @return
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Set a new value to player deaths
     * @param deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * Add a new value to player deaths
     * @param deaths
     */
    public void addDeaths(int deaths) {
        this.deaths += deaths;
    }

    /**
     * Retrieve the player kills
     * @return
     */
    public int getKills() {
        return kills;
    }

    /**
     * Add a new value to player kills
     * @param kills
     */
    public void addKills(int kills) {
        this.kills += kills;
    }

    /**
     * Set a new value to player kills
     * @param kills
     */
    public void setKills(int kills) {
        this.kills = kills;
    }


    /**
     * Retrieve the player coins
     * @return
     */
    public long getCoins() {
        return coins;
    }

    /**
     * Set a new value to player coins
     * @param coins
     */
    public void setCoins(long coins) {
        this.coins = coins;
    }

    /**
     * Add a new value to player coins
     * @param coins
     */
    public void addCoins(long coins) {
        this.coins += coins;
    }

    /**
     * Remove a certain value of coins
     * @param coins
     */
    public void removeCoins(long coins){
        this.coins -= coins;
    }

    public long getUnic() {
        return unic;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    /**
     * Set a new value to player coins
     * @param unic
     */
    public void setUniCoins(long unic) {
        this.unic = unic;
    }

    /**
     * Add a new value to player coins
     * @param unic
     */
    public void addUniCoins(long unic) {
        this.unic += unic;
    }

    /**
     * Remove a certain value of coins
     * @param unic
     */
    public void removeUniCoins(long unic){
        this.unic -= unic;
    }

    /**
     * Add a new value to player killstreak
     * @param amount
     */
    public void addKillstreak(int amount){
        killstreak.put(getUuid(), + amount);
    }

    /**
     * Set a new value to player killstreak
     * @param amount
     */
    public void setKillstreak(int amount){
        killstreak.put(getUuid(), amount);
    }


    /**
     * See if the player is in killstreak or if its not
     * @return
     */
    public boolean isInKillstreak(){
        if(killstreak.containsKey(getUuid())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Clear killstreak mode
     */
    public void clearKillstreak(){
        killstreak.clear();
    }

    /**
     * Remove a certain value of killstreak
     * @param amount
     */
    public void removeKillStreak(int amount){
        killstreak.remove(amount);
    }


    /**
     * Retrieve the player KDR, Formula: kills / deaths
     * @return
     */
    public int getKDR() {
        return deaths == 0 ? kills : kills / deaths;
    }

    /**
     * Reset all player stats to 0
     */
    public void clear() {
        deaths = 0;
        kills = 0;
        kdr = 0;
    }
}
