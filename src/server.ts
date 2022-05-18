import * as express from 'express';
import * as http from 'http';
import * as WebSocket from 'ws';
import {Client} from './model/client';
import {Server} from "./model/server";
import * as path from "path";
const app = express();

const server = http.createServer(app);

app.get("/", (req: any, res: any) => {
    res.sendFile(path.resolve("./src/view/index.html"));
});

app.get("/dist/server/index.js", (req:any,res:any) => {
    res.sendFile(path.resolve("../../dist/server/index.js"));
});

const wss = new Server(new WebSocket.Server({ server }));

//start our server
server.listen(8080, () => {
    console.log(`Server started on port ${8080} :)`);
});