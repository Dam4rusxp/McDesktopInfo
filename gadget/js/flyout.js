function initFlyout() {
    // The settings need to be loaded seperately for gadget and flyout
    settings.loadSettings();
    refreshPlayerList();
    refreshChatHistory();
}

function refreshPlayerList() {
    sendQuery({"action": "playerList"}, function(answer) {
        if(typeof answer !== "undefined") {
            var playerListElement = System.Gadget.Flyout.document.getElementById("playerList");

            for (var i = 0; i < answer["playerList"].length; i++) {
                var pLi = document.createElement("li");
                var msgTxt = document.createTextNode(answer["playerList"][i]);
                pLi.appendChild(msgTxt);
                playerListElement.appendChild(pLi);
            }
        }
    });
}

function refreshChatHistory() {
    sendQuery({"action": "chatHistory"}, function(answer) {
        if(typeof answer !== "undefined") {
            var chatElement = System.Gadget.Flyout.document.getElementById("chatHistory");

            for (var i = 0; i < answer["chatHistory"].length; i++) {
                var msgDiv = document.createElement("div");
                var msgTxt = document.createTextNode(answer["chatHistory"][i]);
                msgDiv.appendChild(msgTxt);
                chatElement.appendChild(msgDiv);
            }
        }
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
