package eu.union.dev;


import eu.union.dev.storage.KPlayer;
import eu.union.dev.storage.Kit;
import eu.union.dev.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitManager {

    public static KitManager km = new KitManager();
    public HashMap<Player, Kit> playerKit = new HashMap<>();
    private HashMap<Player, Kit> kitselector = new HashMap<>();
    List<Kit> kits = new ArrayList<>(); //Lista de Kits.

    /**
     * Método que sempre retornará o mesmo KitManager.
     *
     * @return
     */
    public static KitManager getManager() {
        return km;
    }

    /**
     * Register kit by adding him on kits list
     *
     * @param kit
     */
    public void registerKit(Kit kit) {
        kits.add(kit);
    }

    /**
     * Remove kit form kits list
     *
     * @param kit
     */
    public void unregisterKit(Kit kit) {
        kits.remove(kit);
    }

    /**
     * Search kit by name
     *
     * @param name
     * @return
     */
    public Kit getKitByName(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name))
                return kit;
        }
        return null;
    }

    public boolean getKitAmIUsing(Player p, String name) {
        return playerKit.containsKey(p) && playerKit.get(p).equals(getKitByName(name));
    }


    /**
     * Get the player kit that hes using
     *
     * @param player
     * @return
     */
    public Kit getKitByPlayer(Player player) {
        if (playerKit.containsKey(player))
            return playerKit.get(player);
        return null;
    }


    /**
     * Reset all player values to default
     *
     * @param player
     */
    public void readyPlayer(Player player) {
        player.getInventory().clear();
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setExhaustion(0f);
        player.setFallDistance(0f);
        player.setFireTicks(0);
        player.setFireTicks(0);

        for (PotionEffect pE : player.getActivePotionEffects()) {
            player.removePotionEffect(pE.getType());
        }

        playerKit.remove(player);
    }

    /**
     * Function activated when player use the kit
     *
     * @param player
     * @param kit
     */
    public void applyKit(Player player, Kit kit) {
        readyPlayer(player);
        kit.applyKit(player);

        playerKit.put(player, kit);

        player.sendMessage(Messages.PREFIX.toString() + " §7You are using kit: §a" + kit.getName());
    }

    /**
     * Remove kit from player
     *
     * @param player
     */
    public void removeKit(Player player) {

        if (!playerKit.containsKey(player)) {
            player.sendMessage(Messages.NO_PERM.toString());
            return;
        }

        readyPlayer(player);
        playerKit.remove(player);
    }

    /**
     * Returns the difficulty of the kit
     *
     * @param kit
     * @return
     */
    public String getKitDifficulty(Kit kit) {
        return kit.getDifficulty().value();
    }

    /**
     * Check if the player have the level required
     * to use the selected kit
     *
     * @param //kPlayer
     * @param //kit
     * @return
     */
    public boolean hasEnoughLevel(KPlayer kPlayer, Kit kit) {
        return kPlayer.getLevel() >= kit.getLevel();
    }

    public boolean usingKit(Player player) {
        return playerKit.containsKey(player);
    }


    /**
     * Get a list of kits
     *
     * @return
     */
    public List<Kit> getKits() {
        return kits;
    }

    public Kit getPlayerKitInLobby(Player p){
        if (kitselector.containsKey(p)){
            return kitselector.get(p);
        }
        return null;
    }
    public void setPlayerKitInLobby(Player p , Kit kit){
        kitselector.put(p,kit);
    }
}
