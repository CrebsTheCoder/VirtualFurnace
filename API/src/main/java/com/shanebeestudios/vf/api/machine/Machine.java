package com.shanebeestudios.vf.api.machine;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Abstract machine class
 * <p>Other machines will extend from this class</p>
 */
public abstract class Machine {

    private final String name;
    private final UUID uniqueID, machineOpener;

    Machine(@NotNull UUID uniqueID, UUID machineOpener, String name) {
        this.uniqueID = uniqueID;
        this.machineOpener = machineOpener;
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
     * Get this machine's opener.
     *
     * @return The machine opener's unique id, if there is none; null.
     */
    public UUID getMachineOpener() {
       return machineOpener;
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
                ", machineOpener=" + machineOpener +
                '}';
    }
}
