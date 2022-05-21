package network;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static fx.App.client_name;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TrameClient")
public class TrameClientTest {

    @Test
    public void testSend() {
        Trame trame = TrameConstructor.createTrame("SEND",
                new HashMap<>(Map.of("destination", "test", "content-type", "text/plain")),
                "message content");
        assertThat(trame.isSEND()).isTrue();
        assertThat(trame.isValidUNSUBSCRIBE()).isFalse();
        assertThat(trame.isValidSEND()).isTrue();
        assertThat(trame.isValidSUBSCRIBE()).isFalse();
        assertThat(trame.isValidCONNECT()).isFalse();
        assertThat(trame.isValidDISCONNECT()).isFalse();
    }

    @Test
    public void testSubscribe() {
        Trame trame = TrameConstructor.createTrame("SUBSCRIBE",
                new HashMap<>(Map.of("destination", "test", "content-type", "text/plain", "id", "1")),
                "");
        assertThat(trame.isValidUNSUBSCRIBE()).isFalse();
        assertThat(trame.isValidSEND()).isFalse();
        assertThat(trame.isValidSUBSCRIBE()).isTrue();
        assertThat(trame.isValidCONNECT()).isFalse();
        assertThat(trame.isValidDISCONNECT()).isFalse();
    }

    @Test
    public void testUnsubscribe() {
        Trame trame = TrameConstructor.createTrame("UNSUBSCRIBE",
                new HashMap<>(Map.of("id", "1")),
                "");
        assertThat(trame.isValidUNSUBSCRIBE()).isTrue();
        assertThat(trame.isValidSEND()).isFalse();
        assertThat(trame.isValidSUBSCRIBE()).isFalse();
        assertThat(trame.isValidCONNECT()).isFalse();
        assertThat(trame.isValidDISCONNECT()).isFalse();
    }

    @Test
    public void testDisconnect() {
        Trame trame = TrameConstructor.createTrame("DISCONNECT",
                new HashMap<>(Map.of("receipt", "1")),
                "");
        assertThat(trame.isValidUNSUBSCRIBE()).isFalse();
        assertThat(trame.isValidSEND()).isFalse();
        assertThat(trame.isValidSUBSCRIBE()).isFalse();
        assertThat(trame.isValidCONNECT()).isFalse();
        assertThat(trame.isValidDISCONNECT()).isTrue();
    }

    @Test
    public void testConnect() {
        Trame trame = TrameConstructor.createTrame("CONNECT",
                new HashMap<>(Map.of("version", "1.0", "host", "localhost", "content-type", "text/plain")),
                "");
        assertThat(trame.isValidUNSUBSCRIBE()).isFalse();
        assertThat(trame.isValidSEND()).isFalse();
        assertThat(trame.isValidSUBSCRIBE()).isFalse();
        assertThat(trame.isValidCONNECT()).isTrue();
        assertThat(trame.isValidDISCONNECT()).isFalse();
    }
}
