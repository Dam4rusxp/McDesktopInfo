package de.damarus.mcdesktopinfo.socket;

import java.io.*;
import java.net.Socket;

import de.damarus.mcdesktopinfo.McDesktopInfo;
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
                if(line.startsWith(token)) contentLength = Integer.parseInt(line.substring(token.length()));
            } while (line.length() != 0);

            char[] cData = new char[contentLength];
            sIn.read(cData);
            String requestBody = new String(cData);

            JSONObject params = (JSONObject)JSONValue.parse(requestBody);

            params.put("gadgetIp", socket.getInetAddress().toString());

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
            String query = params.get("action").toString();
            Query queryObj = QueryEnum.valueOf(query.toUpperCase()).getQueryObj();

            answer.putAll(queryObj.runSecure(params));
        } catch (Exception e) {
            McDesktopInfo.log("An error occurred while executing the query \"" + params.get("action") + "\"!");
        }

        return answer;
    }
}
