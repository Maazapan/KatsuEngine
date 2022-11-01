package io.github.maazapan.katsuengine.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KatsuBlockInteractEvent extends Event implements Cancellable {

    private final Player player;
    private final Block block;

    private boolean cancel;

    private static final HandlerList handlerList = new HandlerList();

    public KatsuBlockInteractEvent(Player player, Block block) {
        this.player = player;
        this.block = block;
        this.cancel = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
