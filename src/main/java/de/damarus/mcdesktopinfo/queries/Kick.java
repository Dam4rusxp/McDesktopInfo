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

package de.damarus.mcdesktopinfo.queries;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class Kick extends Query {

    protected Kick(String query) {
        super(query, false);
    }

    @Override
    public String exec(HashMap<String, String> params) {
        // Report to serverlog that a kick was queried
        // TODO Remove cleartext password
        McDesktopInfo.log("The IP " + params.get("gadgetIp") + " sent a query to kick the player " + params.get("player") + " using the password " + params.get("adminPw"));

        Player player = getServer().getPlayer(params.get("player"));
        if(player != null) {
            player.kickPlayer("Kicked with McDesktopInfo");
            return params.get("player");
        }
        return "";
    }
}
