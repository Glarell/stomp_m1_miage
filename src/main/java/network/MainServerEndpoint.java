package network;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;


@ServerEndpoint(value = "/main")
public class MainServerEndpoint {

    private Session session;
    private static final Logger logger = Logger.getLogger(MainServerEndpoint.class.getName());
    private static final Set<MainServerEndpoint> serverEndpoints = new CopyOnWriteArraySet<>();
    private static final HashMap<String, String> users = new HashMap<>();
    private static final HashMap<String, Boolean> users_connected = new HashMap<>();
    private static final HashMap<String, String> users_queues = new HashMap<>();

    private final String new_line = System.lineSeparator();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        serverEndpoints.add(this);
        users.put(session.getId(), id);
        users_connected.put(id, false);
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("id") String id) throws IOException {
        firstConnexion(session, message, id);
        if (users_connected.get(id)){
            Trame trame = TrameConstructor.parseTrameClient(message);
            switch (trame.getType()){
                case "SEND":
                    if (trame.isValidSEND()){
                        System.out.println(trame.toString());
                    }else{
                        sendError(session, trame, "SEND malformed");
                    }
                    break;
                case "SUBSCRIBE":
                    if (trame.isValidSUBSCRIBE()){
                        System.out.println(trame.toString());
                    }else{
                        sendError(session, trame, "SUBSCRIBE malformed");
                    }
                    users_queues.put(id, trame.getBody());
                    break;
                case "UNSUBSCRIBE":
                    if (trame.isValidUNSUBSCRIBE()){
                        System.out.println(trame.toString());
                    }else{
                        sendError(session, trame, "UNSUBSCRIBE malformed");
                    }
                    break;
                default:
                    sendError(session, trame, "No Default FRAMES recognized");
                    break;
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("id") String id) {
        serverEndpoints.remove(this);
        users_connected.remove(id);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }

    private static void broadcast(String message) {
        serverEndpoints.forEach(endpoint -> {
            synchronized (endpoint){
                try{
                    endpoint.session.getBasicRemote().sendText(message);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private static void firstConnexion(Session session, String message, @PathParam("id") String id) throws IOException {
        if(users_connected.containsKey(id)){
            if (users_connected.get(id) == false){
                Trame trame = TrameConstructor.parseTrameClient(message);
                if (trame.isCONNECT()){
                    Trame res = TrameConstructor.createTrame("CONNECTED", new HashMap<>(Map.of("version","1.0","content-type","text/plain")), "");
                    users_connected.replace(id, true);
                    session.getBasicRemote().sendText(res.toSend());
                }else{
                    sendError(session, trame, "First Frame is not a correct CONNECT frame");
                }
            }
        }else{
            logger.info("Cet utilisateur n'est pas connecté au serveur !");
        }
    }

    private static void manageSEND(Session session, String message, @PathParam("id") String id) {

    }

    private static void manageSUBSCRIBE(Session session, String message, @PathParam("id") String id){

    }

    private static void manageUNSUBSCRIBE(Session session, String message, @PathParam("id") String id){

    }

    private static void sendError(Session session, Trame message, String reason) throws IOException {
        Trame trame = TrameConstructor.createTrame("ERROR",new HashMap(Map.of("content-type","text/plain","message",reason)), message.toString());
        session.getBasicRemote().sendText(trame.toSend());
    }
}
