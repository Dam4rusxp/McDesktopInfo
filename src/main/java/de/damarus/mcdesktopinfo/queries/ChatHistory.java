package de.damarus.mcdesktopinfo.queries;

import java.util.LinkedList;
import java.util.List;

import de.damarus.mcdesktopinfo.McDesktopInfo;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChatHistory extends Query implements Listener {

    List<String> chatHistory = new LinkedList<String>();

    protected ChatHistory(String query, boolean runOnRefresh) {
        super(query, runOnRefresh);
        Bukkit.getServer().getPluginManager().registerEvents(this, McDesktopInfo.getPluginInstance());
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {
        if(!isDisabled()) {
            addMessage(e.getPlayer().getName() + ": " + e.getMessage());

        }
    }

    @EventHandler
    public void onServerMessage(ServerCommandEvent e) {
        if(!isDisabled() && e.getCommand().startsWith("say ")) {
            addMessage("[Server] " + e.getCommand().substring(4));
        }
    }

    private void addMessage(String msg) {
        chatHistory.add(msg);

        // Remove oldest message if history limit is crossed
        if (chatHistory.size() > McDesktopInfo.getPluginInstance().getConfig().getInt("maxChatHistory")) chatHistory.remove(0);
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
