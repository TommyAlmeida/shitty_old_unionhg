package eu.union.dev.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (((e.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) &&
                !p.getGameMode().equals(GameMode.CREATIVE) &&
                p.getItemInHand().getType().equals(Material.MUSHROOM_SOUP)){
            if (p.getHealth() < p.getMaxHealth()) {
                int soup = 7;
                if (p.getHealth() < p.getMaxHealth() - soup) {
                    p.setHealth(p.getHealth() + soup);
                    p.setItemInHand(new ItemStack(Material.BOWL));
                } else {
                    p.setHealth(p.getMaxHealth());
                    p.setItemInHand(new ItemStack(Material.BOWL));
                }
            } else
            if (p.getFoodLevel() < 20){
                int food = 7;
                if (p.getFoodLevel() < 20 - food) {
                    p.setFoodLevel(p.getFoodLevel() + food);
                    p.setItemInHand(new ItemStack(Material.BOWL));
                } else {
                    p.setFoodLevel(20);
                    p.setItemInHand(new ItemStack(Material.BOWL));
                }
            }
        }
    }
}
