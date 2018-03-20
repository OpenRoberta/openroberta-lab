package de.fhg.iais.roberta.javaServer.integrationTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

import de.fhg.iais.roberta.testutil.SeleniumHelper;
import de.fhg.iais.roberta.testutil.SeleniumHelper.Button;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

@Ignore
@Category(IntegrationTest.class)
public class JavascriptBasicIT {
    private SeleniumHelper shAsField;

    @Before
    public void setUp() throws Exception {
        this.shAsField = new SeleniumHelper("commonTest");
    }

    @After
    public void tearDown() throws Exception {
        this.shAsField.tearDown();
    }

    @Test
    public void testBasics() throws Exception {
        SeleniumHelper sh = this.shAsField; // for brevity
        // mandatory elements for Java <-> Javascript communication:
        // Is the test running or finished? Is the result expected? Written by Javascript test fixtures.
        sh.assertText("Status", By.id("ready"));
        sh.assertText("Ergebnisse", By.id("result"));

        // see the Javascript test fixture to understand what the clicks effect and why success/error are expected as stated here.

        sh.click(Button.B2);
        sh.expectSuccess();

        sh.click(Button.B1);
        sh.click(Button.B3);
        sh.expectSuccess();

        sh.click(Button.B2);
        sh.expectSuccess();

        sh.click(Button.B3);
        sh.expectError();

        sh.click(Button.B1);
        sh.click(Button.B1);
        sh.click(Button.B2);
        sh.expectError();

        sh.click(Button.B1);
        sh.click(Button.B1);
        sh.click(Button.B3);
        sh.expectError();

        sh.click(Button.B2);
        sh.expectSuccess();

        sh.click(Button.B4);
        sh.click(Button.B2);
        sh.expectSuccess();

        sh.click(Button.B5);
        sh.click(Button.B6);
        sh.expectSuccess();

        sh.click(Button.B7);
        sh.click(Button.B8);
        sh.expectSuccess();

        //sh.click(Button.B2);
        //sh.expectError();
    }
}
