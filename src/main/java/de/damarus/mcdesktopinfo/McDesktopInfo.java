package de.damarus.mcdesktopinfo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.damarus.mcdesktopinfo.queries.Query.QueryEnum;
import de.damarus.mcdesktopinfo.socket.SocketListener;

public class McDesktopInfo extends JavaPlugin {

    public static final String PLUGIN_NAME = "McDesktopInfo";

    private Thread listenerThread;
    private SocketListener listener;

    public void onEnable() {
        // TODO Write better script for writing/updating config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Register the command handlers
        CommandHandler chandler = new CommandHandler(this);
        getCommand("mcdesktopinfo").setExecutor(chandler);
        getCommand("mcdi").setExecutor(chandler);
    }

    public void onDisable() {
        // Save all pending config changes
        saveConfig();
        listener.stopListener();
    }

    public void reloadConfig() {
        super.reloadConfig();

        List<String> admin = getConfig().getStringList("adminQueries");
        List<String> user = getConfig().getStringList("userQueries");
        List<String> disabled = getConfig().getStringList("disabledQueries");

        QueryEnum[] queryObjects = QueryEnum.values();
        List<String> queries = new ArrayList<String>();
        for(QueryEnum q : queryObjects) {
            queries.add(q.getQueryObj().getQuery());
        }

        // Remove nonexistant queries
        admin.retainAll(queries);
        user.retainAll(queries);
        disabled.retainAll(queries);

        // Remove query from user list if already configured in admin list
        user.removeAll(admin);

        // Disable not configured queries
        for(int q = 0; q < queries.size(); q++) {
            if(!(user.contains(queries.get(q)) || admin.contains(queries.get(q)) || disabled.contains(queries.get(q)))) {
                disabled.add(queries.get(q));
            }
        }

        if(getConfig().getString("adminPw").isEmpty()) {
            log("No password set, admin functions are disabled!");
        } else {
            PasswordSystem.digestPWs();
        }

        if(listener == null || getConfig().getInt("socket-port") != listener.getPort()) restartListener();

        getConfig().set("adminQueries", admin);
        getConfig().set("userQueries", user);
        getConfig().set("disabledQueries", disabled);
        saveConfig();
    }

    public void restartListener() {
        if(listener != null) {
            log("Stopping listener...");
            listener.stopListener();
            try {
                listenerThread.join();
            } catch (InterruptedException e) {
                log("An error occurred while waiting for the listener to stop.");
                e.printStackTrace();
            }
        }
        listener = new SocketListener(getConfig().getInt("socket-port"));
        listenerThread = new Thread(listener);
        listenerThread.start();
    }

    public static void log(Object message) {
        Bukkit.getServer().getLogger().info("[" + McDesktopInfo.PLUGIN_NAME + "] " + message.toString());
    }

    public static Plugin getPluginInstance() {
        return Bukkit.getServer().getPluginManager().getPlugin(PLUGIN_NAME);
    }
}
