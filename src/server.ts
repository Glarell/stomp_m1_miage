import * as express from 'express';
import * as http from 'http';
import * as WebSocket from 'ws';
import * as path from "path";

class Server {

    MESSAGE(subscription:string,id:number,queue:string) {
        return `MESSAGE
subscription:${subscription}
message-id:${id}
destination:/queue/${queue}
content-type:text/plain

hello queue a^@`
    }

    RECEIPT(id:number) {
        return `RECEIPT
receipt-id:message-${id}

^@`
    }

    ERROR(id:number,queue:string,content_length:number) {
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
const app = express();

//initialize a simple http server
const server = http.createServer(app);

let s:Server = new Server();

app.get("/", (req: any, res: any) => {
    res.sendFile(path.resolve("./src/client/index.html"));
});


//initialize the WebSocket server instance
const wss = new WebSocket.Server({ server });

wss.on('connection', (ws: WebSocket) => {

    //connection is up, let's add a simple simple event
    ws.on('message', (message: string) => {

        //log the received message and send it back to the client
        console.log('received: %s', message);
        ws.send(`Hello, you sent -> ${message}`);
    });

    //send immediatly a feedback to the incoming connection
    ws.send('Hi there, I am a WebSocket server');
});

//start our server
server.listen(8080, () => {
    console.log(`Server started on port ${8080} :)`);
});