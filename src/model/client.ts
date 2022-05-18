export class Client {

    socket: WebSocket;

    constructor(url:string) {
        //ws://localhost:8080
        this.socket=new WebSocket(url);

        this.socket.onopen = this.connecting;

        this.socket.onmessage = function(event) {
            console.log(`[message] Data received from server: ${event.data}`);
        };

        this.socket.onclose = function(event) {
            if (event.wasClean) {
                console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
            } else {
                console.log('[close] Connection died');
            }
        };

        this.socket.onerror = function(error) {
            console.log(`[error] ${error}`);
        };
    }

    connecting() {
        this.socket.send(this.CONNECT("1.0","localhost"))
    }

    CONNECT(version:string,host:string) {
        return `CONNECT
accept-version:${version}
host:${host}

^@`
    }

    SEND(id:number,queue:string) {
        return `SEND
destination:/queue/${queue}
receipt:message-${id}

hello queue a^@`;
    }

    SUBSCRIBE(id:number,queue:string) {
        return `SUBSCRIBE
id:${id}
destination:/queue/${queue}
ack:client

^@`
    }

    UNSUBSCRIBE(id:number) {
        return `UNSUBSCRIBE
id:${id}

^@`
    }

    BEGIN(transation:string) {
        return `BEGIN
transaction:${transation}

^@`
    }

    COMMIT(transation:string) {
        return `COMMIT
transaction:${transation}

^@`
    }

    DISCONNECT_START(id:number) {
        return `DISCONNECT
receipt:${id}
^@`
    }

    DISCONNECT_END(id:number) {
        return `RECEIPT
receipt-id:${id}
^@`
    }
}
