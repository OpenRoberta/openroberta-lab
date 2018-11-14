package de.fhg.iais.roberta.util.test;

import org.json.JSONObject;

import de.fhg.iais.roberta.factory.BlocklyDropdown2EnumHelper;
import de.fhg.iais.roberta.util.Util1;

public class GeneralTestsHelper {
    public static void loadBlocks(String uri) {
        JSONObject robotDescription = new JSONObject();
        Util1.loadYAMLRecursive("", robotDescription, uri);
        BlocklyDropdown2EnumHelper.loadBlocks(robotDescription);
    }

}
