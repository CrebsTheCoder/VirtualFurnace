package com.shanebeestudios.api.machine;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Abstract machine class
 * <p>Other machines will extend from this class</p>
 */
public abstract class Machine {

    private final String name;
    private final UUID uniqueID;

    Machine(@NotNull UUID uniqueID, String name) {
        this.uniqueID = uniqueID;
        this.name = name;
    }

    /**
     * Get this machine's unique ID
     *
     * @return Unique ID of this machine
     */
    public UUID getUniqueID() {
        return uniqueID;
    }

    /**
     * Get this machine's name
     *
     * @return Name of this machine
     */
    public String getName() {
        return name;
    }

    /**
     * Tick this machine.
     */
    public void tick() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return Objects.equals(name, machine.name) && Objects.equals(uniqueID, machine.uniqueID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uniqueID);
    }

    /**
     * Open the inventory of this machine to a player
     *
     * @param player Player to open inventory to
     */
    public abstract void openInventory(Player player);

    @Override
    public String toString() {
        return "Machine{" +
                "name='" + name + '\'' +
                ", uniqueID=" + uniqueID +
                '}';
    }
}
