package de.fhg.iais.roberta;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.visitor.JumpLinker;
import static de.fhg.iais.roberta.util.visitor.JumpLinker.JumpTarget.BREAK;

public class JumpLinkerTest {

    private JumpLinker jumpLinker;

    @Before
    public void setUp() throws Exception {
        this.jumpLinker = new JumpLinker();
    }

    @Test
    public void registerAndReturn() {
        JSONObject jump = new JSONObject().put(C.CONDITIONAL, C.ALWAYS);
        jumpLinker.register(jump, BREAK);
        Assertions.assertThat(jumpLinker.isEmpty()).isFalse();

        List<JSONObject> jumps = jumpLinker.handle(BREAK);
        Assertions.assertThat(jumps)
            .hasSize(1)
            .allMatch(returnJump -> returnJump.equals(jump));
        Assertions.assertThat(jumpLinker.isEmpty()).isTrue();
    }

    @Test
    public void registerInIsolation() {
        jumpLinker.register(new JSONObject().put(C.CONDITIONAL, C.ALWAYS), BREAK);
        jumpLinker.isolate(() -> {
            Assertions.assertThat(jumpLinker.handle(BREAK)).isEmpty();
            Assertions.assertThat(jumpLinker.isEmpty()).isTrue();
        });
        Assertions.assertThat(jumpLinker.handle(BREAK)).isNotEmpty();
        Assertions.assertThat(jumpLinker.isEmpty()).isTrue();
    }

    @Test
    public void keepsIfNotHandled() {
        jumpLinker.isolate(() -> {
            jumpLinker.register(new JSONObject().put(C.CONDITIONAL, C.ALWAYS), BREAK);
            Assertions.assertThat(jumpLinker.isEmpty()).isFalse();
        });
        Assertions.assertThat(jumpLinker.handle(BREAK)).hasSize(1);
    }

    @Test
    public void removesIfHandled() {
        jumpLinker.isolate(() -> {
            jumpLinker.register(new JSONObject().put(C.CONDITIONAL, C.ALWAYS), BREAK);
            Assertions.assertThat(jumpLinker.handle(BREAK)).hasSize(1);
        });
        Assertions.assertThat(jumpLinker.isEmpty()).isTrue();
        Assertions.assertThat(jumpLinker.handle(BREAK)).isEmpty();
    }


}