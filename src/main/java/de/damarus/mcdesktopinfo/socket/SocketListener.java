package de.damarus.mcdesktopinfo.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
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

                /*
                 * Filter out the important String
                 * 
                 * GET 127.0.0.1:6868/playerCount?rnd=234525 HTTP/1.1
                 * becomes
                 * playerCount
                 * 
                 * The if-else block provides compatibilty with
                 * requests that aren't sent from the gadget.
                 */
                request = request.substring(request.indexOf("/") + 1);
                if(request.contains("?rnd=")) {
                    request = request.substring(0, request.indexOf("?rnd="));
                } else {
                    request = request.substring(0, request.indexOf(" "));
                }

                // Get newest values from server
                values.updateValues();

                // Form response to the given request (Everything is in first line)
                String response = values.get(request);

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