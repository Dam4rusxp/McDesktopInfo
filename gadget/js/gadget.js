var untilFinish = 0;
var autoRefreshRunning = false;

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
    var fields = System.Gadget.document.querySelectorAll("#infoList li");
    untilFinish = fields.length;

    for(i = 0; i < fields.length; i++) {
        if(settings["useCustomName"] && fields[i].getAttribute("id") == "serverName") {
            untilFinish--;
            continue;
        }

        var keys = ["action"];
        var values = [fields[i].getAttribute("id")];
        var params = buildParams(keys, values);

        sendQuery(params, function(response, field) {
            System.Gadget.document.querySelector("#" + field + " .value").innerHTML = response;
            untilFinish--;
            if(untilFinish == 0) System.Gadget.document.getElementById("refreshBtn").disabled = false;
        }, fields[i].getAttribute("id"));
    }
}

function autoRefresh() {
    if(settings["useAutoRefresh"]) {
        refresh();
        setTimeout(autoRefresh, settings["refreshInterval"] * 1000);
    } else {
        autoRefreshRunning = false;
    }
}

function sendQuery(params, callback, callbackParam) {
    // If no host is specified, do not try to update
    if(settings["host"] != undefined && settings["host"] != "") {
        var xhr = new XMLHttpRequest();

        xhr.open("GET", encodeURI("http://" + settings["host"] + "/") + params + "&rnd=" + Math.random(), true);

        // Set the function that is executes when we get an answer
        xhr.onreadystatechange = function() {
            if(xhr.readyState == 4) {
                var response = xhr.responseText;
                if(response == undefined) response = "";

                typeof callback == "function" && callback(response, callbackParam);
            }
        }

        xhr.send();
    } else {
        typeof callback == "function" && callback("", callbackParam);
    }
}

function buildParams(keys, values) {
    var params = "?";

    if(keys.length != values.length) return;

    // Add adminPw if one is set
    if(settings["adminPw"] != undefined && settings["adminPw"] != "") {
        keys.push("adminPw");
        values.push(settings["adminPw"]);
    }

    // Build the param String and escape it properly
    for( var i = 0; i < keys.length; i++) {
        if(i > 0) params += "&";
        params += escape(keys[i]) + "=" + escape(values[i]);
    }

    return params;
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
        System.Gadget.document.querySelector("#serverName .value").innerHTML = settings["serverName"];
    }

    // Set Background
    System.Gadget.background = "img/" + settings["bg"];

    // Hide all values with labels
    var liElements = System.Gadget.document.querySelectorAll("#infoList li");
    for(i = 0; i < liElements.length; i++) liElements[i].style.display = "";

    // Display values for enabled queries
    var enabledQueries = settings["enabledQueries"].split(";");

    System.Gadget.document.getElementById("serverName").style.display = "block";
    for(i = 0; i < enabledQueries.length; i++) {
        System.Gadget.document.getElementById(enabledQueries[i]).style.display = "block";
        System.Gadget.document.getElementById("infoList").appendChild(System.Gadget.document.getElementById(enabledQueries[i]));
    }

    // Set text color
    var spans = System.Gadget.document.getElementsByTagName("span");
    var as = System.Gadget.document.getElementsByTagName("a");

    for(i = 0; i < spans.length; i++) spans[i].style.color = settings["textColor"];
    for(i = 0; i < as.length; i++) as[i].style.color = settings["textColor"];

    refresh();

    // Start auto refreshing
    if(settings["useAutoRefresh"] && !autoRefreshRunning) {
        setTimeout(autoRefresh, settings["refreshInterval"] * 1000);
        autoRefreshRunning = true;
    }
}
