package de.fhg.iais.roberta.javaServer.basics.restInterface;

import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExportAllRestTest extends AbstractRestInterfaceTest {

    @Before
    public void init() throws Exception {
        setup();
        createUsers();

        //login Pid and Minscha
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        //create test group and programs
        pidAndMinschaCreateSomePrograms();
        groupSetup();
    }

    /**
     * "Minscha" tries to export all of his programs<br>
     * <b>INVARIANT:</b> user "Minscha" exists and is logged in<br>
     * <b>PRE:</b> "Minscha" owns one ev3 program and no Groups<br>
     * <b>POST:</b> a zip entity is created with his program in a folder named ev3
     * <ul>
     * <li>assure "Minscha" is logged in
     * <li>export his program and convert them to a ZipInputStream
     * <li>analyze his folder structure and if the program was exported
     * </ul>
     */
    @Test
    public void exportNoGroupProgramsTest() throws Exception {
        Assert.assertTrue(this.sMinscha.isUserLoggedIn());
        ZipInputStream zin1 = exportAllProgramsAsZipInputStream(this.sMinscha);

        //check directory structure
        //Minscha owns one program
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + this.sMinscha.getUserId()));
        //No directory except the robot directory should be created
        ZipEntry exportedProgram = zin1.getNextEntry();
        assert exportedProgram != null;
        Assert.assertEquals("ev3/p1.xml", exportedProgram.getName());
        Assert.assertNull(zin1.getNextEntry());
    }

    /**
     * checks if "Mischas" program is exported correctly<br>
     * <b>INVARIANT:</b> user "Minscha" exists and is logged in<br>
     * <b>PRE:</b> "Minscha" owns one ev3 program and no Groups<br>
     * <b>POST:</b> a zip entity is created with his program in a folder named ev3
     * <ul>
     * <li>export his program and convert them to a ZipInputStream
     * <li>get the first program of the zip entity as a String
     * <li>check if the xml text of the String is correct
     * </ul>
     */
    @Test
    public void checkXmlTest() throws Exception {
        ZipInputStream zis = exportAllProgramsAsZipInputStream(this.sMinscha);
        String xmlText = firstProgramOfZipToString(zis);

        //check if xml text is correct
        Assert.assertEquals(
            "<export xmlns=\"http://de.fhg.iais.roberta.blockly\"><program><block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" " +
                "robottype=\"ev3\" xmlversion=\"2.0\" description=\"\" tags=\"\"><instance x=\"512\" y=\"50\"><block type=\"robControls_start\" " +
                "id=\"RDF[XZ?y7bn;Z{?V}Q)(\" intask=\"true\" deletable=\"false\"><mutation declare=\"false\"></mutation><field name=\"DEBUG\">FALSE</field><comment " +
                "w=\"0\" h=\"0\" pinned=\"false\">progTextToExport</comment></block></instance></block_set></program><config><block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" " +
                "robottype=\"ev3\" xmlversion=\"2.0\" description=\"\" tags=\"\"><instance x=\"213\" y=\"213\"><block type=\"robBrick_EV3-Brick\" id=\"1\" " +
                "intask=\"true\" deletable=\"false\"><comment w=\"0\" h=\"0\" pinned=\"false\">xmlTextToExport</comment><value name=\"S1\"><block type=\"robBrick_touch\" id=\"2\" " +
                "intask=\"true\"></block></value></block></instance></block_set></config></export>"
            ,
            xmlText);
    }

    /**
     * "pid" tries to export all of his programs while logged out<br>
     * <b>INVARIANT:</b> user "pid" exists <br>
     * <b>PRE:</b> "pid" has group and owned programs <br>
     * <b>POST:</b> "pid" is logged out
     * <ul>
     * <li>"pid" logs out and tries to export -> nothing happens because he needs to be logged in
     * </ul>
     */
    @Test
    public void exportAllLoggedOutTest() throws Exception {
        //pid logs out and tries to export
        restUser(this.sPid, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Response rePid = restExportAll(this.sPid);

        //check if zip exists
        Assert.assertNull(rePid);
    }

    /**
     * "pid" tries to export all of his programs<br>
     * <b>INVARIANT:</b>user "pid" exists and is logged in<br>
     * <b>PRE:</b> "pid" has group and owned programs<br>
     * <b>POST:</b> nothing changes
     * <ul>
     * <li>"pid" tries to exportAll
     * <li>check if his programs are in a zip entity with the right folder structure
     * </ul>
     */
    @Test
    public void groupAndOwnedProgramsTest() throws Exception {
        ZipInputStream zin2 = exportAllProgramsAsZipInputStream(this.sPid);

        //check directory structure
        //['p1','p4','gp1','gp2','gp3'] Are his ev3 Programs, gp means created by a group member
        //['p5','gp4'] are his calliope Programs
        List<String> programNames = new ArrayList<>();
        ZipEntry ze;
        while ( (ze = zin2.getNextEntry()) != null ) {
            programNames.add(ze.getName());
        }
        //this has an order due to it not coming from a HashMap
        Assert.assertEquals("MyPrograms/ev3/p1.xml", programNames.get(0));
        Assert.assertEquals("MyPrograms/ev3/p2.xml", programNames.get(1));
        Assert.assertEquals("MyPrograms/calliope/p5.xml", programNames.get(2));
        //Group Members are stored in a HashMap so no order
        Assert.assertTrue(programNames.contains("GroupPrograms/PidsGroup/ev3/Member1/gp1.xml"));
        Assert.assertTrue(programNames.contains("GroupPrograms/PidsGroup/ev3/Member1/gp2.xml"));
        Assert.assertTrue(programNames.contains("GroupPrograms/PidsGroup/calliope/Member1/gp4.xml"));
        Assert.assertTrue(programNames.contains("GroupPrograms/PidsGroup/ev3/Member2/gp3.xml"));
        Assert.assertEquals(7, programNames.size());
    }

    /**
     * "pid" tries to export while only having group programs:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <b>PRE:</b> "pid" has group and owned programs <br>
     * <b>POST:</b> "pid" has only group programs which are in a zip that was created with the right folder structure
     * <ul>
     * <li>delete all programs made by "pid"
     * <li>"pids" programs are exported and converted to a ZipInputStream
     * <li>test if the zip is in a minimal directory structure
     * </ul>
     */
    @Test
    public void onlyGroupProgramsTest() throws Exception {
        //Pid deletes all Programs made by him and not a group member
        Assert.assertEquals("ev3lejosv1", this.sPid.getRobotName());
        assertProgramListingAsExpected(this.sPid, "['p1','p2','gp1','gp2','gp3']");
        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p1';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p2';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restClient.setRobot(mkFRR(this.sPid.getInitToken(), "{'cmd':'setRobot'; 'robot':'calliope2017'}"));
        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p5';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + this.sPid.getUserId()));

        ZipInputStream zin = exportAllProgramsAsZipInputStream(this.sPid);

        //check zip entries it should not create the MyGroups Folder
        ZipEntry ze;
        while ( (ze = zin.getNextEntry()) != null ) {
            Assert.assertTrue(ze.getName().startsWith("PidsGroup/"));
        }

    }

    /**
     * a new account tries to export without having any programs<br>
     * <ul>
     * <li>a new user creates an account
     * <li>he tries to export all of his programs without saving any
     * <li>an empty zip is created
     * </ul>
     */
    @Test
    public void noProgramsExportTest() throws Exception {
        //create temporary user without any programs
        HttpSessionState tempMember = HttpSessionState.initOnlyLegalForDebugging("tempMember", robotPlugins, this.serverProperties, 5);
        restUser(
            tempMember,
            "{'cmd':'createUser';'accountName':'tempMember';'userName':'temp';'password':'12';'userEmail':'';'role':'STUDENT',"
                + " 'isYoungerThen14': true, 'language': 'de'}",
            "ok",
            Key.USER_CREATE_SUCCESS);
        restUser(tempMember, "{'cmd':'login';'accountName':'tempMember';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);

        ZipInputStream zin = exportAllProgramsAsZipInputStream(tempMember);
        //check if zip is empty
        assertNull(zin.getNextEntry());
    }

    //helper methods for this test

    /**
     * "pid" and "minscha" create programs:<br>
     * <b>PRE:</b> "pid" and "minscha" have no programs saved<br>
     * <b>POST:</b> "pid" has two ev3 programs and one calliope program with the default xml, minscha has one ev3 program with a changed xml
     * <ul>
     * <li>create two programs as "pid"
     * <li>change the robot to calliope2017
     * <li>create another program as "pid"
     * <li>change the robot back to ev3lejosv1
     * <li>save a program as "minscha" with a custom xml
     * </ul>
     */
    private void pidAndMinschaCreateSomePrograms() throws Exception {
        saveProgramAs(this.sPid, "pid", "pid", "p1", ".1.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sPid, "pid", "pid", "p2", ".2.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        restClient.setRobot(mkFRR(this.sPid.getInitToken(), "{'cmd':'setRobot'; 'robot':'calliope2017'}"));
        saveProgramAs(this.sPid, "pid", "pid", "p5", ".1.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        restClient.setRobot(mkFRR(this.sPid.getInitToken(), "{'cmd':'setRobot'; 'robot':'ev3lejosv1'}"));
        //Minscha changes config
        saveProgramAs(this.sMinscha, "minscha", "minscha", "p1", "progTextToExport", null, CONF_PRE + "xmlTextToExport" + CONF_POST, "ok", Key.PROGRAM_SAVE_SUCCESS);
    }

    private void groupSetup() throws Exception {
        //pid creates group
        restGroups(this.sPid, "{'cmd':'createUserGroup';'groupName':'PidsGroup';'groupMemberNames':['Member1','Member2'] }", "ok", Key.GROUP_CREATE_SUCCESS);

        restUser(
            this.groupMember1,
            "{'cmd':'login';'accountName':'PidsGroup:Member1';'password':'PidsGroup:Member1';'userGroupOwner':'pid';'userGroupName':'PidsGroup' }",
            "ok",
            Key.USER_GET_ONE_SUCCESS);

        restUser(
            this.groupMember2,
            "{'cmd':'login';'accountName':'PidsGroup:Member2';'password':'PidsGroup:Member2';'userGroupOwner':'pid';'userGroupName':'PidsGroup' }",
            "ok",
            Key.USER_GET_ONE_SUCCESS);

        //they save some programs
        saveProgramAs(this.groupMember1, "PidsGroup:Member1", "PidsGroup:Member1", "gp1", ".1.PidsGroup:Member1", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.groupMember1, "PidsGroup:Member1", "PidsGroup:Member1", "gp2", ".1.PidsGroup:Member1", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.groupMember2, "PidsGroup:Member2", "PidsGroup:Member2", "gp3", ".2.PidsGroup:Member2", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);

        //different robot
        restClient.setRobot(mkFRR(this.groupMember1.getInitToken(), "{'cmd':'setRobot'; 'robot':'calliope2017'}"));
        saveProgramAs(this.groupMember1, "PidsGroup:Member1", "PidsGroup:Member1", "gp4", ".1.PidsGroup:Member1", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
    }

    private String firstProgramOfZipToString(ZipInputStream uZis) throws IOException {
        ZipInputStream zis = uZis;
        if ( zis == null ) {
            return null;
        }
        StringBuilder resultString = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;
        //get the first program
        zis.getNextEntry();
        //read its contents
        while ( (read = zis.read(buffer, 0, 1024)) >= 0 ) {
            resultString.append(new String(buffer, 0, read));
        }
        return resultString.toString();
    }

    private ZipInputStream exportAllProgramsAsZipInputStream(HttpSessionState user) throws Exception {
        Response reUser = restExportAll(user);
        if ( reUser == null ) {
            return null;
        }
        //check if zip has contentType and name
        Assert.assertEquals("{Content-Type=[application/zip], Content-Disposition=[attachment; filename=\"NEPO_Programs.zip\"]}",
            reUser.getMetadata().toString());
        ByteArrayInputStream stream1 = (ByteArrayInputStream) reUser.getEntity();
        return new ZipInputStream(stream1);

    }

}