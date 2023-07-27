package com.shanebeestudios.api;

import com.shanebeestudios.api.event.machine.FurnaceExtractEvent;
import com.shanebeestudios.api.machine.*;
import com.shanebeestudios.api.recipe.FurnaceFuel;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;

class FurnaceListener implements Listener {

    private final FurnaceManager furnaceManager;
    private final RecipeManager recipeManager;
    //private final TileManager tileManager;

    FurnaceListener(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.recipeManager = virtualFurnaceAPI.getRecipeManager();
        //this.tileManager = virtualFurnaceAPI.getTileManager();
    }

    @EventHandler
    private void onClickFurnace(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack hand = event.getItem();

        if (hand != null) {
            Furnace furnace = this.furnaceManager.getFurnaceFromItemStack(hand);
            if (furnace != null) {
                event.setCancelled(true);
                furnace.openInventory(event.getPlayer());
                return;
            }
        }

        Block block = event.getClickedBlock();
        if (block == null) return;

/*        Chunk chunk = block.getChunk();
        VirtualChunk virtualChunk = tileManager.getChunk(chunk);
        if (virtualChunk != null) {
            Tile<?> tile = virtualChunk.getTile(block);
            if (tile != null) {
                event.setCancelled(true);
                if (event.getHand() != EquipmentSlot.OFF_HAND) {
                    tile.activate(event.getPlayer());
                }
            }
        }*/
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        HumanEntity clicker = event.getWhoClicked();
        if (holder instanceof Furnace furnace && clicker instanceof Player) {
            int slot = event.getRawSlot();
            // Give XP to player when they extract from the furnace
            if (slot == 2) {
                ItemStack output = furnace.getOutput();
                if (output != null) {
                    int exp = (int) furnace.extractExperience();
                    // Call the furnace extract event
                    FurnaceExtractEvent extractEvent = new FurnaceExtractEvent(furnace, ((Player) clicker), output, exp);
                    extractEvent.callEvent();

                    ((Player) clicker).giveExp(extractEvent.getExperience());
                    event.setCurrentItem(extractEvent.getItemStack());
                }
            }
            // Enable putting custom fuels in the furnaces
            else if (slot == 1) {
                ItemStack cursor = clicker.getItemOnCursor();

                FurnaceFuel fuel = recipeManager.getFuelByMaterial(cursor.getType());
                if (fuel != null && isNotVanillaFuel(cursor)) {
                    ItemStack furnaceFuel = furnace.getFuel();
                    event.setCancelled(true);
                    if (furnaceFuel != null && furnaceFuel.getType() == cursor.getType()) {
                        InventoryView view = event.getView();
                        int fuelAmount = furnaceFuel.getAmount();
                        int cursorAmount = cursor.getAmount();
                        int maxStack = cursor.getType().getMaxStackSize();

                        ItemStack fuelSlot = view.getItem(1);
                        assert fuelSlot != null;
                        if (fuelAmount < maxStack) {
                            int diff = maxStack - fuelAmount;
                            if (cursorAmount < diff) {
                                cursor.setAmount(0);
                                fuelSlot.setAmount(fuelAmount + cursorAmount);
                            } else {
                                cursor.setAmount(cursorAmount - diff);
                                fuelSlot.setAmount(maxStack);
                            }
                            ((Player) clicker).updateInventory();
                        }

                    } else {
                        ItemStack oldCursor = cursor.clone();
                        clicker.setItemOnCursor(furnaceFuel);
                        event.getView().setItem(1, oldCursor);
                    }
                }
            }
        } else if (holder instanceof BrewingStand) {
            Player player = ((Player) event.getWhoClicked());
            ItemStack c = player.getItemOnCursor();
            ItemStack cursor = c.getType() != Material.AIR ? c.clone() : null;
            int slot = event.getRawSlot();
            if (slot >= 0 && slot <= 4) {
                // If shift-clicking we don't want to cancel since this will
                // just be removing items from the inventory not placing in
                if (event.isShiftClick()) {
                    return;
                }
                event.setCancelled(true);
                player.setItemOnCursor(event.getView().getItem(slot));
                event.getView().setItem(slot, cursor);
            }
        }
    }

    private boolean isNotVanillaFuel(ItemStack itemStack) {
        for (FurnaceFuel fuel : FurnaceFuel.getVanillaFuels()) {
            if (fuel.getFuel() == itemStack.getType()) {
                return false;
            }
        }
        return true;
    }
/*
    @EventHandler
    private void onChunkLoad(ChunkLoadEvent event) {
        handleChunk(event.getChunk(), true);
    }

    @EventHandler
    private void onChunkUnload(ChunkUnloadEvent event) {
        handleChunk(event.getChunk(), false);
    }

    private void handleChunk(Chunk chunk, boolean load) {
        VirtualChunk virtualChunk = tileManager.getChunk(chunk);
        if (virtualChunk != null) {
            if (load) {
                if (virtualChunk.isLoaded()) return;
                tileManager.loadChunk(virtualChunk);
            } else {
                if (virtualChunk.isForceLoaded()) return;
                tileManager.unloadChunk(virtualChunk);
            }
        }
    }*/

}
