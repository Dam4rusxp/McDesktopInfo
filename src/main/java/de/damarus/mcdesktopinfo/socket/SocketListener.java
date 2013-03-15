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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.damarus.mcdesktopinfo.McDesktopInfo;

public class SocketListener implements Runnable {

    private ServerSocket serverSocket;
    private boolean breakLoop = false;

    public SocketListener(int port) {
        McDesktopInfo.log("Starting listener on port " + port + ".");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            McDesktopInfo.log("Could not start socket on port " + port + ".");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(!breakLoop) {
            try {
                // Wait for connection
                Socket socket = serverSocket.accept();

                // Handle connection in a new thread
                new Thread(new ConnectionHandler(socket)).start();
            } catch (IOException e) {
                McDesktopInfo.log("Listening on port " + serverSocket.getLocalPort() + " was interrupted.");
                e.printStackTrace();
            } catch (Exception e) {
                McDesktopInfo.log("An unknown error has happened while accepting the connection from a client.");
                e.printStackTrace();
            }
        }
    }
}
