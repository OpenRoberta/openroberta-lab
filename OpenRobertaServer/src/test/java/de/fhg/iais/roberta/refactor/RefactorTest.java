//package de.fhg.iais.roberta.refactor;
//
//import java.util.Properties;
//import java.util.Set;
//
//import org.json.JSONObject;
//import org.junit.Test;
//
//import de.fhg.iais.roberta.factory.BlocklyDropdownFactoryHelper;
//import de.fhg.iais.roberta.util.Util;
//import de.fhg.iais.roberta.util.syntax.BlockType;
//import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
//
//public class RefactorTest {
//    /**
//     * this test class generates data, that is needed for refactoring the worker, visitor, AST structure.
//     * It is here for documentation.
//     */
//    @Test
//    public void refactorTest() {
//        String[] robots = {"wedo", "ev3lejosv1", "ev3lejosv0", "ev3dev", "ev3c4ev3", "nxt", "microbit", "botnroll", "nao", "bob3", "rob3rta", "sensebox",
//            "mbot", "mbot2", "edison", "festobionic", "festobionicflower", "uno", "unowifirev2", "nano", "mega", "nano33ble",
//            "calliope2017NoBlue", "calliope2017", "calliope2016"};
//        for ( String robot : robots ) {
//            Properties pluginProperties = Util.loadProperties("classpath:/" + robot + ".properties");
//            String robotDescriptor = pluginProperties.getProperty("robot.descriptor");
//            JSONObject robotDescription = new JSONObject();
//            Util.loadYAMLRecursive("", robotDescription, robotDescriptor, false);
//            BlocklyDropdownFactoryHelper.loadBlocks(robotDescription);
//        }
//        Set<String> blocktypeNames = BlockTypeContainer.getNames();
//        for ( String blocktypeName : blocktypeNames ) {
//            BlockType blockType = BlockTypeContainer.getByName(blocktypeName);
//            Class<?> astClass = blockType.getAstClass();
//            String simpleName = astClass.getSimpleName();
//            String path = astClass.getName().replaceAll("\\.", "/") + ".java";
//            String category = blockType.getCategory().toString();
//            String refactor = "./refactor.sh '" + blocktypeName.toUpperCase() + "' '" + simpleName + "' '" + path + "' '" + category + "' '{";
//            Set<String> blocklyNames = blockType.getBlocklyNames();
//            boolean first = true;
//            for ( String blocklyName : blocklyNames ) {
//                if ( first ) {
//                    first = false;
//                } else {
//                    refactor = refactor + ",";
//                }
//                refactor = refactor + "\"" + blocklyName + "\"";
//            }
//            refactor = refactor + "}'";
//            System.out.println(refactor);
//        }
//    }
//}
