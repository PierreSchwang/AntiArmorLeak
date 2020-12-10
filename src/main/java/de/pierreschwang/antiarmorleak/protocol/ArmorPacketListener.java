package de.pierreschwang.antiarmorleak.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftVersion;
import de.pierreschwang.antiarmorleak.AntiArmorLeakPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmorPacketListener extends PacketAdapter {

    private final AntiArmorLeakPlugin plugin;
    private final boolean postNetherUpdate;

    public ArmorPacketListener(AntiArmorLeakPlugin plugin) {
        super(plugin, PacketType.Play.Server.ENTITY_EQUIPMENT);
        this.plugin = plugin;
        this.postNetherUpdate = MinecraftVersion.atOrAbove(new MinecraftVersion("1.16"));
        plugin.getLogger().info("Using minecraft version " + MinecraftVersion.getCurrentVersion().getVersion() +
                " [Post-Nether-Update: " + postNetherUpdate + "]");
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPlayer().hasPermission("antiarmorleak.bypass"))
            return;
        ItemStack equipment = getEquipmentPart(event.getPacket());
        if (equipment == null)
            return;
        if(!equipment.getType().name().endsWith("_HELMET") && !equipment.getType().name().endsWith("_CHESTPLATE") &&
                !equipment.getType().name().endsWith("_LEGGINGS") && !equipment.getType().name().endsWith("_SHOES"))
            return;

        if (plugin.getArmorConfig().getExcludedArmorParts().contains(equipment.getType().name()))
            return;

        if (plugin.getArmorConfig().isFullDurability())
            equipment.setDurability(equipment.getType().getMaxDurability());

        if (plugin.getArmorConfig().isMinimumProtection()) {
            ItemMeta meta = equipment.getItemMeta();
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                System.out.println(enchantment.getName());
            }
            if (meta.getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL) > 1) {
                meta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
                equipment.setItemMeta(meta);
            }
        }

        updateEquipmentPart(event.getPacket(), equipment);
    }

    private ItemStack getEquipmentPart(PacketContainer packetContainer) {
        if (!postNetherUpdate) {
            return packetContainer.getItemModifier().read(0);
        }
        return packetContainer.getSlotStackPairLists().read(0).get(0).getSecond();
    }

    private void updateEquipmentPart(PacketContainer packetContainer, ItemStack newStack) {
        if (!postNetherUpdate) {
            packetContainer.getItemModifier().write(0, newStack);
            return;
        }
        packetContainer.getSlotStackPairLists().read(0).get(0).setSecond(newStack);
    }


}
