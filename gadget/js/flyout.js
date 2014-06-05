function initFlyout() {
    // The settings need to be loaded seperately for gadget and flyout
    settings.load();
    refreshPlayerList();
}

function refreshPlayerList() {
    sendQuery({"action": "playerList"}, function(answer) {
        var htmlList = "";
        var playerList = answer["playerList"];
        for (var i = 0; i < playerList.length; i++) {
            htmlList += playerList[i];
            htmlList += " (<a href=\"#\" onclick=\"kickPlayer(\'" + playerList[i] + "\'); return false;\">Kick</a>)" + "<br />";
        }

        System.Gadget.Flyout.document.getElementById("playerList").innerHTML = htmlList;
    });
}

function kickPlayer(player) {
    var content = {
        "action": "kick",
        "player": player
    };

    sendQuery(content, function() {
        refresh();
        refreshPlayerList();
    });
}
