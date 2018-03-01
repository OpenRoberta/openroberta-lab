package de.fhg.iais.roberta.syntax.codegen.nao;

import org.junit.Before;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.nao.NAOConfiguration;
import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class PythonVisitorTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    private static final String IMPORTS =
        "" //
            + "#!/usr/bin/python\n\n"
            + "import math\n"
            + "import time\n"
            + "from hal import Hal\n"
            + "h = Hal()\n\n"
            + "def run():\n";

    private static final String SUFFIX =
        "" //
            + "\ndef main():\n"
            + "    try:\n"
            + "        run()\n"
            + "    except Exception as e:\n"
            + "        h.say(\"Error!\")\n\n"
            + "if __name__ == \"__main__\":\n"
            + "    main()";

    private Configuration brickConfiguration;

    @Before
    public void setupConfigurationForAllTests() {
        Configuration.Builder<?> configuration = new NAOConfiguration.Builder();
        brickConfiguration = configuration.build();
    }

    // TODO: where are the tests?
}
