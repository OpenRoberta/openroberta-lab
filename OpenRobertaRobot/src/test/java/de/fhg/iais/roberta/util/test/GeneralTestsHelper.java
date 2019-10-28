package de.fhg.iais.roberta.util.test;

import org.json.JSONObject;

import de.fhg.iais.roberta.factory.BlocklyDropdownFactoryHelper;
import de.fhg.iais.roberta.util.Util;

public class GeneralTestsHelper {
    public static void loadBlocks(String uri) {
        JSONObject robotDescription = new JSONObject();
        Util.loadYAMLRecursive("", robotDescription, uri, false);
        BlocklyDropdownFactoryHelper.loadBlocks(robotDescription);
    }

}
