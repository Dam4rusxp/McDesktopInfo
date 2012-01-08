var listTemp = "";

function initFlyout() {
    // The settings need to be loaded seperately for gadget and flyout
    settingsChanged();
    
    refreshPlayerList();
}

function refreshPlayerList() {
    var players = getInfo("playerList");
    players = players.split("+");
    
    players.foreach(displayPlayerList2);
    playerList.innerHTML = listTemp;
    listTemp = "";
}

function displayPlayerList2(player) {
    if(player != undefined && player != "") {
        listTemp += player + " (<a href=\"javascript:void(0);\" onclick=\" kickPlayer(\'" + player + "\');\">Kick</a></span>)" + "<br />";
    }
}

function kickPlayer(player) {
    getInfo("kick?player=" + player);
    loadInfo();
    refreshPlayerList();
}