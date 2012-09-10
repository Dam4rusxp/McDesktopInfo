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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import org.bukkit.Server;
import de.damarus.mcdesktopinfo.McDesktopInfo;
import de.damarus.mcdesktopinfo.RequestHandler;

public class SocketListener implements Runnable {

    private Server server;
    private ServerSocket serverSocket;
    private boolean breakLoop = false;

    public SocketListener(int port, Server server) {
        try {
            this.server = server;

            McDesktopInfo.log("Starting listener on port " + port + ".");

            serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            McDesktopInfo.log("Could not start socket on port " + port + ".");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        RequestHandler values = new RequestHandler(server);

        while(!breakLoop) {
            try {
                // Wait for connection
                Socket socket = serverSocket.accept();

                // Get in and out streams
                BufferedReader sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream sOut = new DataOutputStream(socket.getOutputStream());

                String request = sIn.readLine();
                HashMap<String, String> params = new HashMap<String, String>();

                // Splitting into real request and parameters
                request = request.substring(request.indexOf("/") + 1);
                if(request.contains(" HTTP/")) request = request.substring(0, request.indexOf(" HTTP/"));

                String[] paramsWithValue = request.split("[?]");

                boolean skippedFirst = false;
                for(String s : paramsWithValue) {
                    if(!skippedFirst) {
                        skippedFirst = true;
                        continue;
                    }
                    String[] x = s.split("[=]");
                    params.put(x[0], x[1]);
                }

                params.put("gadgetIp", socket.getInetAddress().getHostAddress());

                // Get newest values from server
                values.updateValues();

                // Form response to the given request (Everything is in first line)
                String response = values.get(paramsWithValue[0], params);

                sOut.writeBytes(response); // Give the response back to the client
                sOut.flush(); // Make sure, that data is sent now

                socket.close(); // Transmission done...
            } catch(IOException e) {
                McDesktopInfo.log("Listening on port " + serverSocket.getLocalPort() + " was interrupted.");
                e.printStackTrace();
            }
        }
    }
}