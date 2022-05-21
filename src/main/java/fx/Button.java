package fx;

import javafx.scene.layout.GridPane;
import network.Trame;
import network.TrameConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static fx.App.client_name;
import static fx.Controller.session;


/**
 * The type Button.
 */
public class Button extends javafx.scene.control.Button {
    private static final Logger logger = Logger.getLogger(Button.class.getName());

    /**
     * The constant new_line.
     */
    private final int CASE_SIZE = 25;

    private boolean isWhite;

    /**
     * Instantiates a new Button.
     *
     * @param isWhite the is white
     */
    public Button(boolean isWhite) {
        this.isWhite = isWhite;
        this.setMinWidth(CASE_SIZE);
        this.setMinHeight(CASE_SIZE);
        if (isWhite) {
            this.setStyle("-fx-background-color: white");
        } else {
            this.setStyle("-fx-background-color: black");
        }
        this.setOnMouseClicked(e -> {
            Button source = (Button) e.getSource();
            int x = GridPane.getRowIndex(source);
            int y = GridPane.getColumnIndex(source);
            Trame trame = TrameConstructor.createTrame("SEND", new HashMap<>(Map.of("destination", "test", "content-type", "text/plain")),
                    String.format("(x:%s,y:%s) - (%s->%s)",
                            x,
                            y,
                            isWhite(),
                            !isWhite()));
            try {
                if (Controller.isSubscribe) {
                    logger.info(String.format("Le client %s envoi un message : \n\t(x:%s,y:%s) - (%s->%s)", client_name, x, y, isWhite(), !isWhite()));
                    session.getBasicRemote().sendText(trame.toSend());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Is white boolean.
     *
     * @return the boolean
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Sets white.
     *
     * @param white the white
     */
    public void setWhite(boolean white) {
        isWhite = white;
    }
}
