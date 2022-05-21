package network;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TrameServer")
public class TrameServerTest {

    @Test
    public void testMessage() {
        Trame trame = TrameConstructor.createTrame("MESSAGE", new HashMap<>(Map.of(
                "subscription", "1", "message-id",
                "0", "destination", "test",
                "content-type", "text/plain")), "this is the body");
        assertThat(trame.isMESSAGE()).isTrue();
        assertThat(trame.isERROR()).isFalse();
        assertThat(trame.isCONNECTED()).isFalse();
    }

    @Test
    public void testError() {
        Trame trame = TrameConstructor.createTrame("ERROR",
                new HashMap<>(Map.of("content-type", "text/plain", "message", "reason")),
                "this is the body");
        assertThat(trame.isMESSAGE()).isFalse();
        assertThat(trame.isERROR()).isTrue();
        assertThat(trame.isCONNECTED()).isFalse();
    }

    @Test
    public void testConnected() {
        Trame trame = TrameConstructor.createTrame("CONNECTED",
                new HashMap<>(Map.of("version", "1.0", "content-type", "text/plain")),
                "");
        assertThat(trame.isMESSAGE()).isFalse();
        assertThat(trame.isERROR()).isFalse();
        assertThat(trame.isCONNECTED()).isTrue();
    }
}
