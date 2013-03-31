package de.damarus.mcdesktopinfo.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
            } catch (Exception e) {
                McDesktopInfo.log("An unknown error has happened while accepting the connection from a client.");
                e.printStackTrace();
            }
        }
    }

    public void stopListener() {
        breakLoop = true;
        try {
            serverSocket.close();
        } catch (SocketException e) {} catch (IOException e) {
            McDesktopInfo.log("An IO error occurred when stopping the listener.");
            e.printStackTrace();
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
