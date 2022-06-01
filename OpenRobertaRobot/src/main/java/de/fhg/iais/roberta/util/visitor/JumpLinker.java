package de.fhg.iais.roberta.util.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

/**
 * Class to manage delayed location assignments of generated Jumps.
 * <p>
 * Isolate a jump context (e.g. for loop to catch break and continue) using {@link #isolate(Runnable)}.<br>
 * Register jumps with currently unknown target location using {@link #register(JSONObject, JumpTarget)} inside the runnable of {@link #isolate(Runnable)}.<br>
 * Then {@link #handle(JumpTarget...)} jumps of certain categories and leave those who are not meant for the current context to be handled by the context above<br>
 */
public class JumpLinker {

    private Map<JumpTarget, List<JSONObject>> jumpTargetMap;

    public JumpLinker() {
        jumpTargetMap = createEmptyMap();
    }

    private Map<JumpTarget, List<JSONObject>> createEmptyMap() {
        Map<JumpTarget, List<JSONObject>> map = new HashMap<>();
        Arrays.stream(JumpTarget.values()).forEach((target) -> map.put(target, new ArrayList<>()));
        return map;
    }

    /**
     * True if there are no jumps currently registered in this context, independent of JumpTarget.
     */
    public boolean isEmpty() {
        return jumpTargetMap.values().stream().allMatch(List::isEmpty);
    }

    /**
     * Generates a list of all jumps registered with the given targets and removes the respective jumps from the registry.
     *
     * @param targets which JumpTargets to handle
     * @return a list of all jumps registered with under the given targets
     */
    public List<JSONObject> handle(JumpTarget... targets) {
        List<JSONObject> allJumps = Arrays.stream(targets)
            .map(jumpTargetMap::get)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        Arrays.stream(targets).forEach((jumpTarget -> jumpTargetMap.get(jumpTarget).clear()));

        return allJumps;
    }

    /**
     * Essential method of {@link JumpLinker}.<br>
     * Used to catch only those jumps (e.g. breaks in a for loop) registered inside the execution of runnable. <br>
     * All jumps registered before the execution are not accessible inside the isolation. <br>
     * After the execution all jumps from before the execution are restored and the jumps not handle during the execution are added on top in the respective categories <br>
     *
     * <br>
     * <b>Example</b>:<br>
     * <em>State before execution:</em><br>
     * BREAK: [Jump1, Jump2]<br>
     * <br>
     * <em>State while execution:</em><br>
     * BREAK: [ ]<br>
     * <br>
     * <em>Register inside execution:</em><br>
     * BREAK: [Jump3]<br>
     * <br>
     * <em>State after execution</em><br>
     * BREAK: [Jump1, Jump2, Jump3]<br>
     *
     * @param runnable code which should be run isolated
     */
    public void isolate(Runnable runnable) {
        Map<JumpTarget, List<JSONObject>> tempTargetList = jumpTargetMap;
        jumpTargetMap = createEmptyMap();

        runnable.run();

        jumpTargetMap.forEach((jumpTarget, list) -> list.addAll(tempTargetList.get(jumpTarget)));
    }

    /**
     * Registers a jump with currently unknown target to resolve target later.
     *
     * @param jump the jump object to register
     * @param target the target type under which the jump will be accessible later
     */
    public void register(JSONObject jump, JumpTarget target) {
        jumpTargetMap.get(target).add(jump);
    }

    public enum JumpTarget {
        BREAK, CONTINUE, INTERNAL_BREAK, STATEMENT_END, METHOD_END
    }

}

