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
                // Craft correct scope for callback
                (function(player) {
                    var pLi = document.createElement("li");
                    pLi.appendChild(document.createTextNode(answer["playerList"][i] + " ("));

                    var kickLink = document.createElement("a");
                    kickLink.appendChild(document.createTextNode("Kick"));
                    kickLink.href = "#";
                    kickLink.onclick = function() {
                        kickPlayer(player);
                    };

                    pLi.appendChild(kickLink);
                    pLi.appendChild(document.createTextNode(")"));

                    playerListElement.appendChild(pLi);
                })(answer["playerList"][i]);
            }
        }
    });
}

function refreshChatHistory() {
    sendQuery({"action": "chatHistory"}, function(answer) {
        if(typeof answer !== "undefined") {
            var chatElement = System.Gadget.Flyout.document.getElementById("chatHistory");

            for (var i = 0; i < answer["chatHistory"].length; i++) {
                var paragraph = document.createElement("p");
                paragraph.appendChild(document.createTextNode(answer["chatHistory"][i]));
                chatElement.appendChild(paragraph);
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
