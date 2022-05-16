const http = require('http');
const ws = require('ws');

class Server {

    MESSAGE(subscription,id,queue) {
        return `MESSAGE
subscription:${subscription}
message-id:${id}
destination:/queue/${queue}
content-type:text/plain

hello queue a^@`
    }

    RECEIPT(id) {
        return `RECEIPT
receipt-id:message-${id}

^@`
    }

    ERROR(id,queue,content_length) {
        return `ERROR
receipt-id:message-${id}
content-type:text/plain
content-length:${content_length}
message:malformed frame received

The message:
-----
MESSAGE
destined:/queue/${queue}
receipt:message-${id}

Hello queue ${queue}!
-----
Did not contain a destination header, which is REQUIRED
for message propagation.
^@`
    }
}
$( document ).ready(function() {
    server = new Server();
    $('#btn_server').click(function (e) {
        console.log("youpii");
        const wss = new ws.Server({noServer: true});

        function accept(req, res) {
            // all incoming requests must be websockets
            if (!req.headers.upgrade || req.headers.upgrade.toLowerCase() != 'websocket') {
                res.end();
                return;
            }

            // can be Connection: keep-alive, Upgrade
            if (!req.headers.connection.match(/\bupgrade\b/i)) {
                res.end();
                return;
            }

            wss.handleUpgrade(req, req.socket, Buffer.alloc(0), onConnect);
        }

        function onConnect(ws) {
            ws.on('message', function (message) {
                const id = message.toString();
                ws.send(server.RECEIPT(id))
                //ws.send(`Message recu : ${message}!`);
                setTimeout(() => ws.close(1000, "Bye!"), 5000);
            });
        }

        if (!module.parent) {
            http.createServer(accept).listen(8080);
        } else {
            exports.accept = accept;
        }
    });
});