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

package de.damarus.mcdesktopinfo.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

import org.bukkit.Server;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import de.damarus.mcdesktopinfo.QueryHandler;

public class ConnectionHandler implements Runnable {

    private Socket socket;
    private Server server;
    private QueryHandler values;

    public ConnectionHandler(Socket socket, Server server, QueryHandler values) {
        this.socket = socket;
        this.server = server;
        this.values = values;
    }

    @Override
    public void run() {
        try {
            // Get in and out streams
            BufferedReader sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream sOut = new DataOutputStream(socket.getOutputStream());

            String query = sIn.readLine();
            HashMap<String, String> params = new HashMap<String, String>();

            // Splitting into real query and parameters
            query = query.substring(query.indexOf("/") + 1);
            if(query.contains(" HTTP/")) query = query.substring(0, query.indexOf(" HTTP/"));

            String[] paramsWithValue = query.split("[?]");

            for(int i = 1; i < paramsWithValue.length; i++) {
                String[] param = paramsWithValue[i].split("[=]");

                // Skip parameters without value
                if(param.length == 1) {
                    continue;
                }

                params.put(param[0], param[1]);
            }

            params.remove("rnd");
            params.put("gadgetIp", socket.getInetAddress().getHostAddress());

            // Get newest values from server
            values.updateValues();

            // Form response to the given query (Everything is in first line)
            String response = values.get(paramsWithValue[0], params);

            sOut.writeBytes(response); // Give the response back to the client
            sOut.flush(); // Make sure, that data is sent now

            socket.close(); // Transmission done...
        } catch (IOException e) {
            McDesktopInfo.log("Caught an exception while answering a query.");
            e.printStackTrace();
        }
    }
}