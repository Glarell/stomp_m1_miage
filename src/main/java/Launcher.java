import fx.App;
import network.MainClientEndpoint;
import network.TyrusServer;

public class Launcher {

    public static void main(String[] args) {
        if ("server".equals(args[0])) {
            TyrusServer.main(args);
        }
        if ("client".equals(args[0])) {
            App.main(args);
        }
    }
}
