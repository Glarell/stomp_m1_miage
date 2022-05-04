var socket;

/* LISTENERS */
$( document ).ready(function() {
    $('#btn_client').click(function (e) {

        socket = new WebSocket('wss://localhost:8081');

        socket.onopen = function(e) {
            console.log("[open] Connection established");
        };

        socket.onmessage = function(event) {
            console.log(`[message] Data received from server: ${event.data}`);
        };

        socket.onclose = function(event) {
            if (event.wasClean) {
                console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
            } else {
                console.log('[close] Connection died');
            }
        };

        socket.onerror = function(error) {
            console.log(`[error] ${error.message}`);
        };

    });

    $('#btn_message').click(function (){
        const message = $('#text_message').val();
        socket.send(message);
    });
});