package eu.union.dev.utils;

/**
 * Created by Fentis on 06/06/2016.
 */
public enum Messages {

    PREFIX("§e§lUnionHG §7§l»"),
    NO_PERMA("§e§lUnionHG §cYou don't have permissions to do this");


    String s;

    Messages(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return this.s;
    }
}

