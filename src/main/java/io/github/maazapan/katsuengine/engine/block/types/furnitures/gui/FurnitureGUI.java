package io.github.maazapan.katsuengine.engine.block.types.furnitures.gui;

import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.maazapan.katsuengine.KatsuEngine;
import io.github.maazapan.katsuengine.engine.block.KatsuBlock;
import io.github.maazapan.katsuengine.engine.block.manager.BlockManager;
import io.github.maazapan.katsuengine.engine.block.types.furnitures.gui.page.PlayerPage;
import io.github.maazapan.katsuengine.manager.gui.GUI;
import io.github.maazapan.katsuengine.utils.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

public class FurnitureGUI extends GUI {

    private final KatsuEngine plugin;
    private final Player player;

    private final Map<UUID, PlayerPage> playerPageMap = new HashMap<>();

    public FurnitureGUI(Player player, KatsuEngine plugin) {
        super("inventorys.furniture-gui", plugin);
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public void handlerMenu(InventoryClickEvent event) {
        BlockManager blockManager = plugin.getBlockManager();
        NBTItem nbtItem = new NBTItem(event.getCurrentItem());

        event.setCancelled(true);

        if (nbtItem.hasKey("katsu_furniture_item")) {
            KatsuBlock furniture = blockManager.getKatsuBlock(nbtItem.getString("katsu_furniture_item"));
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 2);
            player.getInventory().addItem(furniture.getItemStack());
            return;
        }

        if (nbtItem.hasKey("katsu-actions")) {
            List<String> actions = nbtItem.getObject("katsu-actions", List.class);
            PlayerPage page = playerPageMap.get(player.getUniqueId());

            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);

            actions.forEach(action -> {
                if (action.equalsIgnoreCase("[NEXT_PAGE]")) {
                    if (page.getPage() < getMaxPages()) {
                        page.setPage(page.getPage() + 1);
                        init();
                    }
                    return;
                }

                if (action.equalsIgnoreCase("[OLD_PAGE]")) {
                    if (page.getPage() > 1) {
                        page.setPage(page.getPage() - 1);
                        init();
                    }
                    return;
                }

                if (action.equalsIgnoreCase("[CLOSE]")) {
                    player.closeInventory();
                }
            });
        }
    }

    @Override
    public void init() {
        this.create();

        BlockManager blockManager = plugin.getBlockManager();
        List<KatsuBlock> furnitureList = new ArrayList<>(blockManager.getKatsuBlockMap().values());
        PlayerPage page = playerPageMap.get(player.getUniqueId());

        int a = 0;

        for (int i = 36 * (page.getPage() - 1); i < furnitureList.size(); i++) {
            ItemBuilder itemBuilder = new ItemBuilder(furnitureList.get(i).getItemStack().clone());

            itemBuilder.setName("#FF1D1D" + ChatColor.stripColor(itemBuilder.getMeta().getDisplayName()));
            itemBuilder.setLore("&8KatsuEngine", "", "&fID: &7" + furnitureList.get(i).getId(), " ", "&e▸ Click to get block ");

            NBTItem nbtItem = new NBTItem(itemBuilder.toItemStack());
            nbtItem.setString("katsu_furniture_item", furnitureList.get(i).getId());
            nbtItem.applyNBT(itemBuilder.toItemStack());

            getInventory().addItem(itemBuilder.toItemStack());

            a++;

            if (a > 35) {
                break;
            }
        }
        player.openInventory(getInventory());
    }

    @Override
    public int getSlots() {
        return 45;
    }

    public FurnitureGUI addPages() {
        playerPageMap.put(player.getUniqueId(), new PlayerPage(player.getUniqueId(), 1));
        return this;
    }

    public int getMaxPages() {
        BlockManager blockManager = plugin.getBlockManager();
        List<KatsuBlock> furnitureList = new ArrayList<>(blockManager.getKatsuBlockMap().values());

        if (furnitureList.size() % 36 == 0) {
            return furnitureList.size() / 36;
        }
        return (furnitureList.size() / 36) + 1;
    }

    public Map<UUID, PlayerPage> getPlayerPageMap() {
        return playerPageMap;
    }
}
