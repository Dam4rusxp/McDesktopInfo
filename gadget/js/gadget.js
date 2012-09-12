var fields = new Array("playerCount", "serverName", "pluginVersion", "serverVersion", "mem");

function init() {
    System.Gadget.settingsUI = "settings.html";
    System.Gadget.onSettingsClosed = settingsClosed;
    
    System.Gadget.Flyout.file = "flyout.html";
    System.Gadget.Flyout.onShow = showFlyout;
    System.Gadget.Flyout.onHide = hideFlyout;
    
    settingsChanged();
}

function changeBg(newBg) {
    System.Gadget.background = "img/" + newBg;
}

function refresh() {
    // Load info for each existing field
    for(i = 0; i < fields.length; i++) {
        sendQuery(fields[i], function(resp) {
            System.Gadget.document.getElementById(fields[i]).innerHTML = resp;
        });
    }
    
    if(mySettings.useCustomName) {
        System.Gadget.document.getElementById("serverName").innerHTML = mySettings.serverName;
    }
    
    if(mySettings.useAutoRefresh) {
        setTimeout("refresh()", mySettings.refreshInterval);
    }
}

function sendQuery(query, callback) {
    // If no host is specified, do not try to update
    if(mySettings.host != "") {
        var xhr = new XMLHttpRequest();
        var response;
        
        // If existing, add adminPw to query
        if(mySettings.adminPw != undefined && mySettings.adminPw != "") {
        	query += "?adminPw=" + mySettings.adminPw;
    	}
        xhr.open("GET", "http://" + mySettings.host + "/" + query + "?rnd=" + Math.random(), false);
        
        // Set the function that is executes when we get an answer
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                response = xhr.responseText;
                if(response == undefined) response = "";

                typeof callback == "function" && callback(response);
            }
        }
        
        xhr.send(null);
        return;
    }

    // Callback with an empty string if we get no answer
    typeof callback == "function" && callback("");
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

    changeBg(mySettings.bg);
    refresh();
}