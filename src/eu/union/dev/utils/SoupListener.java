package eu.union.dev.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class SoupListener implements Listener {
    public SoupListener() {
        ShapelessRecipe cocoaSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        cocoaSoup.addIngredient(1, Material.INK_SACK, 3);
        cocoaSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(cocoaSoup);
        ShapelessRecipe cacSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        cacSoup.addIngredient(1, Material.CACTUS);
        cacSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(cacSoup);
        ShapelessRecipe abobSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        abobSoup.addIngredient(2, Material.PUMPKIN_SEEDS);
        abobSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(abobSoup);
        ShapelessRecipe florSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        florSoup.addIngredient(1, Material.RED_ROSE);
        florSoup.addIngredient(1, Material.YELLOW_FLOWER);
        florSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(florSoup);
        ShapelessRecipe melSoup = new ShapelessRecipe(new ItemStack(Material.MUSHROOM_SOUP));
        melSoup.addIngredient(2, Material.MELON_SEEDS);
        melSoup.addIngredient(1, Material.BOWL);
        Bukkit.addRecipe(melSoup);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getHealth() != 20.0D) {
            int soup = 7;
            if (((event.getAction() == Action.RIGHT_CLICK_AIR) ||
                    (event.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                    (player.getItemInHand().getType() == Material.MUSHROOM_SOUP)) {
                player.setHealth(player.getHealth() + soup > player.getMaxHealth() ? player.getMaxHealth() : player.getHealth() + soup);
                event.getPlayer().setItemInHand(new ItemStack(Material.BOWL));
            }
        }
    }
}
