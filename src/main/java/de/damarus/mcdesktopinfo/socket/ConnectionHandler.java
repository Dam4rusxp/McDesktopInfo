package de.damarus.mcdesktopinfo.socket;

import java.io.*;
import java.net.Socket;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import de.damarus.mcdesktopinfo.PasswordSystem;
import de.damarus.mcdesktopinfo.queries.Query;
import de.damarus.mcdesktopinfo.queries.Query.QueryEnum;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ConnectionHandler implements Runnable {

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int contentLength = -1;
            String token = "Content-Length: ";

            String line;
            do {
                line = sIn.readLine();
                if(line.startsWith(token)) {
                    contentLength = Integer.parseInt(line.substring(token.length()));
                    McDesktopInfo.log("Detected Content-Length of " + contentLength + " bytes.");
                }
            } while (line.length() != 0);

            char[] cData = new char[contentLength];
            sIn.read(cData);
            String requestBody = new String(cData);

            JSONObject params = (JSONObject)JSONValue.parse(requestBody);
            JSONObject answer = get(params);

            Writer sOut = new OutputStreamWriter(socket.getOutputStream());
            answer.writeJSONString(sOut);
            sOut.flush();

            socket.close(); // Transmission done...
        } catch (IOException e) {
            McDesktopInfo.log("Caught an exception while answering a query.");
            e.printStackTrace();
        }
    }

    public JSONObject get(JSONObject params) {
        JSONObject answer = new JSONObject();
        try {
            Query queryObj = QueryEnum.valueOf((String)answer.get("action")).getQueryObj();

            // TODO Move this to the Kick class
            if(queryObj.equals(QueryEnum.KICK.getQueryObj())) {
                // Report to serverlog that a kick was queried and only log the used password if it was wrong
                McDesktopInfo.log("The IP " + params.get("gadgetIp") + " sent a query to kick the player " + params.get("player") +
                    ((PasswordSystem.checkAdminPW((String)params.get("adminPw")) ? "" : " using a wrong password: " + params.get("adminPw"))));
            }

            if(!queryObj.isDisabled() && (queryObj.isUserExecutable() || PasswordSystem.checkAdminPW((String)params.get("adminPw")))) answer.putAll(queryObj.run(params));
        } catch (IllegalArgumentException e) {
            McDesktopInfo.log("Received unknown query \"" + params.get("action") + "\"");
        }

        return answer;
    }
}
