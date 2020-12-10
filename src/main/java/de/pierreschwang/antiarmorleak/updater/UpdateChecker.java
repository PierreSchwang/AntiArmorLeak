package de.pierreschwang.antiarmorleak.updater;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import de.pierreschwang.antiarmorleak.AntiArmorLeakPlugin;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

    private static final int SPIGOT_RESOURCE_ID = 86611;
    private static final String REMOTE_URL = "https://api.spigotmc.org/legacy/update.php?resource=" + SPIGOT_RESOURCE_ID;

    public void checkForUpdates(AntiArmorLeakPlugin plugin) {
        String localVersion = plugin.getDescription().getVersion();
        //noinspection UnstableApiUsage - (Never had any problems with Futures...)
        Futures.addCallback(getLatestVersion(plugin), new FutureCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(localVersion.equals(s))
                    return;
                plugin.getLogger().info("A new version for " + plugin.getDescription().getName() + " is available! " +
                        "[" + localVersion + " -> " + s + "]");
            }

            @Override
            public void onFailure(Throwable throwable) {
                plugin.getLogger().severe("Could not look for new updates!");
                plugin.getLogger().throwing(UpdateChecker.class.getName(), "getLatestVersion", throwable);
            }
        });
    }

    private ListenableFuture<String> getLatestVersion(AntiArmorLeakPlugin plugin) {
        SettableFuture<String> future = SettableFuture.create();
        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try(final BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(REMOTE_URL).openStream()))) {
                future.set(reader.readLine());
            } catch (IOException e) {
                future.setException(e);
            }
        });
        return future;
    }

}
