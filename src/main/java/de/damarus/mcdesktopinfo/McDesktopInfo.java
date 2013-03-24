/*
 *  McDesktopInfo - A Bukkit plugin + Windows Sidebar Gadget
 *  Copyright (C) 2012  Damarus
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
