package fx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import network.Trame;
import network.TrameConstructor;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fx.App.client_name;

/**
 * The type Controller.
 */
@ClientEndpoint
public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    /**
     * The constant isSubscribe.
     */
    public static boolean isSubscribe;

    /**
     * The constant session.
     */
    public static Session session;
    /**
     * The First received.
     */
    public boolean first_received = false;

    private final int BOARD_SIZE = 16;

    /**
     * The Anchorpane.
     */
    @FXML
    public AnchorPane anchorpane;
    /**
     * The Borderpane.
     */
    @FXML
    public BorderPane borderpane;

    /**
     * The Gridpane.
     */
    @FXML
    public GridPane gridpane;
    /**
     * The Label client.
     */
    @FXML
    public Label label_client;
    /**
     * The Label sub.
     */
    @FXML
    public Label label_sub;
    /**
     * The Btn sub.
     */
    public javafx.scene.control.Button btn_sub;

    /**
     * Initialize.
     * Créé une instance client (websocket & javafx) et s'abonne à une queue (ici: /queue/r-place)
     */
    public void initialize() {
        if (gridpane == null) {
            gridpane = new GridPane();
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Button btn = new Button(true);
                gridpane.add(btn, i, j);
            }
        }
        gridpane.setGridLinesVisible(true);
        AnchorPane.setTopAnchor(gridpane, 10.0);
        AnchorPane.setLeftAnchor(gridpane, 10.0);
        AnchorPane.setRightAnchor(gridpane, 10.0);
        AnchorPane.setBottomAnchor(gridpane, 10.0);
        anchorpane.getChildren().add(gridpane);

        ClientManager client = ClientManager.createClient();
        try {
            URI uri = new URI("ws://localhost:8080/stomp/main/" + client_name);
            session = client.connectToServer(this, uri);
            session.getBasicRemote().sendText(TrameConstructor.createTrame("SUBSCRIBE", new HashMap<>(Map.of("destination", "/queue/r-place", "content-type", "text/plain", "id", client_name)), "").toSend());
            this.label_client.setText(String.format("Client %s", client_name));
            this.label_sub.setText("Subscribed");
            logger.info(String.format("Le client %s s'est [%s]", client_name, this.label_sub.getText()));
            isSubscribe = true;
        } catch (DeploymentException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change l'état (true=Blanc, false=Noir) d'un bouton dans la grille du client
     *
     * @param x      the x
     * @param y      the y
     * @param button the button
     */
    public void changeGrid(int x, int y, Button button) {
        try {
            this.gridpane.add(button, y, x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A l'ouverture d'un socket client, une trame connect est envoyée au serveur
     *
     * @param session the session
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            Trame trame = TrameConstructor.createTrame("CONNECT", new HashMap<>(Map.of("version", "1.0", "host", "localhost", "content-type", "text/plain")),
                    "");
            session.getBasicRemote().sendText(trame.toSend());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère les messages du serveur afin de modifier la grille
     *
     * @param message the message
     * @param session the session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        Trame trame = TrameConstructor.parseTrameServeur(message);
        if (trame != null) {
            if (!first_received) {
                first_received = true;
                if (trame.isERROR()) {
                    try {
                        session.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(trame.toString());
                }
            } else if (trame.isValidMESSAGE()) {
                Pattern pattern = Pattern.compile("\\(x:(?<x>\\d+),y:(?<y>\\d+)\\) - \\((?<start>false|true)->(?<end>true|false)\\)");
                Matcher matcher = pattern.matcher(trame.getBody());
                if (matcher.find()) {
                    int x = Integer.parseInt(matcher.group("x"));
                    int y = Integer.parseInt(matcher.group("y"));
                    boolean value = Boolean.parseBoolean(matcher.group("end"));
                    Platform.runLater(() -> {
                        try {
                            logger.info(String.format("Le client %s a reçu un message : \n\t(x:%s,y:%s) - (%s->%s)", client_name, x, y, !value, value));
                            changeGrid(x, y, new Button(value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }

    }

    /**
     * Permettre au client de s'abonner ou désabonner via un bouton
     *
     * @param e the e
     * @throws IOException the io exception
     */
    public void onActionSub(ActionEvent e) throws IOException {
        Trame trame;
        if (btn_sub.getText().equals("Unsubscribe")) {
            trame = TrameConstructor.createTrame("UNSUBSCRIBE",
                    new HashMap<>(Map.of("id", client_name)),
                    "");
            this.btn_sub.setText("Subscribe");
            this.label_sub.setText("Unsubscribed");
            isSubscribe = false;
        } else {
            trame = TrameConstructor.createTrame("SUBSCRIBE",
                    new HashMap<>(Map.of("destination", "/queue/r-place", "content-type", "text/plain", "id", client_name)),
                    "");
            this.btn_sub.setText("Unsubscribe");
            this.label_sub.setText("Subscribed");
            isSubscribe = true;
        }
        logger.info(String.format("Le client %s s'est [%s]", client_name, this.label_sub.getText()));
        session.getBasicRemote().sendText(trame.toSend());
    }
}
