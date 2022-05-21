package network;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Trame server test.
 */
@DisplayName("TrameServer")
public class TrameServerTest {

    /**
     * Test message.
     */
    @Test
    public void testMessage() {
        Trame trame = TrameConstructor.createTrame("MESSAGE", new HashMap<>(Map.of(
                "subscription", "1", "message-id",
                "0", "destination", "test",
                "content-type", "text/plain")), "this is the body");
        assertThat(trame.isValidMESSAGE()).isTrue();
        assertThat(trame.isValidERROR()).isFalse();
        assertThat(trame.isValidCONNECTED()).isFalse();
    }

    /**
     * Test error.
     */
    @Test
    public void testError() {
        Trame trame = TrameConstructor.createTrame("ERROR",
                new HashMap<>(Map.of("content-type", "text/plain", "version", "1.0")),
                "this is the body");
        assertThat(trame.isValidMESSAGE()).isFalse();
        assertThat(trame.isValidERROR()).isTrue();
        assertThat(trame.isValidCONNECTED()).isFalse();
    }

    /**
     * Test connected.
     */
    @Test
    public void testConnected() {
        Trame trame = TrameConstructor.createTrame("CONNECTED",
                new HashMap<>(Map.of("version", "1.0", "content-type", "text/plain")),
                "");
        assertThat(trame.isValidMESSAGE()).isFalse();
        assertThat(trame.isValidERROR()).isFalse();
        assertThat(trame.isValidCONNECTED()).isTrue();
    }
}
