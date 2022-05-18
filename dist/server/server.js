"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const http = require("http");
const WebSocket = require("ws");
const server_1 = require("./model/server");
const path = require("path");
const app = express();
const server = http.createServer(app);
app.get("/", (req, res) => {
    res.sendFile(path.resolve("./src/view/index.html"));
});
app.get("/dist/server/index.js", (req, res) => {
    res.sendFile(path.resolve("../../dist/server/index.js"));
});
const wss = new server_1.Server(new WebSocket.Server({ server }));
//start our server
server.listen(8080, () => {
    console.log(`Server started on port ${8080} :)`);
});
//# sourceMappingURL=server.js.map