package fx;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Button extends javafx.scene.control.Button {

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
            System.out.printf("Case - (x:%s;y:%s) - Couleur (%s->%s)\n", x, y,isWhite(),!isWhite());
            Controller.changeGrid(x,y,new Button(!source.isWhite()));
        });
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }
}
