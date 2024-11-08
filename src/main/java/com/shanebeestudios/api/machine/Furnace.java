package com.shanebeestudios.api.machine;

import com.shanebeestudios.api.*;
import com.shanebeestudios.api.event.machine.*;
import com.shanebeestudios.api.property.*;
import com.shanebeestudios.api.recipe.FurnaceRecipe;
import com.shanebeestudios.api.recipe.*;
import com.shanebeestudios.api.util.Util;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Virtual furnace object
 */
@SuppressWarnings("unused")
public class Furnace extends Machine implements PropertyHolder<FurnaceProperties>, InventoryHolder, ConfigurationSerializable {

    private final FurnaceProperties furnaceProperties;
    private final RecipeManager recipeManager;
    private final Inventory inventory;
    private ItemStack fuel;
    private ItemStack input;
    private ItemStack output;
    private int cookTime;
    private int cookTimeTotal;
    private int fuelTime;
    private int fuelTimeTotal;
    private float experience;

    /**
     * Create a new furnace object
     * <p><b>NOTE:</b> Creating a furnace object using this method will not tick the furnace.</p>
     * <p>It is recommended to use <b>{@link FurnaceManager#createFurnace(String)}</b></p>
     * <p><b>NOTE:</b> The properties used for this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name Name of the object which will show up in the UI
     */
    public Furnace(String name) {
        this(name, FurnaceProperties.FURNACE);
    }

    /**
     * Create a new furnace object
     * <p><b>NOTE:</b> Creating a furnace object using this method will not tick the furnace.</p>
     * <p>It is recommended to use <b>{@link FurnaceManager#createFurnace(String)}</b></p>
     * <p><b>NOTE:</b> The properties used for this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name              Name of the object which will show up in the UI
     * @param furnaceProperties Property for this furnace.
     */
    public Furnace(String name, FurnaceProperties furnaceProperties) {
        super(UUID.randomUUID(), name);
        this.furnaceProperties = furnaceProperties;
        this.recipeManager = VirtualFurnaceAPI.getInstance().getRecipeManager();
        this.cookTime = 0;
        this.cookTimeTotal = 0;
        this.fuelTime = 0;
        this.fuelTimeTotal = 0;
        this.fuel = null;
        this.input = null;
        this.output = null;
        this.inventory = Bukkit.createInventory(this, InventoryType.FURNACE, Util.getColString(name));
        this.experience = 0.0f;
        this.updateInventory();
    }

    // Used for deserializer
    private Furnace(String name, UUID uuid, int cookTime, int fuelTime, float xp, ItemStack fuel, ItemStack input, ItemStack output, FurnaceProperties furnaceProperties) {
        super(uuid, name);
        this.recipeManager = VirtualFurnaceAPI.getInstance().getRecipeManager();
        this.cookTime = cookTime;
        this.fuelTime = fuelTime;
        this.fuel = fuel;
        this.input = input;
        this.output = output;
        this.furnaceProperties = furnaceProperties;

        FurnaceRecipe furnaceRecipe = recipeManager.getByIngredient(input != null ? input.getType() : null);
        if (furnaceRecipe != null) {
            this.cookTimeTotal = furnaceRecipe.getCookTime();
        } else {
            this.cookTimeTotal = 0;
        }
        FurnaceFuel fuelF = recipeManager.getFuelByMaterial(fuel != null ? fuel.getType() : null);
        if (fuelF != null) {
            this.fuelTimeTotal = fuelF.getBurnTime();
        } else {
            this.fuelTimeTotal = 0;
        }
        this.experience = xp;
        this.inventory = Bukkit.createInventory(this, InventoryType.FURNACE, Util.getColString(name));
        this.updateInventory();
    }

    /**
     * Deserialize this object from yaml
     * <p><b>Internal use only!</b></p>
     *
     * @param args Serialized map of object
     * @return New instance of object
     */
    public static Furnace deserialize(Map<String, Object> args) {
        String name = ((String) args.get("name"));
        UUID uuid = UUID.fromString(((String) args.get("uuid")));
        FurnaceProperties furnaceProperties = (FurnaceProperties) args.get("properties");
        int cookTime = ((Number) args.get("cookTime")).intValue();
        int fuelTime = ((Number) args.get("fuelTime")).intValue();
        float xp = args.containsKey("xp") ? ((Number) args.get("xp")).floatValue() : 0.0f;
        ItemStack fuel = ((ItemStack) args.get("fuel"));
        ItemStack input = ((ItemStack) args.get("input"));
        ItemStack output = ((ItemStack) args.get("output"));

        return new Furnace(name, uuid, cookTime, fuelTime, xp, fuel, input, output, furnaceProperties);
    }

    /**
     * Get the properties associated with this furnace
     *
     * @return Properties associated with this furnace
     */
    @Override
    public FurnaceProperties getProperties() {
        return this.furnaceProperties;
    }

    /**
     * Get this furnace's inventory
     *
     * @return Inventory
     */
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Get this furnace's current fuel
     *
     * @return Current fuel
     */
    public ItemStack getFuel() {
        return fuel;
    }

    /**
     * Set this furnace's fuel
     *
     * @param fuel Fuel to set
     */
    public void setFuel(ItemStack fuel) {
        this.fuel = fuel;
    }

    /**
     * Get this furnace's current input ItemStack
     *
     * @return Current input
     */
    public ItemStack getInput() {
        return input;
    }

    /**
     * Set this furnace's input ItemStack
     *
     * @param input ItemStack to set
     */
    public void setInput(ItemStack input) {
        this.input = input;
    }

    /**
     * Get this furnace's output ItemStack
     *
     * @return Output ItemStack
     */
    public ItemStack getOutput() {
        return output;
    }

    /**
     * Get the current experience stored in this furnace
     * <p>This will also reset the current experience back to 0.0</p>
     *
     * @return Current experience stored in this furnace
     */
    public float extractExperience() {
        float exp = this.experience;
        this.experience = 0.0f;
        return exp;
    }

    /**
     * Open this furnace's inventory to a player
     *
     * @param player Player to open inventory to
     */
    @Override
    public void openInventory(Player player) {
        updateInventory();
        player.openInventory(this.inventory);
    }

    private void updateInventory() {
        this.inventory.setItem(0, this.input);
        this.inventory.setItem(1, this.fuel);
        this.inventory.setItem(2, this.output);
    }

    private void updateInventoryView() {
        ItemStack input = this.inventory.getItem(0);
        if (this.input != input) {
            this.input = input;
        }
        ItemStack fuel = this.inventory.getItem(1);
        if (this.fuel != fuel) {
            this.fuel = fuel;
        }
        ItemStack output = this.inventory.getItem(2);
        if (this.output != output) {
            this.output = output;
        }
        for (HumanEntity entity : this.inventory.getViewers()) {
            try {
                InventoryView view = entity.getOpenInventory();
                view.setProperty(InventoryView.Property.COOK_TIME, this.cookTime);
                view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_SMELTING, this.cookTimeTotal);
                view.setProperty(InventoryView.Property.BURN_TIME, this.fuelTime);
                view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_FUEL, this.fuelTimeTotal);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Tick this furnace
     * <p>This will process the fuel, cook the input item
     * and update the inventory</p>
     */
    @Override
    public void tick() {
        // The fuel is the fire in the middle, not the ItemStack
        // Cook time is the arrow in the Furnace UI

        if (this.fuelTime > 0) { // If fuel still there
            burningTheFuel(); // decrease fuel

            if (canCook()) { // If can cook
                cooking(); // increase cook time

                if (this.cookTime >= this.cookTimeTotal) { // If cook time more than or equal to total cook time
                    this.cookTime = 0; // set cook time to 0

                    finishCook(); // finish cook
                }

            } else // cannot cook
                this.cookTime = 0; // set cook time to 0

        } else // fuel empty
            if (canBurn() && canCook()) { // check can burn fuel and can cook

                igniteFuel(); // start burn fuel and cooking

            } else // the condition here is fuel empty and can't burn fuel (or cook)
                if (this.cookTime > 0) { // If Furnace is cooking

                    if (canCook()) // If ItemStack in cook slot is available (obviously can cook it)
                        this.cookTime -= 5; // decrease cook time because there is no fuel
                    else // or cannot cook it
                        this.cookTime = 0; // set the cook time to 0

                }

        updateInventoryView();
    }

    // Checks to see if the fuel can be burt.
    private boolean canBurn() {
        if (this.fuel == null) return false;
        return this.recipeManager.getFuelByMaterial(this.fuel.getType()) != null;
    }

    // Ignite the fuel of the furnace.
    private void igniteFuel() {
        FurnaceFuel fuel = this.recipeManager.getFuelByMaterial(this.fuel.getType());
        if (fuel == null) return;

        FurnaceFuelIgniteEvent event = new FurnaceFuelIgniteEvent(this, this.fuel, fuel, fuel.getBurnTime());
        event.callEvent();

        if (event.isCancelled()) {
            return;
        }
        int fuelAmount = this.fuel.getAmount();
        if (fuelAmount > 1) {
            this.fuel.setAmount(fuelAmount - 1);
        } else {
            if (this.fuel.getType() == Material.LAVA_BUCKET)
                this.fuel.setType(Material.BUCKET);
            else {
                this.fuel = null;
            }
        }
        int burn = (int) (event.getBurnTime() / furnaceProperties.getFuelMultiplier());
        this.fuelTime = burn;
        this.fuelTimeTotal = burn;
        updateInventory();
    }

    // Burning the fuel of the furnace.
    private void burningTheFuel() {
        fuelTime--;

        FurnaceFuelBurningEvent event = new FurnaceFuelBurningEvent(this, fuelTime);
        event.callEvent();
    }

    // Checks if the input is a valid ingredient of a FurnaceRecipe.
    private boolean canCook() {
        if (this.input == null) return false;
        FurnaceRecipe result = this.recipeManager.getByIngredient(this.input.getType());
        if (result == null) return false;
        this.cookTimeTotal = (int) (result.getCookTime() / furnaceProperties.getCookMultiplier());
        if (this.output == null) return true;

        Material type = this.output.getType();
        if (type == result.getResult()) {
            return this.output.getAmount() < type.getMaxStackSize();
        }
        return false;
    }

    // Finish the cook.
    private void finishCook() {
        FurnaceRecipe result = this.recipeManager.getByIngredient(this.input.getType());
        if (result == null) return;
        ItemStack out;
        if (this.output == null) {
            out = new ItemStack(result.getResult());
        } else {
            out = this.output.clone();
            out.setAmount(out.getAmount() + 1);
        }
        this.experience += result.getExperience();

        FurnaceCookFinishEvent event = new FurnaceCookFinishEvent(this, this.input, out);
        event.callEvent();

        if (event.isCancelled()) {
            return;
        }
        this.output = event.getResult();
        int inputAmount = this.input.getAmount();
        if (inputAmount > 1) {
            this.input.setAmount(inputAmount - 1);
        } else {
            this.input = null;
        }
        updateInventory();
    }

    private void cooking() {
        FurnaceRecipe result = this.recipeManager.getByIngredient(this.input.getType());
        if (result == null) return;

        cookTime++;

        FurnaceCookingEvent event = new FurnaceCookingEvent(this, input);
        event.callEvent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Furnace furnace = (Furnace) o;
        return cookTime == furnace.cookTime && cookTimeTotal == furnace.cookTimeTotal &&
                fuelTime == furnace.fuelTime && fuelTimeTotal == furnace.fuelTimeTotal &&
                Objects.equals(furnaceProperties, furnace.furnaceProperties) && Objects.equals(recipeManager, furnace.recipeManager) &&
                Objects.equals(fuel, furnace.fuel) && Objects.equals(input, furnace.input) &&
                Objects.equals(output, furnace.output) && Objects.equals(inventory, furnace.inventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(furnaceProperties, recipeManager, fuel, input, output, cookTime, cookTimeTotal, fuelTime, fuelTimeTotal, inventory);
    }

    @Override
    public String toString() {
        return "Furnace{" +
                "furnaceProperties=" + furnaceProperties +
                ", recipeManager=" + recipeManager +
                ", inventory=" + inventory +
                ", fuel=" + fuel +
                ", input=" + input +
                ", output=" + output +
                ", cookTime=" + cookTime +
                ", cookTimeTotal=" + cookTimeTotal +
                ", fuelTime=" + fuelTime +
                ", fuelTimeTotal=" + fuelTimeTotal +
                ", experience=" + experience +
                '}';
    }

    /**
     * Serialize this object for yaml
     * <p><b>Internal use only!</b></p>
     *
     * @return Returns serialized map of object
     */
    // Serializer for config
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", this.getName());
        result.put("uuid", this.getUniqueID().toString());
        result.put("properties", this.furnaceProperties);
        result.put("cookTime", this.cookTime);
        result.put("fuelTime", this.fuelTime);
        result.put("xp", this.experience);
        result.put("fuel", this.fuel);
        result.put("input", this.input);
        result.put("output", this.output);
        return result;
    }

}
