package io.github.maazapan.katsuengine.integrations.terra;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.inject.annotations.Inject;
import io.github.maazapan.katsuengine.integrations.terra.manager.AddonManager;
import org.slf4j.Logger;

public class KatsuAddon implements AddonInitializer {

    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    @Inject
    private Logger logger;

    @Override
    public void initialize() {
        logger.info("Katsu Engine initialization!");

        AddonManager addonManager = new AddonManager(platform, addon);
        addonManager.registerStructures();
    }
}
