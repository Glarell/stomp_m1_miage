var socket;

class client {

    SEND(id,queue) {
        return `SEND
destination:/queue/${queue}
receipt:message-${id}

hello queue a^@`
    }

    SUBSCRIBE(id,queue) {
        return `SUBSCRIBE
id:${id}
destination:/queue/${queue}
ack:client

^@`
    }

    UNSUBSCRIBE(id) {
        return `UNSUBSCRIBE
id:${id}

^@`
    }

    BEGIN(transation) {
        return `BEGIN
transaction:${transation}

^@`
    }

    COMMIT(transation) {
        return `COMMIT
transaction:${transation}

^@`
    }

    DISCONNECT_START(id) {
        return `DISCONNECT
receipt:${id}
^@`
    }

    DISCONNECT_END(id) {
        return `RECEIPT
receipt-id:${id}
^@`
    }
}

/* LISTENERS */
$( document ).ready(function() {
    $('#btn_client').click(function (e) {
        client = new Client();
        socket = new WebSocket('wss://localhost:8081');

        socket.onopen = function(e) {
            console.log("[open] Connection established");
            socket
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
        const id = $('#text_message').val();
        const message = client.SEND(id,"test");
        socket.send(message);
        console.log(message)
    });
});