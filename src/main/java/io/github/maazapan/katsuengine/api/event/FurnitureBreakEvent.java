package io.github.maazapan.katsuengine.api.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FurnitureBreakEvent extends Event implements Cancellable {

    private final Player player;
    private final EnumWrappers.PlayerDigType digType;

    private boolean cancel;
    private static final HandlerList handlerList = new HandlerList();

    public FurnitureBreakEvent(Player player, EnumWrappers.PlayerDigType digType) {
        this.player = player;
        this.digType = digType;
        this.cancel = false;
    }

    public Player getPlayer() {
        return player;
    }

    public EnumWrappers.PlayerDigType getDigType() {
        return digType;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
