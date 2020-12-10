package de.pierreschwang.antiarmorleak;

import de.pierreschwang.antiarmorleak.config.ArmorConfig;
import de.pierreschwang.antiarmorleak.protocol.ProtocolInjector;
import de.pierreschwang.antiarmorleak.updater.UpdateChecker;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiArmorLeakPlugin extends JavaPlugin {

    private static final int METRICS_PLUGIN_ID = 9612;

    private ArmorConfig armorConfig;

    @Override
    public void onEnable() {
        // Ensure that ProtocolLib is available
        if(!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe(getDescription().getName() + " requires ProtocolLib to run!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new UpdateChecker().checkForUpdates(this);

        armorConfig = new ArmorConfig(this);
        try {
            armorConfig.init();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        new ProtocolInjector().inject(this);

        Metrics metrics = new Metrics(this, METRICS_PLUGIN_ID);
        metrics.addCustomChart(new Metrics.SimplePie("minimum_protection", () ->
                armorConfig.isMinimumProtection() ? "Enabled" : "Disabled"));
        metrics.addCustomChart(new Metrics.SimplePie("full_durability", () ->
                armorConfig.isFullDurability() ? "Enabled" : "Disabled"));
    }

    @Override
    public void onDisable() {

    }

    public ArmorConfig getArmorConfig() {
        return armorConfig;
    }
}
