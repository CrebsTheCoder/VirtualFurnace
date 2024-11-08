package com.shanebeestudios.api.recipe;

import com.shanebeestudios.api.util.Util;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BrewingRecipe extends Recipe {

    private static final List<BrewingRecipe> VANILLA_BREWING_RECIPES = new ArrayList<>();

    public static final BrewingRecipe AWKWARD_POTION = get("awkward", Material.NETHER_WART, PotionType.WATER, PotionType.AWKWARD, false, false);

    public static final BrewingRecipe POTION_OF_SWIFTNESS = getBase("swiftness", Material.SUGAR, PotionType.SPEED);
    public static final BrewingRecipe POTION_OF_SWIFTNESS_II = getStrong("strong_swiftness", Material.GLOWSTONE_DUST, POTION_OF_SWIFTNESS);
    public static final BrewingRecipe POTION_OF_SWIFTNESS_X = getLong("long_swiftness", Material.REDSTONE, POTION_OF_SWIFTNESS);

    public static final BrewingRecipe POTION_OF_SLOWNESS = get("slowness", Material.FERMENTED_SPIDER_EYE, PotionType.SPEED, PotionType.SLOWNESS, false, false);
    public static final BrewingRecipe POTION_OF_SLOWNESS_II = getStrong("strong_slowness", Material.GLOWSTONE_DUST, POTION_OF_SLOWNESS);
    public static final BrewingRecipe POTION_OF_SLOWNESS_X = getLong("long_slowness", Material.REDSTONE, POTION_OF_SLOWNESS);
    public static final BrewingRecipe POTION_OF_SLOWNESS_X2 = getLong("long_slowness_2", Material.FERMENTED_SPIDER_EYE, POTION_OF_SWIFTNESS_X);

    public static final BrewingRecipe POTION_OF_LEAPING = getBase("leaping", Material.RABBIT_FOOT, PotionType.JUMP);
    public static final BrewingRecipe POTION_OF_LEAPING_II = getStrong("strong_leaping", Material.GLOWSTONE_DUST, POTION_OF_LEAPING);
    public static final BrewingRecipe POTION_OF_LEAPING_X = getLong("long_leaping", Material.REDSTONE, POTION_OF_LEAPING);
    public static final BrewingRecipe POTION_OF_SLOWNESS_X3 = getLong("long_slowness_3", Material.FERMENTED_SPIDER_EYE, POTION_OF_LEAPING_X);

    public static final BrewingRecipe POTION_OF_STRENGTH = getBase("strength", Material.BLAZE_POWDER, PotionType.STRENGTH);
    public static final BrewingRecipe POTION_OF_STRENGTH_II = getStrong("strong_strength", Material.GLOWSTONE_DUST, POTION_OF_STRENGTH);
    public static final BrewingRecipe POTION_OF_STRENGTH_X = getLong("long_strength", Material.REDSTONE, POTION_OF_STRENGTH);

    public static final BrewingRecipe POTION_OF_HEALING = getBase("healing", Material.GLISTERING_MELON_SLICE, PotionType.INSTANT_HEAL);
    public static final BrewingRecipe POTION_OF_HEALING_II = getStrong("strong_healing", Material.GLOWSTONE_DUST, POTION_OF_HEALING);

    public static final BrewingRecipe POTION_OF_POISON = getBase("poison", Material.SPIDER_EYE, PotionType.POISON);
    public static final BrewingRecipe POTION_OF_POISON_II = getStrong("strong_poison", Material.GLOWSTONE_DUST, POTION_OF_POISON);
    public static final BrewingRecipe POTION_OF_POISON_X = getLong("long_poison", Material.REDSTONE, POTION_OF_POISON_II);

    public static final BrewingRecipe POTION_OF_HARMING_1 = get("harming", Material.FERMENTED_SPIDER_EYE, POTION_OF_POISON, PotionType.INSTANT_DAMAGE, false, false);
    public static final BrewingRecipe POTION_OF_HARMING_2 = get("harming_2", Material.FERMENTED_SPIDER_EYE, POTION_OF_HEALING, PotionType.INSTANT_DAMAGE, false, false);
    public static final BrewingRecipe POTION_OF_HARMING_II = getStrong("strong_harming", Material.FERMENTED_SPIDER_EYE, POTION_OF_POISON_II);
    public static final BrewingRecipe POTION_OF_HARMING_II_2 = getStrong("strong_harming_2", Material.FERMENTED_SPIDER_EYE, POTION_OF_HEALING_II);

    public static final BrewingRecipe POTION_OF_REGEN = getBase("regen", Material.GHAST_TEAR, PotionType.REGEN);
    public static final BrewingRecipe POTION_OF_REGEN_II = getStrong("strong_regen", Material.GLOWSTONE_DUST, POTION_OF_REGEN);
    public static final BrewingRecipe POTION_OF_REGEN_X = getLong("long_regen", Material.REDSTONE, POTION_OF_REGEN_II);

    public static final BrewingRecipe POTION_OF_FIRE_RESIST = getBase("fire_resistance", Material.MAGMA_CREAM, PotionType.FIRE_RESISTANCE);
    public static final BrewingRecipe POTION_OF_FIRE_RESIST_X = getLong("long_fire_resistance", Material.REDSTONE, POTION_OF_FIRE_RESIST);

    public static final BrewingRecipe POTION_OF_WATER_BREATHING = getBase("water_breathing", Material.PUFFERFISH, PotionType.WATER_BREATHING);
    public static final BrewingRecipe POTION_OF_WATER_BREATHING_X = getLong("long_water_breathing", Material.REDSTONE, POTION_OF_WATER_BREATHING);

    public static final BrewingRecipe POTION_OF_NIGHT_VISION = getBase("night_vision", Material.GOLDEN_CARROT, PotionType.NIGHT_VISION);
    public static final BrewingRecipe POTION_OF_NIGHT_VISION_X = getLong("long_night_vision", Material.REDSTONE, POTION_OF_NIGHT_VISION);

    public static final BrewingRecipe POTION_OF_INVISIBILITY = getBase("invisibility", Material.FERMENTED_SPIDER_EYE, POTION_OF_NIGHT_VISION);
    public static final BrewingRecipe POTION_OF_INVISIBILITY_X = getLong("long_invisibility", Material.FERMENTED_SPIDER_EYE, POTION_OF_NIGHT_VISION_X);
    public static final BrewingRecipe POTION_OF_INVISIBILITY_X2 = getLong("long_invisibility_2", Material.REDSTONE, POTION_OF_INVISIBILITY);

    public static final BrewingRecipe POTION_OF_TURTLE_MASTER = getBase("turtle_master", Material.TURTLE_HELMET, PotionType.TURTLE_MASTER);
    public static final BrewingRecipe POTION_OF_TURTLE_MASTER_II = getStrong("strong_turtle_master", Material.GLOWSTONE_DUST, POTION_OF_TURTLE_MASTER);
    public static final BrewingRecipe POTION_OF_TURTLE_MASTER_X = getLong("long_turtle_master", Material.REDSTONE, POTION_OF_TURTLE_MASTER);

    public static final BrewingRecipe POTION_OF_SLOW_FALLING = getBase("slow_falling", Material.PHANTOM_MEMBRANE, PotionType.SLOW_FALLING);
    public static final BrewingRecipe POTION_OF_SLOW_FALLING_X = getLong("long_slow_falling", Material.REDSTONE, POTION_OF_SLOW_FALLING);

    public static final BrewingRecipe POTION_OF_WEAKNESS = get("weakness", Material.FERMENTED_SPIDER_EYE, PotionType.WATER, PotionType.WEAKNESS, false, false);
    public static final BrewingRecipe POTION_OF_WEAKNESS_X = getLong("long_weakness", Material.REDSTONE, POTION_OF_WEAKNESS);

    private final ItemStack ingredient;
    private final ItemStack inputBottle;
    private final ItemStack outputBottle;
    private final int cookTime;

    public BrewingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack ingredient, @NotNull ItemStack inputBottle, @NotNull ItemStack outputBottle) {
        this(key, ingredient, inputBottle, outputBottle, 400);
    }

    public BrewingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack ingredient, @NotNull ItemStack inputBottle, @NotNull ItemStack outputBottle, int cookTime) {
        super(key, outputBottle);
        this.ingredient = ingredient;
        this.inputBottle = inputBottle;
        this.outputBottle = outputBottle;
        this.cookTime = cookTime;
    }

    private static BrewingRecipe get(String key, Material ingredient, PotionType input, PotionType out, boolean extend, boolean upgrade) {
        ItemStack inputItem = new ItemStack(Material.POTION);
        PotionMeta inputMeta = (PotionMeta) inputItem.getItemMeta();
        assert inputMeta != null;
        inputMeta.setBasePotionData(new PotionData(input));
        inputItem.setItemMeta(inputMeta);

        ItemStack outputItem = new ItemStack(Material.POTION);
        PotionMeta outputMeta = (PotionMeta) outputItem.getItemMeta();
        assert outputMeta != null;
        outputMeta.setBasePotionData(new PotionData(out, extend, upgrade));
        outputItem.setItemMeta(outputMeta);

        NamespacedKey nameKey = Util.getKey("mc_potion_rec_" + key);
        ItemStack ing = new ItemStack(ingredient);
        BrewingRecipe recipe = new BrewingRecipe(nameKey, ing, inputItem, outputItem, 400);
        VANILLA_BREWING_RECIPES.add(recipe);
        return recipe;
    }

    private static BrewingRecipe get(String key, Material ingredient, BrewingRecipe in, boolean extend, boolean upgrade) {
        return get(key, ingredient, in, ((PotionMeta) in.getOutputBottle().getItemMeta()).getBasePotionData().getType(), extend, upgrade);
    }

    private static BrewingRecipe get(String key, Material ingredient, BrewingRecipe in, PotionType out, boolean extend, boolean upgrade) {
        ItemStack output = new ItemStack(Material.POTION);
        PotionMeta outputMeta = (PotionMeta) output.getItemMeta();
        assert outputMeta != null;
        outputMeta.setBasePotionData(new PotionData(out, extend, upgrade));
        output.setItemMeta(outputMeta);

        NamespacedKey namekey = Util.getKey("mc_potion_rec_" + key);
        ItemStack ing = new ItemStack(ingredient);
        BrewingRecipe recipe = new BrewingRecipe(namekey, ing, in.outputBottle, output, 400);
        VANILLA_BREWING_RECIPES.add(recipe);
        return recipe;
    }

    private static BrewingRecipe getBase(String key, Material ing, PotionType potionType) {
        return get(key, ing, PotionType.AWKWARD, potionType, false, false);
    }

    private static BrewingRecipe getBase(String key, Material ing, BrewingRecipe in) {
        return get(key, ing, in, false, false);
    }

    private static BrewingRecipe getStrong(String key, Material ing, BrewingRecipe in) {
        return get(key, ing, in, false, true);
    }

    private static BrewingRecipe getLong(String key, Material ing, BrewingRecipe in) {
        return get(key, ing, in, true, false);
    }

    public static List<BrewingRecipe> getVanillaBrewingRecipes() {
        return VANILLA_BREWING_RECIPES;
    }

    public ItemStack getIngredient() {
        return ingredient;
    }

    public ItemStack getInputBottle() {
        return inputBottle;
    }

    public ItemStack getOutputBottle() {
        return outputBottle;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrewingRecipe that = (BrewingRecipe) o;
        return cookTime == that.cookTime && Objects.equals(ingredient, that.ingredient) &&
                Objects.equals(inputBottle, that.inputBottle) && Objects.equals(outputBottle, that.outputBottle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, inputBottle, outputBottle, cookTime);
    }

    @Override
    public String toString() {
        return "BrewingRecipe{" +
                "key=" + key +
                ", ingredient=" + ingredient +
                ", inputBottle=" + inputBottle +
                ", outputBottle=" + outputBottle +
                ", cookTime=" + cookTime +
                '}';
    }

}
