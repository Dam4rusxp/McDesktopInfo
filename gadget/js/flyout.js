function initFlyout() {
    // The settings need to be loaded seperately for gadget and flyout
    settings.load();
    refreshPlayerList();
}

function refreshPlayerList() {
    sendQuery("playerList", function(response) {
        players = response.split("+");

        var listTemp = "";
        for(i = 0; i < players.length; i++) {
            if(players[i] != undefined && players[i] != "") {
                listTemp += players[i] + " (<a href=\"#\" onclick=\"kickPlayer(\'" + players[i] + "\'); return false;\">Kick</a>)" + "<br />";
            }
        }

        System.Gadget.Flyout.document.getElementById("playerList").innerHTML = listTemp;
    });
}

function kickPlayer(player) {
    var keys = ["action", "player"];
    var values = ["kick", player];
    var body = buildParams(keys, values);

    sendQuery(body, undefined, undefined);
    refresh();
    refreshPlayerList();
}