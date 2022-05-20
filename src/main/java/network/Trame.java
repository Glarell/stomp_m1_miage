package network;

import java.util.HashMap;

public class Trame {

    private String type;
    private HashMap<String, String> headers;
    private String body;

    public Trame(String type, HashMap<String, String> headers, String body) {
        this.type = type;
        this.headers = headers;
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

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

    public boolean isSEND() {
        return this.type.equals("SEND");
    }

    public boolean isSUBSCRIBE() {
        return this.type.equals("SUBSCRIBE");
    }

    public boolean isUNSUBSCRIBE() {
        return this.type.equals("UNSUBSCRIBE");
    }

    public boolean isDISCONNECT() {
        return this.type.equals("DISCONNECT");
    }

    public boolean isCONNECT() {
        return this.type.equals("CONNECT");
    }

    public boolean isCONNECTED() {
        return this.type.equals("CONNECTED");
    }

    {
    }

    public boolean isMESSAGE() {
        return this.type.equals("MESSAGE");
    }

    public boolean isERROR() {
        return this.type.equals("ERROR");
    }

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

    public boolean isValidUNSUBSCRIBE() {
        if (isUNSUBSCRIBE()) {
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

    public boolean isValidSEND() {
        if (isSEND()) {
            if (this.headers.containsKey("destination") && this.headers.containsKey("content-type")) {
                if (this.headers.get("destination").isBlank() || this.headers.get("destination").isEmpty()) {
                    return false;
                } else {
                    if (this.getBody().isEmpty() || this.body.isBlank()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
