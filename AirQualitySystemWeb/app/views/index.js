

var wsUri = "ws://" +window.location.hostname+ ":9000/client";
var output;

function init()
{
    var time = new Date();

    var data = [{
        x: [time],
        y: [0],
        mode: 'lines',
        line: {color: '#80CAF6'}
    }]

    Plotly.newPlot('graph', data);
    output = document.getElementById("output");
    testWebSocket();
}

function testWebSocket()
{
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) { onOpen(evt) };

    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onerror = function(evt) { onError(evt) };
}
function doSend(message)
{
    websocket.send(message);
}
function onOpen(evt)
{
    writeToScreen("CONNECTED");

    doSend("WebSocket rocks");
    onMessage();
}

function onClose(evt)
{
    writeToScreen("DISCONNECTED");
}

function onMessage(evt)
{
    //TODO: make function that updates the plot

    var time = new Date();
    var update = {
        x:  [[time]],
        y: [[evt.data]]
    }
    Plotly.extendTraces('graph', update, [0])

    writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data+'</span>');

}

function onError(evt)
{
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}



function writeToScreen(message)
{
    // var pre = document.update();

        document.getElementById("output").innerHTML = message;



    //output.appendChild(pre);
}

window.addEventListener("load", init, false);