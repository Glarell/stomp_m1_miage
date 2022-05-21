import fx.App;
import network.TyrusServer;

import java.util.logging.Logger;

/**
 * The type Launcher.
 */
public class Launcher {

    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "server":
                    TyrusServer.main(args);
                    break;
                case "client":
                    App.main(args);
                    break;
                default:
                    logger.info("Mauvaise entrée pour lancement de l'application\n" +
                            "Veuilliez respecter un de ces formats : \n" +
                            "\t * %JAVA-PATH%\\java -jar stomp-1.0.0.jar server\n" +
                            "\t * %JAVA-PATH%\\java -jar stomp-1.0.0.jar client");
            }
        } else {
            logger.info("Mauvaise entrée pour lancement de l'application\n" +
                    "Veuilliez respecter un de ces formats : \n" +
                    "\t * %JAVA-PATH%\\java -jar stomp-1.0.0.jar server\n" +
                    "\t * %JAVA-PATH%\\java -jar stomp-1.0.0.jar client");
        }
    }
}
