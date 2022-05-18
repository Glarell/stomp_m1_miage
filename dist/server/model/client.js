"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Client = void 0;
class Client {
    constructor(url) {
        //ws://localhost:8080
        this.socket = new WebSocket(url);
        this.socket.onopen = this.connecting;
        this.socket.onmessage = function (event) {
            console.log(`[message] Data received from server: ${event.data}`);
        };
        this.socket.onclose = function (event) {
            if (event.wasClean) {
                console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
            }
            else {
                console.log('[close] Connection died');
            }
        };
        this.socket.onerror = function (error) {
            console.log(`[error] ${error}`);
        };
    }
    connecting() {
        this.socket.send(this.CONNECT("1.0", "localhost"));
    }
    CONNECT(version, host) {
        return `CONNECT
accept-version:${version}
host:${host}

^@`;
    }
    SEND(id, queue) {
        return `SEND
destination:/queue/${queue}
receipt:message-${id}

hello queue a^@`;
    }
    SUBSCRIBE(id, queue) {
        return `SUBSCRIBE
id:${id}
destination:/queue/${queue}
ack:client

^@`;
    }
    UNSUBSCRIBE(id) {
        return `UNSUBSCRIBE
id:${id}

^@`;
    }
    BEGIN(transation) {
        return `BEGIN
transaction:${transation}

^@`;
    }
    COMMIT(transation) {
        return `COMMIT
transaction:${transation}

^@`;
    }
    DISCONNECT_START(id) {
        return `DISCONNECT
receipt:${id}
^@`;
    }
    DISCONNECT_END(id) {
        return `RECEIPT
receipt-id:${id}
^@`;
    }
}
exports.Client = Client;
//# sourceMappingURL=client.js.map