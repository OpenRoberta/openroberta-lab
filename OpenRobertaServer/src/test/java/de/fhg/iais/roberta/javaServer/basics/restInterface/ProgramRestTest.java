package de.fhg.iais.roberta.javaServer.basics.restInterface;

import de.fhg.iais.roberta.util.Key;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProgramRestTest extends AbstractRestInterfaceTest {
    @Before
    public void init() throws Exception {
        setup();
        createUsers();
        //login Pid and Minscha
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
    }

    @Test
    public void programWorkflowTest() throws Exception {
        pidCreatesAndUpdates4Programs();
        minschaCreates1ConfAnd2Programs();
        pidSharesProgramsMinschaCanAccessRW();
        pidDeletesProgramsMinschaCannotAccess();
        pidSharesProgram1MinschaCanDeleteTheShare();
        pidAndMinschaAccessConcurrently();
        pidChangedRobotAndSavesProgram();

        saveProgramsAndConfigurations();
    }

    /**
     * test store and update of programs for "pid"<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in<br>
     * <b>PRE:</b> no program exists<br>
     * <b>POST:</b> "pid" owns four programs
     * <ul>
     * <li>save 4 programs -> success; 4 programs found
     * <li>saveAs to an existing program -> error; program must not exist
     * <li>save to a not existing program -> error; program must exist
     * </ul>
     */
    private void pidCreatesAndUpdates4Programs() throws Exception {
        // PRE
        int pidId = this.sPid.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));

        // saveAs 4 programs and check the count in the db
        saveProgramAs(this.sPid, "pid", "pid", "p1", ".1.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sPid, "pid", "pid", "p2", ".2.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sPid, "pid", "pid", "p3", ".3.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sPid, "pid", "pid", "p4", ".4.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        // update (save) program 2 and check the effect in the data base
        saveProgram(this.sPid, pidId, "pid", -1, "p2", ".2.pid.updated", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
        Assert.assertTrue(program.contains(".2.pid.updated"));

        // check that 4 programs are stored, check their names
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());

        // check correct server behavior: (1) the program to save exists (2) the program in saveAs doesn't exist
        saveProgramAs(this.sPid, "pid", "pid", "p4", ".4.pid", null, null, "error", Key.PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS);
        saveProgram(this.sPid, pidId, "pid", 0, "p5", ".5.pid", null, null, "error", Key.PROGRAM_SAVE_ERROR_PROGRAM_TO_UPDATE_NOT_FOUND);

        // POST
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
    }

    /**
     * test store and update of programs for "minscha"<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs<br>
     * <b>PRE:</b> "minscha" owns no programs and no configurations<br>
     * <b>POST:</b> "minscha" owns two programs and one configuration attached to both programs
     * <ul>
     * <li>store a named configuration "c1" and check the db
     * <li>store two programs "p1" and "p2" with default configuration and check the db
     * <li>change the configuration of the programs to "c1" and check the db
     * <li>change the configuration "c1" and check whether the other program is updated implicitly
     * <li>change the configuration of one program to anonymous and check, that this has no effect to the other
     * </ul>
     */
    private void minschaCreates1ConfAnd2Programs() throws Exception {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));

        saveConfigAs(this.sMinscha, minschaId, "c1", "c1.1.conf.minscha", "ok", Key.CONFIGURATION_SAVE_SUCCESS);
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
        String getConfigSql =
            "select d.CONFIGURATION_TEXT from CONFIGURATION c, CONFIGURATION_DATA d where c.CONFIGURATION_HASH = d.CONFIGURATION_HASH and OWNER_ID = "
                + minschaId
                + " and NAME = 'c1'";
        String config = this.memoryDbSetup.getOne(getConfigSql);
        Assert.assertTrue(config.contains("c1.1.conf.minscha"));
        saveConfig(this.sMinscha, minschaId, "c1", CONF_PRE + "c1.2.conf.minscha" + CONF_POST, "ok", Key.CONFIGURATION_SAVE_SUCCESS);
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
        config = this.memoryDbSetup.getOne(getConfigSql);
        Assert.assertTrue(config.contains("c1.2.conf.minscha"));

        saveProgramAs(this.sMinscha, "minscha", "minscha", "p1", ".1.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sMinscha, "minscha", "minscha", "p2", ".2.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        JSONObject responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertTrue(responseJson.has("confXML"));
        saveProgram(this.sMinscha, minschaId, "minscha", -1, "p1", ".1.1.minscha", "c1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertEquals("c1", responseJson.getString("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));
        saveProgram(this.sMinscha, minschaId, "minscha", -1, "p2", "p2.2.1.minscha", "c1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertEquals("c1", responseJson.getString("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));
        saveProgram(
            this.sMinscha,
            minschaId,
            "minscha",
            -1,
            "p1",
            ".1.2.minscha",
            null,
            CONF_PRE + "p1.3.conf.minscha" + CONF_POST,
            "ok",
            Key.PROGRAM_SAVE_SUCCESS);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertFalse(responseJson.has("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("p1.3.conf.minscha"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertEquals("c1", responseJson.getString("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));

        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + minschaId + " and NAME = 'p2'");
        Assert.assertTrue(program.contains("p2.2.1.minscha"));

        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
    }

    /**
     * share a programm and access it<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs, "minscha" owns two programs<br>
     * <b>PRE:</b> "minscha" can access his two programs, nothing is shared<br>
     * <b>POST:</b> "minscha" can access his two programs and two shared programs
     * <ul>
     * <li>"pid" shares her programs "p2" R and "p3" W with "minscha"
     * <li>"minscha" cannot modify "p2", but can modify "p3"
     * </ul>
     */
    private void pidSharesProgramsMinschaCanAccessRW() throws Exception {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2']");
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));

        String shareRead = "{'type':'User';'label':'minscha';'right':'READ'}";
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p2';'shareData':" + shareRead + "}", "ok", Key.ACCESS_RIGHT_CHANGED);
        String shareWrite = "{'type':'User';'label':'minscha';'right':'WRITE'}";
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p3';'shareData':" + shareWrite + "}", "ok", Key.ACCESS_RIGHT_CHANGED);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
        JSONArray programListing = assertProgramListingAsExpected(this.sMinscha, "['p1','p2', 'p2', 'p3']");
        boolean ownershipOk = false;
        for ( int i = 0; i < programListing.length(); i++ ) {
            JSONArray programInfo = programListing.getJSONArray(i);
            if ( programInfo.getString(0).equals("p3") ) {
                Assert.assertEquals("p3 is owned by pid", "pid", programInfo.getString(1));
                ownershipOk = true;
                break;
            }
        }
        Assert.assertTrue(ownershipOk);

        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.1.minscha"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p3';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".3.pid"));

        saveProgram(this.sMinscha, pidId, "pid", -1, "p2", ".2.minscha.update", null, null, "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
        String p2Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
        Assert.assertTrue(p2Text.contains(".2.pid.updated"));
        saveProgram(this.sMinscha, pidId, "pid", -1, "p3", ".3.minscha.update", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        String p3Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p3'");
        Assert.assertTrue(p3Text.contains(".3.minscha.update"));

        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2', 'p2', 'p3']");
    }

    /**
     * deleting a program removes the share<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in<br>
     * <b>PRE:</b> "minscha" can access his two programs and two shared programs<br>
     * <b>POST:</b> "minscha" can access his program "p1" and no shared programs
     * <ul>
     * <li>"minscha" deletes his program "p2" -> success; the programm cannot be loaded anymor, the programm cannot be deleted a second time, "minscha"
     * continues to see a program "p2" (the program shared with "pid")
     * <li>"pid" deletes her programs "p2" and "p3" -> success; nothing shared anymore, the visible programs match, neither owner nor sharer can load a deleted
     * program
     * </ul>
     */
    private void pidDeletesProgramsMinschaCannotAccess() throws Exception {
        int pidId = this.sPid.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2','p2','p3']"); // p2 is from "pid"!

        restProgram(this.sMinscha, "{'cmd':'deleteP';'programName':'p2';'author':'minscha'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restProgram(
            this.sMinscha,
            "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'robot':'ev3';'author':'minscha'}",
            "error",
            Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'deleteP';'programName':'p2';'author':'minscha'}", "error", Key.PROGRAM_DELETE_ERROR);
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2','p3']"); // p2 is from "pid"!

        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));
        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p2';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p3';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1']");
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));

        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p3';'owner':'pid';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);

        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p3'"));
    }

    /**
     * it is possible to delete the share. This doesn't delete the program :-)<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns program "p4"<br>
     * <b>PRE:</b> "minscha" can access his program "p1" and no shared programs<br>
     * <b>POST:</b> "minscha" can access his program "p1" and no shared programs
     * <ul>
     * <li>"pid" shares program "p4" W with "minscha"
     * <li>"minscha" can write it
     * <li>"pid" does see the change<br>
     * <br>
     * <li>"minscha" can delete the share
     * <li>"minscha" cannot write it anymore
     * <li>the program continues to exist (for "pid"), but it vanishes from the program list of "minscha"
     * </ul>
     */
    private void pidSharesProgram1MinschaCanDeleteTheShare() throws Exception, JSONException {
        {
            int pidId = this.sPid.getUserId();
            int minschaId = this.sMinscha.getUserId();
            Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
            assertProgramListingAsExpected(this.sPid, "['p1','p4']");
            assertProgramListingAsExpected(this.sMinscha, "['p1']");

            String shareWrite = "{'type':'User';'label':'minscha';'right':'WRITE'}";
            restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'shareData':" + shareWrite + "}", "ok", Key.ACCESS_RIGHT_CHANGED);
            assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");

            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
            String p4Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4Text.contains(".4.pid"));
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid"));
            saveProgram(this.sMinscha, pidId, "pid", -1, "p4", ".4.minscha.update", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));

            restProgram(this.sMinscha, "{'cmd':'shareDelete';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.ACCESS_RIGHT_DELETED);
            saveProgram(this.sMinscha, pidId, "pid", -1, "p4", ".5.minscha.fail", null, null, "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
            assertProgramListingAsExpected(this.sMinscha, "['p1']");
            assertProgramListingAsExpected(this.sPid, "['p1','p4']");
            String p4TextUpd2 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd2.contains(".4.minscha.update"));
            Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
        }
    }

    /**
     * the lastChanged-timestamp is used to guarantee an optimistic locking: - "pid" shares program "p4" W with "minscha"
     * <ul>
     * <li>"pid" reads "p4" (with lastChanged == timestamp X1
     * <li>"minscha" reads "p4" (with the same lastChanged == timestamp X1
     * <li>"pid" can write (lastChanged becomes timestamp X2)
     * <li>"minscha" cannot write (because lastChanged changed :-)
     * <li>Vice versa: if "minscha" writes back before "pid" writes back, "pid"s write fails
     * </ul>
     */
    private void pidAndMinschaAccessConcurrently() throws Exception, JSONException {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1']");

        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        saveProgram(this.sPid, pidId, "pid", -1, "p4", ".4.pId", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        String shareWrite = "{'type':'User';'label':'minscha';'right':'WRITE'}";
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'shareData':" + shareWrite + "}", "ok", Key.ACCESS_RIGHT_CHANGED);

        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");

        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p1'"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p4'"));
        String programP4OfPid = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
        Assert.assertTrue(programP4OfPid.contains(".4.pId"));

        // scenario 1: minscha reads pid's p4, then he writes; pid doesn't use her program; the timestamp increases
        {
            Thread.sleep(500); // REST-call should be executed sequentially. The sleep is NO guaranty ... Otherwise see below!
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pId"));
            long lastChanged1 = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            saveProgram(this.sMinscha, pidId, "pid", lastChanged1, "p4", ".4.minscha.update", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
            Thread.sleep(500); // REST-call should be executed sequentially. The sleep is NO guaranty ... Otherwise see below!
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
            long lastChanged2 = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            Assert.assertTrue("causality violated: changed2 must be later than changed1", lastChanged2 > lastChanged1); // here sometimes a time race occurs. This may generate a test error.
        }
        final Key LOCK_ERROR = Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING;
        // scenario 2: minscha reads pid's p4, then pid reads her p4; pid stores her program, but minscha can't (his timestamp is outdated)
        {
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long minschaReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long pidReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
            saveProgram(this.sPid, pidId, "pid", pidReadTimestamp, "p4", ".4.pid.concurrentOk", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
            saveProgram(this.sMinscha, pidId, "pid", minschaReadTimestamp, "p4", ".4.minscha.concurrentFail", null, null, "error", LOCK_ERROR);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(program.contains(".4.pid.concurrentOk"));
        }
        // scenario 3: minscha reads pid's p4, then pid reads her p4; minscha stores the shared program, but pid can't (her timestamp is outdated)
        {
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long minschaReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long pidReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
            saveProgram(this.sMinscha, pidId, "pid", minschaReadTimestamp, "p4", ".4.minscha.concurrentOk", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
            saveProgram(this.sPid, pidId, "pid", pidReadTimestamp, "p4", ".4.pid.concurrentFail", null, null, "error", LOCK_ERROR);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(program.contains(".4.minscha.concurrentOk"));
        }
    }

    /**
     * create a program with a different robot for "pid"<br>
     * <b>INVARIANT:</b> two users exist, both user have logged in<br>
     * <b>PRE:</b> "pid" has 2 programs both are ev3 programs<br>
     * <b>POST:</b> "pid" has 3 programs one is a calliope programm
     * <ul>
     * <li>"pid" changes robot to calliope
     * <li>"pid" saves a new program
     * <li>"pid" can no longer see his ev3 programs but only the new calliope one
     * <li>"pid" changes back to ev3
     * <li>"pid" can now see his old ev3 programs again
     * </ul>
     */
    private void pidChangedRobotAndSavesProgram() throws Exception {
        //check if pid is logged in
        int pidId = this.sPid.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn());

        //change robot and check if it worked
        restClient.setRobot(mkFRR(this.sPid.getInitToken(), "{'cmd':'setRobot'; 'robot':'calliope2017'}"));
        Assert.assertEquals("calliope2017", this.sPid.getRobotName());

        //save new Program and check number of Programs
        saveProgramAs(this.sPid, "pid", "pid", "p5", ".1.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(3, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
        //check that 1 program is is in current session state and check its name
        assertProgramListingAsExpected(this.sPid, "['p5']");

        //swap back
        restClient.setRobot(mkFRR(this.sPid.getInitToken(), "{'cmd':'setRobot'; 'robot':'ev3lejosv1'}"));
        Assert.assertEquals("ev3lejosv1", this.sPid.getRobotName());
        //check if program list of ev3 prorams remained unchanged
        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
    }

    /**
     * check, that the relationship between programs and configurations works<br>
     * <br>
     * 1. save a new config. Check, that a name and data is required. Check, that the name doesn't exist. Check, that the data is ok.<br>
     * 2. save a new program with default configuration, anonymous configuration and named configuration. Check, that the data is ok.<br>
     */
    private void saveProgramsAndConfigurations() throws Exception {
        int minschaId = this.sMinscha.getUserId();

        {
            saveConfigAs(this.sMinscha, minschaId, "mc1", "mc1.1.conf.minscha", "ok", Key.CONFIGURATION_SAVE_SUCCESS);
            saveConfigAs(this.sMinscha, minschaId, null, "mc1.1.conf.minscha", "error", Key.SERVER_ERROR);
            saveConfigAs(this.sMinscha, minschaId, "mc2", null, "error", Key.SERVER_ERROR);
            saveConfigAs(this.sMinscha, minschaId, "mc1", "mc1.2.conf.minscha", "error", Key.CONFIGURATION_SAVE_AS_ERROR_CONFIGURATION_EXISTS);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where NAME like 'mc%'"));
            String confHash = this.memoryDbSetup.getOne("select CONFIGURATION_HASH from CONFIGURATION where NAME = 'mc1'");
            String confText = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + confHash + "'");
            Assert.assertTrue(confText.contains("mc1.1.conf.minscha"));
        }

        {
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp1", "mp1.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp2", "mp2.minscha", null, "mp2.conf.minscha", "ok", Key.PROGRAM_SAVE_SUCCESS);
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp3", "mp3.minscha", "mc1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp4", "mp3.minscha", "mc1", "mp2.conf.minscha", "error", Key.SERVER_ERROR);
            Assert.assertEquals(3, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where NAME like 'mp%'"));
            String cnMp1 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp1'");
            String chMp1 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp1'");
            Assert.assertTrue(cnMp1 == null && chMp1 == null);
            String cnMp2 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp2'");
            String chMp2 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp2'");
            Assert.assertTrue(cnMp2 == null && chMp2 != null);
            String ctMp2 = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + chMp2 + "'");
            Assert.assertTrue(ctMp2 != null);
            String cnMp3 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp3'");
            String chMp3 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp3'");
            Assert.assertTrue(cnMp3 != null && chMp3 == null);
            String chMc1 = this.memoryDbSetup.getOne("select CONFIGURATION_HASH from CONFIGURATION where NAME = 'mc1'");
            String ctMc1 = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + chMc1 + "'");
            Assert.assertTrue(ctMc1.contains("mc1.1.conf.minscha"));
        }
    }

}