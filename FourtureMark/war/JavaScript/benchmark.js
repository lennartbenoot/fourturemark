function pausecomp(millis) {
    var date = new Date();
    var curDate = null;

    do { curDate = new Date(); }
    while (curDate - date < millis);
}

function downloadSpeed() {

    if (window.XMLHttpRequest) {
        xhttp = new XMLHttpRequest();
    }
    else // Internet Explorer 5/6
    {
        xhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    var StartDate = new Date();
    var StartTime = StartDate.getTime();

    
    //Still cached result. THis can be avoided with:
    // xmlhttp.open("GET","demo_get.asp?t=" + Math.random(),true);
    xhttp.open("GET", "/data/downloadspeed.zip", false);
    xhttp.send("");
    xmlDoc = xhttp.responseXML;

    var EndDate = new Date();
    var EndTime = EndDate.getTime();

    var time = (EndTime - StartTime) / 1000;

    var speed = Math.round(1042 / time);

    return speed;
    //alert( speed  + ' KB/s');
}


function downloadSpeedToStars(speed) {
    if (speed < 0) return 0
    if (speed > 500) return 5;

    return Math.round(speed / 100);

}


function calculateSystemPerformance(webperformance, browser) {
    // Based on the following results
    // IE: 2458
    // Chrome: 167
    // FF:  818

    if (browser == 'IE7') {
        performance = webperformance / 101; // 
    }


}

function javascriptPerformance() {

    var maxRow = 50;
    var maxCol = 50;

    if (window.XMLHttpRequest) {
        xhttp = new XMLHttpRequest();
    }
    else // Internet Explorer 5/6
    {
        xhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xhttp.open("GET", "data/testData.xml", false);
    xhttp.send("");
    xmlDoc = xhttp.responseXML;

    //pausecomp( 2000);

    var StartDate = new Date();
    var StartTime = StartDate.getTime();

    for (var index = 0; index < 1; index++) {

        var table = document.createElement("TABLE");
        for (var iRow = 0; iRow < maxRow; iRow++) {
            var row = document.createElement('TR');
            for (var iCol = 0; iCol < maxCol; iCol++) {
                var cell1 = document.createElement('TD');
                var cell2 = document.createElement('TD');
                var checkbox = document.createElement('INPUT');
                checkbox.type = 'checkbox';
                cell1.appendChild(checkbox);
                row.appendChild(cell1);
                var childNodes = xmlDoc.getElementsByTagName("childLevel1");
                var iRandom = Math.floor(Math.random() * 100);
                cell2.innerHTML = childNodes[iRandom].getAttribute("id");
                row.appendChild(cell2)
                table.appendChild(row);
            }
        }
    }

    var EndDate = new Date();
    var EndTime = EndDate.getTime();

    var time = (EndTime - StartTime) / 1;

    var score = time;
    return score;
}

function javascriptperformanceToStars(javascriptPerformance) {
    var max = 200;
    var min = 2000;

    if (javascriptPerformance < max) return 5;
    if (javascriptPerformance > min) return 0;


    return 5 - Math.round((javascriptPerformance - max) / ((min - max) / 5));

}
