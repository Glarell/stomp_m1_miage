"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Client = void 0;
class Client {
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