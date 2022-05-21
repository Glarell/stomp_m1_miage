package fx;

import javafx.application.Platform;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fx.App.client_name;

/**
 * The type Controller.
 */
@ClientEndpoint
public class Controller {

    /**
     * The constant session.
     */
    public static Session session;
    /**
     * The New line.
     */
    public final String new_line = System.lineSeparator();
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
     * Initialize.
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
        this.label_client.setText("Client "+client_name);
        try {
            System.out.println(client_name);
            URI uri = new URI("ws://localhost:8080/stomp/main/" + client_name);
            session = client.connectToServer(this, uri);
            session.getBasicRemote().sendText(TrameConstructor.createTrame("SUBSCRIBE", new HashMap<>(Map.of("destination", "test", "content-type", "text/plain", "id", client_name)), "").toSend());
        } catch (DeploymentException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change grid.
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
     * On open.
     *
     * @param session the session
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            // TYPE
            String stringBuilder = "CONNECT" + new_line +
                    //HEADERS
                    "version:1.0" + new_line +
                    "host:localhost" + new_line +
                    "content-type:text/plain" + new_line +
                    // BLANK LINE
                    new_line +
                    // END
                    "^@";
            session.getBasicRemote().sendText(stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * On message.
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
                }else{
                    System.out.println(trame.toString());
                }
            }else{
                Pattern pattern = Pattern.compile("\\(x:(?<x>\\d+),y:(?<y>\\d+)\\) - \\((?<start>false|true)->(?<end>true|false)\\)");
                Matcher matcher = pattern.matcher(trame.getBody());
                if (matcher.find()) {
                    int x = Integer.parseInt(matcher.group("x"));
                    int y = Integer.parseInt(matcher.group("y"));
                    boolean value = Boolean.parseBoolean(matcher.group("end"));
                    Platform.runLater(() -> {
                        try {
                            System.out.println("ENVOOIIIEE: "+x+y);
                            changeGrid(x, y, new Button(value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    System.out.println("NO MATCH");
                }
            }
        }

    }
}
