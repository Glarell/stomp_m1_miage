package network;

import java.util.HashMap;

/**
 * The type Trame.
 */
public class Trame {

    private String type;
    private HashMap<String, String> headers;
    private String body;

    /**
     * Instantiates a new Trame.
     *
     * @param type    the type
     * @param headers the headers
     * @param body    the body
     */
    public Trame(String type, HashMap<String, String> headers, String body) {
        this.type = type;
        this.headers = headers;
        this.body = body;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets headers.
     *
     * @param headers the headers
     */
    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--- Type de la trame : ").append(this.type).append(" ---\n");
        stringBuilder.append("--- Headers ---\n");
        if (this.headers != null) {
            this.headers.forEach((x, y) -> stringBuilder.append(x).append(" : ").append(y).append("\n"));
        }
        stringBuilder.append("--- Body ---\n").append(this.body);
        return stringBuilder.toString();
    }

    /**
     * To send string.
     *
     * @return the string
     */
    public String toSend() {
        String new_line = System.lineSeparator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.type).append(new_line);
        if (this.headers != null) {
            this.headers.forEach((x, y) -> stringBuilder.append(x).append(":").append(y).append(new_line));
        }
        stringBuilder.append(new_line);
        if (!this.body.equals("")) {
            stringBuilder.append(this.body).append(new_line);
        }
        stringBuilder.append("^@");
        return stringBuilder.toString();
    }

    /**
     * Is send boolean.
     *
     * @return the boolean
     */
    public boolean isSEND() {
        return this.type.equals("SEND");
    }

    /**
     * Is subscribe boolean.
     *
     * @return the boolean
     */
    public boolean isSUBSCRIBE() {
        return this.type.equals("SUBSCRIBE");
    }

    /**
     * Is unsubscribe boolean.
     *
     * @return the boolean
     */
    public boolean isUNSUBSCRIBE() {
        return this.type.equals("UNSUBSCRIBE");
    }

    /**
     * Is disconnect boolean.
     *
     * @return the boolean
     */
    public boolean isDISCONNECT() {
        return this.type.equals("DISCONNECT");
    }

    /**
     * Is connect boolean.
     *
     * @return the boolean
     */
    public boolean isCONNECT() {
        return this.type.equals("CONNECT");
    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isCONNECTED() {
        return this.type.equals("CONNECTED");
    }

    {
    }

    /**
     * Is message boolean.
     *
     * @return the boolean
     */
    public boolean isMESSAGE() {
        return this.type.equals("MESSAGE");
    }

    /**
     * Is error boolean.
     *
     * @return the boolean
     */
    public boolean isERROR() {
        return this.type.equals("ERROR");
    }

    /**
     * Is valid subscribe boolean.
     *
     * @return the boolean
     */
    public boolean isValidSUBSCRIBE() {
        if (isSUBSCRIBE()) {
            if (this.headers.containsKey("destination") && this.headers.containsKey("id")) {
                try {
                    int temp = Integer.parseInt(this.headers.get("id"));
                    if (this.headers.get("destination").isBlank() || this.headers.get("destination").isEmpty()) {
                        return false;
                    }
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Is valid connect boolean.
     *
     * @return the boolean
     */
    public boolean isValidCONNECT() {
        if (isCONNECT()) {
            if (this.headers.containsKey("version") && this.headers.containsKey("content-type")) {
                if (this.headers.get("version").equals("1.0") && this.headers.get("content-type").equals("text/plain")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Is valid unsubscribe boolean.
     *
     * @return the boolean
     */
    public boolean isValidUNSUBSCRIBE() {
        if (isUNSUBSCRIBE()) {
            if (this.headers.containsKey("id")) {
                try {
                    Integer.parseInt(this.headers.get("id"));
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Is valid send boolean.
     *
     * @return the boolean
     */
    public boolean isValidSEND() {
        if (isSEND()) {
            if (this.headers.containsKey("destination") && this.headers.containsKey("content-type")) {
                if (this.headers.get("destination").isBlank() || this.headers.get("destination").isEmpty()) {
                    return false;
                } else {
                    return !this.getBody().isEmpty() && !this.body.isBlank();
                }
            }
        }
        return false;
    }

    /**
     * Is valid disconnect boolean.
     *
     * @return the boolean
     */
    public boolean isValidDISCONNECT() {
        return this.headers.containsKey("receipt") &&
                this.getBody().isEmpty() &&
                isDISCONNECT();
    }

    /**
     * Is valid message boolean.
     *
     * @return the boolean
     */
    public boolean isValidMESSAGE() {
        return isMESSAGE() &&
                this.headers.containsKey("subscription") &&
                this.headers.containsKey("message-id") &&
                this.headers.containsKey("destination") &&
                this.headers.containsKey("content-type");
    }

    /**
     * Is valid error boolean.
     *
     * @return the boolean
     */
    public boolean isValidERROR() {
        return isERROR() &&
                this.headers.containsKey("version") &&
                this.headers.containsKey("content-type");
    }

    /**
     * Is valid connected boolean.
     *
     * @return the boolean
     */
    public boolean isValidCONNECTED() {
        return isCONNECTED() && this.headers.containsKey("version");
    }
}
