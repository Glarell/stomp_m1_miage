package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Trame constructor.
 */
public class TrameConstructor {

    /**
     * The constant client_headers.
     */
    public static final ArrayList<String> client_headers = new ArrayList<>(List.of("SEND", "SUBSCRIBE", "DISCONNECT", "CONNECT"));
    /**
     * The constant server_headers.
     */
    public static final ArrayList<String> server_headers = new ArrayList<>(List.of("CONNECTED", "MESSAGE", "ERROR"));

    /**
     * Create trame trame.
     *
     * @param type    the type
     * @param headers the headers
     * @param body    the body
     * @return the trame
     */
    public static Trame createTrame(String type, HashMap<String, String> headers, String body) {
        return new Trame(type, headers, body);
    }

    /**
     * Parse trame.
     *
     * @param message         the message
     * @param default_headers the default headers
     * @return the trame
     */
    public static Trame parse(String message, ArrayList<String> default_headers) {
        String new_line = System.lineSeparator();
        String[] lines = message.split(new_line);
        if (message.contains("^@") && default_headers.contains(lines[0]) && lines.length >= 2) {
            String type = lines[0];
            HashMap<String, String> headers = new HashMap<>();
            StringBuilder body = new StringBuilder();
            boolean hasSeenHeaders = false;
            for (int i = 1; i <= lines.length; i++) {
                if (lines[i].contains(":") && !hasSeenHeaders) {
                    headers.put(lines[i].split(":")[0], lines[i].split(":")[1]);
                }else if (lines[i].equals("")){
                    hasSeenHeaders = true;
                } else if (!lines[i].equals("^@") && hasSeenHeaders) {
                    body.append(lines[i]);
                } else {
                    return new Trame(type, headers, body.toString());
                }
            }
        }
        return null;
    }

    /**
     * Parse trame client trame.
     *
     * @param message the message
     * @return the trame
     */
    public static Trame parseTrameClient(String message) {
        return parse(message, client_headers);
    }

    /**
     * Parse trame serveur trame.
     *
     * @param message the message
     * @return the trame
     */
    public static Trame parseTrameServeur(String message) {
        return parse(message, server_headers);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        String test = "SEND\r\ndestination:/queue/a\r\ncontent-type:text/plain\r\n\r\nCoucou les gens\r\n^@";
        System.out.println(parseTrameClient(test).toString());
    }
}

