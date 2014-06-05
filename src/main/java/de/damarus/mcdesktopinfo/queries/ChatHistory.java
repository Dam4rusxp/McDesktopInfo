package de.damarus.mcdesktopinfo.queries;

import java.util.LinkedList;
import java.util.List;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChatHistory extends Query {

    List<String> chatHistory = new LinkedList<String>();

    protected ChatHistory(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {
        if(!isDisabled()) {
            chatHistory.add(e.getPlayer() + ": " + e.getMessage());

            // Remove oldest message if history limit is crossed
            if (chatHistory.size() > McDesktopInfo.getPluginInstance().getConfig().getInt("maxChatHistory"))
                chatHistory.remove(0);
        }
    }

    @Override
    public JSONObject run(JSONObject params) {
        JSONObject answer = new JSONObject();
        JSONArray messages = new JSONArray();

        for(String msg : chatHistory) messages.add(msg);

        answer.put(getQueryString(), messages);

        return answer;
    }
}
