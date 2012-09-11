function initFlyout() {
    // The settings need to be loaded seperately for gadget and flyout
    settingsChanged();
    
    refreshPlayerList();
}

function refreshPlayerList() {
    var players = sendQuery("playerList", undefined);
    players = players.split("+");

    var listTemp = "";
    for(var i = 0; i < players.length; i++) {
        if(player != undefined && player != "") {
            listTemp += player + " (<a href=\"javascript:void(0);\" onclick=\"kickPlayer(\'" + player + "\');\">Kick</a></span>)" + "<br />";
        }
    }

    System.Gadget.Flyout.document.getElementById("playerList").innerHTML = listTemp;
}

function kickPlayer(player) {
    sendQuery("kick?player=" + player, undefined);
    refresh();
    refreshPlayerList();
}