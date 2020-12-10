package de.pierreschwang.antiarmorleak.config;

import de.pierreschwang.antiarmorleak.AntiArmorLeakPlugin;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ArmorConfig extends YamlConfig {

    @Comment("Armor parts which should be excluded from the fake armor")
    private List<String> excludedArmorParts = Collections.emptyList();

    @Comment("If the durability should be faked as 100%")
    private boolean fullDurability = true;

    @Comment("If true protection level should be faked as level 1")
    private boolean minimumProtection = true;

    public ArmorConfig(AntiArmorLeakPlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
    }

    public List<String> getExcludedArmorParts() {
        return excludedArmorParts;
    }

    public boolean isFullDurability() {
        return fullDurability;
    }

    public boolean isMinimumProtection() {
        return minimumProtection;
    }
}
