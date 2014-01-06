function getJSON() {
    var url = "/cron/parseuseragentServlet";


    var jsonT = false;
    var xhr = false;

    if (window.ActiveXObject) {
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else {
        xhr = new XMLHttpRequest();
    }
    if (!xhr) {
        alert('XMLHttp failed to instantiate');
        return false;
    }

    //    xhr.onreadystatechange = function() { jsonT = eval('(' + xhr.responseText + ')'); };
    xhr.open('GET', url, false);
    xhr.send(null);

    return xhr;
}

function fillbenchmark() {

    var score = javascriptPerformance();
    //var score2 = javascriptperformanceToStars(score);
    //var element = document.getElementById("webperformance");
    //element.innerHTML = score + "<div class='rating' id='rating_1'>" + score2 + "</div><div id='starRatingFeedback_1'></div>";
    document.formuserdata.score1.value = score;

    //var systemperf = "3000";
    //var element2 = document.getElementById("systemperformance");
    //element2.innerHTML = systemperf + "<div class='rating' id='rating_1'>2.5</div><div id='starRatingFeedback_1'></div>";

    var speed = downloadSpeed();
    document.formuserdata.score2.value = speed;
    //var speed2 = downloadSpeedToStars(speed);
    //var element3 = document.getElementById("downloadspeed");
    //element3.innerHTML = speed + " Kb/s <div class='rating' id='rating_1'>" + speed2 + "</div><div id='starRatingFeedback_1'></div>";

    //init_rating();
    
    var param1 = gup('param1');
    var param2 = gup('param2');
    var param3 = gup('param3');
    var lang = gup('lang');

    document.formuserdata.param1.value = param1;
    document.formuserdata.param2.value = param2;
    document.formuserdata.param3.value = param3;
    document.formuserdata.lang.value = lang;

    var params = $('formuserdata').serialize();
    var url = "/cron/benchmarkServlet?" + params;

    http_request = false;
    if (window.XMLHttpRequest) { // Mozilla, Safari,...
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            // set type accordingly to anticipated content type
            //http_request.overrideMimeType('text/xml');
            http_request.overrideMimeType('text/html');
        }
    } else if (window.ActiveXObject) { // IE
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) { }
        }
    }
    if (!http_request) {
        alert('Cannot create XMLHTTP instance');
        return false;
    }

    http_request.open('get', url, true);
    http_request.send(null);
    if (lang == "nl"){
      document.getElementById("loader").innerHTML = "Test is uitgevoerd, u kan dit venster sluiten."
      document.getElementById("text").innerHTML = ""
      }
    else if (lang == "fr"){
 		document.getElementById("loader").innerHTML = "Le test a &eacute;t&eacute; effectu&eacute;, vous pouvez fermer cette fen&ecirc;tre."
 		document.getElementById("text").innerHTML = ""
    }
   
}

function gup(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.href);
    if (results == null)
        return "";
    else
        return results[1];
}

function fillResolution(json, divname) {
    var resolution = screen.width + " X " + screen.height;
    // var element = document.getElementById(divname);
    // element.innerHTML = '<div class="resolution">&nbsp;</div><div class="itempaddingleft">Screen Resolution</div><div class="datapaddingleft">' + resolution + '</div>';
    document.formuserdata.screenResolution.value = resolution;
    // Effect.toggle(divname, 'appear');
    setTimeout(function() { fillColor(json, "colorDepth") }, 200);
}

function fillJavascript(json, divname) {
    var enabled = "Yes";
    //  var element = document.getElementById(divname);
    //  element.innerHTML = '<div class="javascript">&nbsp;</div><div class="itempaddingleft">Javascript Enabled?</div><div class="datapaddingleft">' + enabled + '</div>';
    document.formuserdata.javascriptEnabled.value = "Yes";
    //  Effect.toggle(divname, 'appear');
    setTimeout(function() { fillResolution(json, "screenResolution") }, 200);
}

function fillFlash(json, divname) {
    var version = getFlashVersion().split(',').shift();
    //  var element = document.getElementById(divname);
    //  element.innerHTML = '<div class="flash">&nbsp;</div><div class="itempaddingleft">Flash Version</div><div class="datapaddingleft">' + version + '</div>';
    document.formuserdata.flashVersion.value = version;
    //  Effect.toggle(divname, 'appear');
    setTimeout(function() { fillbenchmark() }, 2000);
}

function fillColor(json, divname) {

    var colordepth = screen.colorDepth;
    //  var element = document.getElementById(divname);
    //  element.innerHTML = '<div class="colordepth">&nbsp;</div><div class="itempaddingleft">Color Depth</div><div class="datapaddingleft">' + colordepth + ' bit</div>';
    document.formuserdata.colorDepth.value = colordepth;
    //  Effect.toggle(divname, 'appear');
    setTimeout(function() { fillFlash(json, "flashVersion") }, 200);
}

function fillIP(json, divname) {
    //  var element = document.getElementById(divname);
    //  element.innerHTML = '<div class="ip">&nbsp;</div><div class="itempaddingleft">IP Address</div><div class="datapaddingleft">' + json.ip + '</div>';
    document.formuserdata.ipAddress.value = json.ip;
    //  Effect.toggle(divname, 'appear');
    setTimeout(function() { fillJavascript(json, "javascriptEnabled") }, 200);
}

function fillbrowserName(json, divname) {
    //  var element = document.getElementById(divname);
    //  switch (json.browserName) {
    //    case "MSIE":
    //        element.innerHTML = '<div class="explorer">&nbsp;</div><div class="itempaddingleft">Web Browser</div><div class="datapaddingleft">Internet Explorer ' + json.browserVersion + '</div>';
    //        break;
    //    case "Firefox":
    //        element.innerHTML = '<div class="firefox">&nbsp;</div><div class="itempaddingleft">Web Browser</div><div class="datapaddingleft">Firefox ' + json.browserVersion + '</div>';
    //        break;
    //    case "Chrome":
    //        element.innerHTML = '<div class="chrome">&nbsp;</div><div class="itempaddingleft">Web Browser</div><div class="datapaddingleft">Chrome ' + json.browserVersion + '</div>';
    //        break;
    //    default:
    //        element.innerHTML = '<div class="defaultbrowser">&nbsp;</div><div class="itempaddingleft">Web Browser</div><div class="datapaddingleft">' + json.browserName + ' ' + json.browserVersion + '</div>';
    //        break;
    //  }
    document.formuserdata.browserName.value = json.browserName;
    document.formuserdata.browserVersion.value = json.browserVersion;
    //  Effect.toggle(divname, 'appear');
    setTimeout(function() { fillIP(json, "ip") }, 200);
}

function fillOS(json, divname) {
    //  var element = document.getElementById(divname);

    //  switch (json.platform) {
    //    case "Windows":
    //        element.innerHTML = '<div class="windows">&nbsp;</div><div class="itempaddingleft">Operating System</div><div class="datapaddingleft">Microsoft Windows ' + json.platformVersion + '</div>';
    //        break;
    //    default:
    //        element.innerHTML = '<div class="defaultOS">&nbsp;</div><div class="itempaddingleft">Operating System</div><div class="datapaddingleft">' + json.platform + ' ' + json.platformVersion + '</div>';
    //        break;
    //  }

    document.formuserdata.platform.value = json.platform;
    document.formuserdata.platformVersion.value = json.platformVersion;
    //Effect.toggle(divname, 'appear');
    //    setTimeout(function() {fillbenchmark()}, 1000);
    setTimeout(function() { fillbrowserName(json, "browserName") }, 200);
}

function build() {

    var lang = gup('lang');
    if (lang == "nl"){
    document.getElementById("text").innerHTML = "Performance test wordt doorgevoerd. Gelieve enkele ogenblikken geduld te oefenen. Hartelijk dank voor uw medewerking!"
    }
    else if (lang == "fr"){
    document.getElementById("text").innerHTML = "Le test de performance est en cours d&#39;ex&eacute;cution. Veuillez patienter un instant. Merci de votre collaboration!"
    }
    
    
    
    var xhr = getJSON();
    var JSONdata = eval('(' + xhr.responseText + ')');
    fillOS(JSONdata, "platform");
}

function getFlashVersion() {
    // ie
    try {
        try {
            // avoid fp6 minor version lookup issues
            // see: http://blog.deconcept.com/2006/01/11/getvariable-setvariable-crash-internet-explorer-flash-6/
            var axo = new ActiveXObject('ShockwaveFlash.ShockwaveFlash.6');
            try { axo.AllowScriptAccess = 'always'; }
            catch (e) { return '6,0,0'; }
        } catch (e) { }
        return new ActiveXObject('ShockwaveFlash.ShockwaveFlash').GetVariable('$version').replace(/\D+/g, ',').match(/^,?(.+),?$/)[1];
        // other browsers
    } catch (e) {
        try {
            if (navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin) {
                return (navigator.plugins["Shockwave Flash 2.0"] || navigator.plugins["Shockwave Flash"]).description.replace(/\D+/g, ",").match(/^,?(.+),?$/)[1];
            }
        } catch (e) { }
    }
    return '0,0,0';
}
