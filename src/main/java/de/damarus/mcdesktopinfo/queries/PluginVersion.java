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

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class PluginVersion extends Query {

    protected PluginVersion(String query) {
        super(query, true);
    }

    @Override
    protected String exec(HashMap<String, String> params) {
        return McDesktopInfo.getPluginInstance().getDescription().getVersion();
    }
}
