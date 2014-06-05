function initFlyout() {
    // The settings need to be loaded seperately for gadget and flyout
    settings.load();
    refreshPlayerList();
}

function refreshPlayerList() {
    sendQuery({"action": "playerList"}, function(playerList) {
        var htmlList = "";
        for (var player in playerList) {
            htmlList += player["name"];
            htmlList += " (<a href=\"#\" onclick=\"kickPlayer(\'" + player["name"] + "\'); return false;\">Kick</a>)" + "<br />";
        }

        System.Gadget.Flyout.document.getElementById("playerList").innerHTML = htmlList;
    });
}

function kickPlayer(player) {
    var content = {
        "action": "kick";
        "player": player;
    }

    sendQuery(body, function() {
        refresh();
        refreshPlayerList();
    });
}
