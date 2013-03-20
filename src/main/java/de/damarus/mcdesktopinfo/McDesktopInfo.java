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

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.damarus.mcdesktopinfo.socket.SocketListener;

public class McDesktopInfo extends JavaPlugin {

    public static final String PLUGIN_NAME = "McDesktopInfo";

    private static Plugin instance;
    private static Logger logger;
    private Thread listenerThread;

    public void onEnable() {
        instance = this;
        logger = getServer().getLogger();

        // TODO Write better script for writing/updating config
        getConfig().options().copyDefaults(true);
        saveConfig();
        PasswordSystem.setPlugin(this);

        // Check if an admin password is set
        if(getConfig().getString("adminPw").isEmpty()) {
            log("No password set, admin functions are disabled!");
        } else {
            PasswordSystem.digestPWs();
        }

        // Register the command handlers
        CommandHandler chandler = new CommandHandler(this);
        getCommand("mcdesktopinfo").setExecutor(chandler);
        getCommand("mcdi").setExecutor(chandler);

        // Start the listener in a new thread to be able to do other things while listening
        listenerThread = new Thread(new SocketListener(getConfig().getInt("socket-port")));
        listenerThread.start();
    }

    public void onDisable() {
        // Save all pending config changes
        saveConfig();
    }

    public static void log(Object message) {
        logger.info("[" + McDesktopInfo.PLUGIN_NAME + "] " + message.toString());
    }

    public static Plugin getPluginInstance() {
        return instance;
    }
}
