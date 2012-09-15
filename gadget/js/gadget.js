var fields = new Array("playerCount", "serverName", "pluginVersion", "serverVersion", "mem");

var untilFinish = 0;
var startedAutoRefresh = false;

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
    }, settings["connTimeout"] * 1000);

    // Load info for each existing field
    untilFinish = fields.length;
    for(i = 0; i < fields.length; i++) {
        if(settings["useCustomName"] && fields[i] == "serverName") continue;

        sendQuery(fields[i], function(response, query) {
            // Note: Do not access variables of the refresh function here, because it will finish before this is called
            System.Gadget.document.getElementById(query).innerHTML = response;

            untilFinish--;
            if(untilFinish == 0) {
                System.Gadget.document.getElementById("refreshBtn").disabled = false;
            }
        });
    }
}

function autoRefresh() {
    if(settings["useAutoRefresh"]) {
        refresh();
        setTimeout(autoRefresh, settings["refreshInterval"] * 1000);
    } else {
        startedAutoRefresh = false;
    }
}

function sendQuery(query, callback) {
    // If no host is specified, do not try to update
    if(settings["host"] != undefined && settings["host"] != "") {
        var xhr = new XMLHttpRequest();
        var response;
        var field = query;

        // If existing, add adminPw to query
        if(settings["adminPw"] != undefined && settings["adminPw"] != "") {
            query += "?adminPw=" + settings["adminPw"];
        }
        xhr.open("GET", "http://" + settings["host"] + "/" + query + "?rnd=" + Math.random(), true);

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
        typeof callback == "function" && callback("", query);
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
    settings.load();

    // Set custom name
    if(settings["useCustomName"]) {
        System.Gadget.document.getElementById("serverName").innerHTML = settings["serverName"];
    }

    // Set Background
    System.Gadget.background = "img/" + settings["bg"];

    // Set text color
    var spans = System.Gadget.document.getElementsByTagName("span");
    var as = System.Gadget.document.getElementsByTagName("a");

    for(i = 0; i < spans.length; i++) spans[i].style.color = settings["textColor"];
    for(i = 0; i < as.length; i++) as[i].style.color = settings["textColor"];

    refresh();

    // Start auto refreshing
    if(settings["useAutoRefresh"] && !startedAutoRefresh) {
        setTimeout(autoRefresh, settings["refreshInterval"] * 1000);
        startedAutoRefresh = true;
    }
}