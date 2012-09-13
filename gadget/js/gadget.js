var fields = new Array("playerCount", "serverName", "pluginVersion", "serverVersion", "mem");

var untilFinish = 0;

function init() {
    System.Gadget.settingsUI = "settings.html";
    System.Gadget.onSettingsClosed = settingsClosed;

    System.Gadget.Flyout.file = "flyout.html";
    System.Gadget.Flyout.onShow = showFlyout;
    System.Gadget.Flyout.onHide = hideFlyout;

    settingsChanged();
}

function refresh() {
    // Disable refresh button until we finish, refresh timeout is 5s
    System.Gadget.document.getElementById("refreshBtn").disabled = true;
    setTimeout(function() {
        System.Gadget.document.getElementById("refreshBtn").disabled = false;
    }, mySettings.connTimeout);

    // Load info for each existing field
    untilFinish = fields.length;
    for(i = 0; i < fields.length; i++) {
        if(mySettings.useCustomName && fields[i] == "serverName") continue;

        sendQuery(fields[i], function(response, query) {
            // Note: Do not access variables of the refresh function here, because it will finish before this is called
            System.Gadget.document.getElementById(query).innerHTML = response;

            untilFinish--;
            if(untilFinish == 0) {
                System.Gadget.document.getElementById("refreshBtn").disabled = false;
            }
        });
    }

    if(mySettings.useAutoRefresh) {
        setTimeout("refresh()", mySettings.refreshInterval);
    }
}

function sendQuery(query, callback) {
    // If no host is specified, do not try to update
    if(mySettings.host != undefined && mySettings.host != "") {
        var xhr = new XMLHttpRequest();
        var response;
        var field = query;

        // If existing, add adminPw to query
        if(mySettings.adminPw != undefined && mySettings.adminPw != "") {
            query += "?adminPw=" + mySettings.adminPw;
        }
        xhr.open("GET", "http://" + mySettings.host + "/" + query + "?rnd=" + Math.random(), true);

        // Set the function that is executes when we get an answer
        xhr.onreadystatechange = function() {
            if(xhr.readyState == 4) {
                response = xhr.responseText;
                if(response == undefined) response = "";

                typeof callback == "function" && callback(response, field);
            }
        }

        xhr.send(null);
    } else {
        typeof callback == "function" && callback("");
    }
}

function showFlyout() {
    System.Gadget.Flyout.show = true;
}

function hideFlyout() {
    System.Gadget.Flyout.show = false;
}

function settingsClosed(event) {
    if(event.closeAction == event.Action.commit) {
        settingsChanged();
    }
}

function settingsChanged() {
    mySettings.load();

    if(mySettings.useCustomName) {
        System.Gadget.document.getElementById("serverName").innerHTML = mySettings.serverName;
    }

    System.Gadget.background = "img/" + mySettings.bg;
    refresh();
}