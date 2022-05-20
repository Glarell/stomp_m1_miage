package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.UUID;

import java.nio.charset.Charset;
import java.util.Random;

public class App extends Application {

	public static String client_name;
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/main.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UUID randomUUID = UUID.randomUUID();
		client_name = randomUUID.toString().replaceAll("[a-z]", "0").substring(0,8);
		System.out.println(client_name);
		launch(args);
	}
}
