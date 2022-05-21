package network;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;


/**
 * The type Main server endpoint.
 */
@ServerEndpoint(value = "/main/{username}")
public class MainServerEndpoint {

    private static final Logger logger = Logger.getLogger(MainServerEndpoint.class.getName());
    private static final Set<MainServerEndpoint> serverEndpoints = new CopyOnWriteArraySet<>();
    private static final HashMap<String, String> users = new HashMap<>();
    private static final HashMap<String, Session> users_sessions = new HashMap<>();
    private static final HashMap<String, Integer> users_connected = new HashMap<>();
    private static final HashMap<String, ArrayList<Subscription>> users_subscribes = new HashMap<>();
    private static final HashMap<String, ArrayList<Message>> queues = new HashMap<>();

    /**
     * On open.
     *
     * @param session the session
     * @param id      the id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String id) {
        serverEndpoints.add(this);
        // Liste des websockets connectés
        users.put(session.getId(), id);
        // Liste des utilisateurs et si ils sont connectés en STOMP
        users_connected.put(id, 0);
        users_sessions.put(id, session);
    }

    private static void manageUNSUBSCRIBE(Session session, String message, @PathParam("username") String id) {
        Trame trame = TrameConstructor.parseTrameClient(message);
        String id_sub = trame.getHeaders().get("id");
        users_subscribes.forEach((x, y) -> {
            y.removeIf(subscription -> subscription.getId().equals(id_sub));
        });
    }

    /**
     * On close.
     *
     * @param session the session
     * @param id      the id
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String id) {
        serverEndpoints.remove(this);
        users_connected.remove(id);
        users_connected.replace(id, 0);
        users_subscribes.remove(id);
        users_sessions.remove(id);
    }

    /**
     * On error.
     *
     * @param session   the session
     * @param id        the id
     * @param throwable the throwable
     */
    @OnError
    public void onError(Session session, @PathParam("username") String id, Throwable throwable) {
    }


    private static void firstConnexion(Session session, String message, String id) throws IOException {
        if (users_connected.containsKey(id)) {
            if (users_connected.get(id) == 0) {
                Trame trame = TrameConstructor.parseTrameClient(message);
                if (trame.isValidCONNECT()) {
                    logger.info(String.format("Une trame [CONNECTED] a été reçu du client [%s], son contenu : \n%s", id, trame));
                    Trame res = TrameConstructor.createTrame("CONNECTED", new HashMap<>(Map.of("version", "1.0", "content-type", "text/plain")), "");
                    users_connected.replace(id, 1);
                    session.getBasicRemote().sendText(res.toSend());
                } else {
                    sendError(session, trame, "First Frame is not a correct CONNECT frame");
                }
            }
        } else {
            logger.info("Cet utilisateur n'est pas connecté au serveur !");
        }
    }

    private static void manageSEND(Session session, String message, @PathParam("username") String id) {
        Trame trame = TrameConstructor.parseTrameClient(message);
        String destination = trame.getHeaders().get("destination");
        String body = trame.getBody();
        if (queues.containsKey(destination)) {
            ArrayList<Message> queue = queues.get(destination);
            queue.add(new Message(body, queue.size() + 1));
            queues.replace(destination, queue);
        } else {
            ArrayList<Message> queue = new ArrayList<>(List.of(new Message(body, 1)));
            queues.put(destination, queue);
        }
        updateQueuesSEND(destination, id);
    }

    private static void updateQueuesSEND(String destination, @PathParam("username") String id) {
        int size_queue = queues.get(destination).size();
        users_subscribes.forEach((x, y) -> {
            synchronized (y) {
                for (Subscription subscription : y) {
                    if (subscription.getDestination().equals(destination)) {
                        while (subscription.getCursor() < size_queue) {
                            Trame trame = TrameConstructor.createTrame("MESSAGE", new HashMap<>(Map.of(
                                    "subscription", String.valueOf(subscription.getId()), "message-id",
                                    String.valueOf(subscription.getCursor()), "destination", destination,
                                    "content-type", "text/plain")), queues.get(destination).get(subscription.getCursor()).getContent());
                            subscription.setCursor(subscription.getCursor() + 1);
                            try {
                                users_sessions.get(x).getBasicRemote().sendText(trame.toSend());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    private static void manageSUBSCRIBE(Session session, String message, @PathParam("username") String id) {
        Trame trame = TrameConstructor.parseTrameClient(message);
        String destination = trame.getHeaders().get("destination");
        String id_sub = trame.getHeaders().get("id");
        Subscription sub = new Subscription(id_sub, destination);
        AtomicBoolean condition = new AtomicBoolean(false);
        users_subscribes.forEach((x, y) -> {
            for (Subscription subscription : y) {
                if (subscription.getId().equals(id_sub)) {
                    try {
                        sendError(session, trame, "Already a subscription with this id");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    condition.set(true);
                }
            }
        });
        if (!condition.get()) {
            if (!queues.containsKey(destination)) {
                ArrayList<Message> liste = new ArrayList<>();
                queues.put(destination, liste);
            }
            if (!users_subscribes.containsKey(id)) {
                ArrayList<Subscription> subs = new ArrayList<>(List.of(sub));
                users_subscribes.put(id, subs);
            } else {
                ArrayList<Subscription> subs = users_subscribes.get(id);
                subs.add(sub);
                users_subscribes.replace(id, subs);
            }
            updateQueuesSEND(destination, id);
        }
    }

    private static void sendError(Session session, Trame message, String reason) throws IOException {
        Trame trame = TrameConstructor.createTrame("ERROR", new HashMap<>(Map.of("content-type", "text/plain", "message", reason)), message.toString());
        session.getBasicRemote().sendText(trame.toSend());
    }

    /**
     * On message.
     *
     * @param session the session
     * @param message the message
     * @param id      the id
     * @throws IOException the io exception
     */
    @OnMessage
    public void onMessage(Session session, String message, @PathParam("username") String id) throws IOException {
        if (users_connected.get(id) == 1) {
            Trame trame = TrameConstructor.parseTrameClient(message);
            switch (trame.getType()) {
                case "SEND":
                    if (trame.isValidSEND()) {
                        logger.info(String.format("Une trame [SEND] a été reçu du client [%s], son contenu : \n%s", id, trame));
                        manageSEND(session, message, id);
                    } else {
                        sendError(session, trame, "SEND malformed");
                    }
                    break;
                case "SUBSCRIBE":
                    if (trame.isValidSUBSCRIBE()) {
                        logger.info(String.format("Une trame [SUBSCRIBE] a été reçu du client [%s], son contenu : \n%s", id, trame));
                        manageSUBSCRIBE(session, message, id);
                    } else {
                        sendError(session, trame, "SUBSCRIBE malformed");
                    }
                    break;
                case "UNSUBSCRIBE":
                    if (trame.isValidUNSUBSCRIBE()) {
                        logger.info(String.format("Une trame [UNSUBSCRIBE] a été reçu du client [%s], son contenu : \n%s", id, trame));
                        manageUNSUBSCRIBE(session, message, id);
                    } else {
                        sendError(session, trame, "UNSUBSCRIBE malformed");
                    }
                    break;
                case "DISCONNECT":
                    if (trame.isValidDISCONNECT()) {
                        logger.info(String.format("Une trame [DISCONNECT] a été reçu du client [%s], son contenu : \n%s", id, trame));
                        users.remove(session.getId());
                        users_connected.remove(id);
                        users_subscribes.remove(id);
                        users_sessions.remove(id);
                    } else {
                        sendError(session, trame, "DISCONNECT malformed");
                    }
                    break;
                default:
                    logger.info(String.format("Une trame [UNRECOGNIZED] a été reçu du client [%s], son contenu : \n%s", id, trame));
                    sendError(session, trame, "No Default FRAMES recognized");
                    break;
            }
        } else {
            firstConnexion(session, message, id);
        }
    }

}
