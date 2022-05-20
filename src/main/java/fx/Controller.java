package fx;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import static network.MainClientEndpoint.session;

public class Controller {


    private final int BOARD_SIZE = 16;
    public AnchorPane anchorpane;
    public BorderPane borderpane;

    public static GridPane gridpane;

    public void initialize() {
        gridpane = new GridPane();
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
    }

    public static void changeGrid(int x, int y, Button button) {
        gridpane.add(button, y, x);
    }
}
