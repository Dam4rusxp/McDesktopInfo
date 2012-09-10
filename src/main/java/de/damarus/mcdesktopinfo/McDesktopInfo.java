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

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import de.damarus.mcdesktopinfo.socket.SocketListener;
import java.util.logging.Logger;

public class McDesktopInfo extends JavaPlugin {

    private static Logger logger;
    private Thread        listenerThread;

    public void onEnable() {
        logger = getServer().getLogger();

        // TODO Write better script for writing/updating config
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        PasswordSystem.setConfig(getConfig());
        PasswordSystem.digestPWs();
        saveConfig();

        // Check if an admin password is set
        if(getConfig().getString("adminPw").isEmpty()) {
            log("No password set, admin functions are disabled!");
        }

        // Register the command handler
        getCommand("mcdesktopinfo").setExecutor(new CommandHandler(this));

        // Start the listener in a new thread to be able to do other things while listening
        listenerThread = new Thread(new SocketListener(getConfig().getInt("socket-port"), getServer()));
        listenerThread.start();

        log("Enabled!");
    }

    public void onDisable() {
        logger.info("Disabled!");
    }

    public static void log(String message) {
        logger.info("[" + Config.PLUGIN_NAME + "] " + message);
    }

    public static void respond(CommandSender sender, String message) {
        sender.sendMessage("[" + Config.PLUGIN_NAME + "] " + message);
    }
}