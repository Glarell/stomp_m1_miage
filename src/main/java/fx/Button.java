package fx;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import static network.MainClientEndpoint.session;

public class Button extends javafx.scene.control.Button {
    public static final String new_line = System.lineSeparator();

    private boolean isWhite;

    public Button(boolean isWhite) {
        this.isWhite=isWhite;
        this.setMinWidth(50);
        this.setMinHeight(50);
        if (isWhite) {
            this.setStyle("-fx-background-color: white");
        } else {
            this.setStyle("-fx-background-color: black");
        }
        this.setOnMouseClicked(e -> {
            Button source = (Button) e.getSource();
            int x = GridPane.getRowIndex(source);
            int y = GridPane.getColumnIndex(source);
            System.out.printf("Case - (x:%s,y:%s) - Couleur (%s->%s)\n", x, y,isWhite(),!isWhite());
//            Controller.changeGrid(x,y,new Button(!source.isWhite()));
            String stringBuilder = "SEND" + new_line +
                    //HEADERS
                    "destination:test" + new_line +
                    "content-type:text/plain" + new_line +
                    // BLANK LINE
                    new_line +
                    //CONTENT
                    String.format("(x:%s,y:%s) - (%s->%s)",
                            x,
                            y,
                            isWhite(),
                            !isWhite()) + new_line +
                    // END
                    "^@";
            try {
                session.getBasicRemote().sendText(stringBuilder);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }
}
