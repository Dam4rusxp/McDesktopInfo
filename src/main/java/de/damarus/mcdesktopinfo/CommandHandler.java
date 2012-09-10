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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler implements CommandExecutor {

    JavaPlugin plugin;

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            helpMessage(sender);
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                helpMessage(sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("reload")) {
                McDesktopInfo.respond(sender, "Reloading config...");
                plugin.reloadConfig();
                McDesktopInfo.respond(sender, "Done!");
                return true;
            }
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("setPassword")) {
                McDesktopInfo.respond(sender, "Changing the admin password...");

                plugin.getConfig().set("adminPw", args[1]);
                PasswordSystem.digestPWs();
                plugin.saveConfig();

                McDesktopInfo.respond(sender, "Done!");
            }
        }
        return false;
    }

    public void helpMessage(CommandSender sender) {
        // @formatter:off
        McDesktopInfo.respond(sender,
            "---McDesktopInfo help message---\n" +
            "<...> - required argument | [...] - optional argument\n" +
            "/mcdi help                       Display this message\n" +
            "/mcdi reload                        Reload the config\n" +
            "/mcdi setPassword <pw>       Set a new admin password\n");
        // @formatter:on
    }
}