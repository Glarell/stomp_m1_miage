import fx.App;
import network.TyrusServer;

/**
 * The type Launcher.
 */
public class Launcher {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if ("server".equals(args[0])) {
            TyrusServer.main(args);
        }
        if ("client".equals(args[0])) {
            App.main(args);
        }
    }
}
