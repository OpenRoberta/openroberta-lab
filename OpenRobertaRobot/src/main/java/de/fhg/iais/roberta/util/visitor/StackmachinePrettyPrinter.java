package de.fhg.iais.roberta.util.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class StackmachinePrettyPrinter {
    public static String prettyPrint(JSONObject stackMachineCodeWrapped, boolean withPlusMinus, boolean withDebugStop) {
        JSONArray stackMachineCode = stackMachineCodeWrapped.getJSONArray("ops");
        List<String> prettyprintedOps = new ArrayList<>();
        for ( int i = 0; i < stackMachineCode.length(); i++ ) {
            StringBuilder sb = new StringBuilder();
            JSONObject stackMachineOp = stackMachineCode.getJSONObject(i);
            String opc = stackMachineOp.getString("opc");
            String label = stackMachineOp.optString("label", "");
            sb.append(String.format("%4d: %-10s %-16s", i, label, opc));
            Set<String> opKeySet = new TreeSet(stackMachineOp.keySet());
            for ( String key : opKeySet ) {
                if ( key.equals("opc") || key.equals("label") || key.equals("+") || key.equals("-") || key.equals("possibleDebugStop") ) {
                    // already printed or printed later
                } else {
                    sb.append(key).append(": ").append(stackMachineOp.get(key)).append(", ");
                }
            }
            prettyprintedOps.add(sb.toString());
        }
        StringBuilder sb = new StringBuilder();
        Optional<Integer> maxOpLenOpt = prettyprintedOps.stream().map(o -> o.length()).reduce(Integer::max);
        if ( maxOpLenOpt.isPresent() ) {
            int maxOpLen = maxOpLenOpt.get();
            String format = "%-" + maxOpLen + "s > ";
            for ( int i = 0; i < prettyprintedOps.size(); i++ ) {
                if ( withDebugStop || withPlusMinus ) {
                    sb.append(String.format(format, prettyprintedOps.get(i)));
                    JSONObject stackMachineOp = stackMachineCode.getJSONObject(i);
                    Set<String> opKeySet = new TreeSet(stackMachineOp.keySet());
                    for ( String key : opKeySet ) {
                        if ( withPlusMinus && (key.equals("+") || key.equals("-")) ) {
                            sb.append(key).append(": ").append(stackMachineOp.get(key)).append(", ");
                        } else if ( withDebugStop && key.equals("possibleDebugStop") ) {
                            sb.append(key).append(": ").append(stackMachineOp.get(key)).append(", ");
                        }
                    }
                } else {
                    sb.append(prettyprintedOps.get(i));
                }
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
