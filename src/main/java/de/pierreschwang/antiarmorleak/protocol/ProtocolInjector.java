package de.pierreschwang.antiarmorleak.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import de.pierreschwang.antiarmorleak.AntiArmorLeakPlugin;

public class ProtocolInjector {

    public void inject(AntiArmorLeakPlugin plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new ArmorPacketListener(plugin));
    }

}
