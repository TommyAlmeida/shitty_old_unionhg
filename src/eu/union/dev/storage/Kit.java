package eu.union.dev.storage;

import eu.union.dev.api.Icon;
import eu.union.dev.utils.Messages;
import eu.union.dev.utils.Perms;
import org.bukkit.entity.Player;

public abstract class Kit {

    public String prefix = Messages.PREFIX.toString();
    Rarity rarity;
    Difficulty difficulty;
    private String name;
    private String permission;
    private Icon icon;
    private long price;

    public Kit(String name, boolean free, Difficulty difficulty, Rarity rarity, Icon icon, long price) {
        this.name = name;
        this.difficulty = difficulty;
        this.rarity = rarity;
        this.icon = icon;
        this.price = price;
        if (free)
            this.permission = Perms.KIT_FREE.toString();
        else
            this.permission =  "unkit."+name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getPermission() {
        return permission;
    }

    public abstract void applyKit(Player player);

    public Icon getIcon() {
        return icon;
    }

    public long getPrice() { return price; }

    public enum Difficulty {
        LOW("Low"),
        MEDIUM("Medium"),
        HARD("Hard"),
        PRO("Pro");

        String d;

        Difficulty(String d) {
            this.d = d;
        }

        public String value() {
            return this.d;
        }
    }

    public enum Rarity {
        COMMON("§7", 70),
        RARE("§b", 15),
        EPIC("§d", 7),
        HEROIC("§6", 5),
        BEAST("§5", 3);

        String color;
        int chance;

        Rarity(String color, int chance) {
            this.color = color;
            this.chance = chance;
        }

        public int getChance() {
            return this.chance;
        }

        public String getColor() {
            return this.color;
        }
    }

}