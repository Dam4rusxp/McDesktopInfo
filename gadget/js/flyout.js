﻿function initFlyout() {
    // The settings need to be loaded seperately for gadget and flyout
    settingsChanged();

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
    sendQuery("kick?player=" + player, undefined);
    refresh();
    refreshPlayerList();
}