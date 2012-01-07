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
import de.damarus.mcdesktopinfo.Values;

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
        Values values = new Values(server);

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
                request = request.substring(request.indexOf("/"));
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