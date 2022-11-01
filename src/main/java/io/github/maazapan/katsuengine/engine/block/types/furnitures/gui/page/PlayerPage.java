package io.github.maazapan.katsuengine.engine.block.types.furnitures.gui.page;

import java.util.UUID;

public class PlayerPage {

    private final UUID uuid;
    private int page;

    public PlayerPage(UUID uuid, int page) {
        this.uuid = uuid;
        this.page = page;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
