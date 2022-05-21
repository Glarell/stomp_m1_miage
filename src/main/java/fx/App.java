package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.Trame;
import network.TrameConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type App.
 */
public class App extends Application {

	/**
	 * The constant client_name.
	 */
	public static String client_name;

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		UUID randomUUID = UUID.randomUUID();
		client_name = randomUUID.
				toString().
				replaceAll("[a-z]",
						String.valueOf((int) (Math.random() * 10))).substring(0, 8);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/main.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception {
		Trame trame = TrameConstructor.createTrame("DISCONNECT",
				new HashMap<>(Map.of("receipt", client_name)),
				"");
		Controller.session.getBasicRemote().sendText(trame.toSend());
		Controller.session.close();
	}
}
