package de.fhg.iais.roberta.javaServer.basics.restInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupDao;
import de.fhg.iais.roberta.util.Key;

public class GroupRestTest extends AbstractRestInterfaceTest {

    User userPid;
    User userMinscha;

    @Before
    public void init() throws Exception {
        setup();
        createUsers();
        getUsers();
    }

    @Test
    public void groupWorkFlowTest() throws Exception {
        groupCreationTests();
        pidAddsMembersToGroup();
        groupProgramTests();
        getUserGroupTests();
        setGroupMembersDefaultPasswordTests();
        updateMemberTest();
        deleteGroupMembersTest();
        deleteUserGroupsTest();
    }

    /**
     * tests about group Creation via REST calls:<br>
     * <b>INVARIANT:</b> users "pid" and "minscha" exist<br>
     * <b>PRE:</b> user "pid" has no groups and is logged out, "minscha" is logged out<br>
     * <b>POST:</b> "pid" and "minscha" are logged in <br>
     * "pid" has two groups: "PidsGroup" with two members and "PidsGroup2" without members
     */
    private void groupCreationTests() throws Exception {
        pidCreatesGroupLoggedOut();
        minschaTriesToCreateGroupUnvalidated();
        pidCreatesGroupWithMembers();
        pidCreatesGroupWithoutMember();
    }

    /**
     * add members to "pid"s group:<br>
     * <b>INVARIANT:</b> "pid" has two groups<br>
     * <b>PRE:</b> "pid"s groups have 2 and 0 members <br>
     * <b>POST:</b> "pid"s groups have  3 and 1 members
     * <ul>
     * <li>"pid" adds a member to the "PidsGroup" group --> success 3 members found
     * <li>"pid" adds a member to the "PidsGroup2" group --> success 1 member found
     * </ul>
     */
    private void pidAddsMembersToGroup() throws Exception {
        int pidId = this.sPid.getUserId();
        UserGroupDao groupDao;
        UserDao userDao = new UserDao(newDbSession());
        User pid = userDao.get(pidId);

        //Pid adds a member to his first group
        restGroups(this.sPid, "{'cmd':'addGroupMembers';'groupName':'PidsGroup';'groupMemberNames':['Member3'] }", "ok", Key.GROUP_ADD_MEMBER_SUCCESS);

        //Check if new member is inside the group
        groupDao = new UserGroupDao(newDbSession());
        List<User> members = new ArrayList<>(groupDao.load("PidsGroup", pid).getMembers());
        Assert.assertTrue(members.stream().anyMatch(p -> p.getAccount().equals("PidsGroup:Member3")));
        Assert.assertEquals(3, members.size());

        //Pid adds a member to his second group
        restGroups(this.sPid, "{'cmd':'addGroupMembers';'groupName':'PidsGroup2';'groupMemberNames':['Member1'] }", "ok", Key.GROUP_ADD_MEMBER_SUCCESS);
        //Check if new member is inside the group
        groupDao = new UserGroupDao(newDbSession());
        members = new ArrayList<>(groupDao.load("PidsGroup2", pid).getMembers());
        Assert.assertTrue(members.stream().anyMatch(p -> p.getAccount().equals("PidsGroup2:Member1")));
        Assert.assertEquals(1, members.size());

    }

    /**
     * tests about group programs and group member programs:<br>
     * <b>INVARIANT:</b> user "pid" exists and has a group with two members <br>
     * <b>PRE:</b> both group members have no programs and are logged out<br>
     * <b>POST:</b> a group program exists<br>
     * "Member1" has 3 programs 2 for ev3 and 1 for calliope<br>
     * "Member2" has 1 program for ev3<br>
     * both members are logged in
     */
    private void groupProgramTests() throws Exception {
        GroupMember1And2LoginAndSavePrograms();
        member1SavesCalliopeProgram();
        pidCreatesAGroupProgram();
    }

    /**
     * test workflows about the getUserGroup REST call<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     */
    private void getUserGroupTests() throws Exception {
        getUserGroupDefaultTest();
        getUserGroupListTest();
        getUserGroupListUnverifiedTest();
        getUserGroupNonExistentGroupTest();
    }

    /**
     * group members change their password and "pid" restores them to default and tries some edge cases:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in and has a group with 2 members<br>
     * <b>PRE:</b> both group members have the default password<br>
     * <ul>
     * <li>"Member1" and "Member2" change their password
     * <li>"pid" resets their passwords to the default value
     * <li>"pid" tries to change a password of a non existent user/group and while logged out and fails
     * <li>"pid tries to change the default password of a member with a default password
     * </ul>
     */
    private void setGroupMembersDefaultPasswordTests() throws Exception {
        groupMembersChangePasswords();
        pidResetsMembersPasswordsToDefault();
        changePasswordToDefaultOfNonExistentUsers();
        changePasswordToDefaultLoggedOut();
        changeDefaultPasswordToDefault();
    }

    /**
     * "tests the updateMemberAccount Rest call:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in and has a group with "Member1"<br>
     * <b>PRE:</b> a group with Member1 exists<br>
     * <b>POST:</b> the group has an additional member "UpdatedMember2"
     * <ul>
     * <li>test normal updateMemberAccount
     * <li>test updateMemberAccount to create new Accounts
     * <li>test edge cases that should fail
     * </ul>
     */
    private void updateMemberTest() throws Exception {
        updateMemberAccount();
        updateMemberNoCurrentGroupMember();
        updateMemberShouldFail();

    }

    /**
     * tests for the deleteGroupMembers REST call<br>
     * <b>INVARIANT:</b>user "pid" exists and is logged in<br>
     * <b>PRE:</b>"pid" has a group called "PidsGroup" with "Member1" and "Member2"<br>
     * <b>POST:</b> "PidsGroup" no longer has "Member1" and "Member2"
     * <ul>
     * <li> try deleting members while not being the owner
     * <li> try deleting "" as a member
     * <li> delete "Member1" and "Member2" from "PidsGroup"
     * <li> try to delete non-existent groupMembers
     * <li> try deleting group members from a non-existent group
     * <li> try deleting group members while logged out
     * </ul>
     */
    private void deleteGroupMembersTest() throws Exception {
        deletingGroupMembersWhileNotTheOwner();
        deletingGroupMemberNoNameGiven();
        deleteTwoGroupMembers();
        deleteNonExistentGroupMembers();
        deleteGroupMembersFromNonExistentGroup();
        deleteGroupMembersLoggedOut();

    }

    /**
     * "pid" deletes his groups<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <b>PRE:</b> "pid" has two groups with members<br>
     * <b>POST:</b> "pid" no longer has any groups
     * <ul>
     * <li>"pid" tries deleting a group that doesnt exist --> fails
     * <li>"pid" tries deleting without giving group names --> fails
     * <li>"minscha" tries deleting "pid"s groups --> fails
     * <li>"pid" deletes his groups
     * </ul>
     */
    private void deleteUserGroupsTest() throws Exception {
        deleteNonExistentUserGroup();
        deleteNothingAsGroup();
        deleteGroupAsNotTheOwner();
        deleteTwoGroups();
    }

    //group creation tests:

    /**
     * tries to create a group while logged out:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged out, and has no groups<br>
     * <ul>
     * <li> check if "pid" is logged out
     * <li>try to create a group as "pid" --> fails because he is not logged in
     */
    private void pidCreatesGroupLoggedOut() throws Exception {
        //pid tries creating a group without being logged in
        Assert.assertFalse(this.sPid.isUserLoggedIn());
        //error expected because pid is not logged in
        restGroups(
            this.sPid,
            "{'cmd':'createUserGroup';'groupName':'PidsGroup';'groupMemberNames':['Member1','Member2'] }",
            "error",
            Key.USER_ERROR_NOT_LOGGED_IN);
    }

    /**
     * tries to create a group without verified e-mail:<br>
     * <b>INVARIANT:</b> user "minscha" exists has no groups and no verified e-mail<br>
     * <b>PRE:</b> "minscha" is logged out<br>
     * <b>POST:</b> "minscha" is logged in
     * <ul>
     * <li>"minscha" is logged in
     * <li>attempt to createa a group as "minscha" --> fail because his e-mail is not verified
     */
    private void minschaTriesToCreateGroupUnvalidated() throws Exception {
        //Minscha logs in
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        //Minscha tries to create a group without having an activated e-mail this should fail
        restGroups(
            this.sMinscha,
            "{'cmd':'createUserGroup';'groupName':'MinschasGroup';'groupMemberNames':['Member1','Member2'] }",
            "error",
            Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER);
    }

    /**
     * creates new Group for "pid":<br>
     * <b>INVARIANT:</b> user "pid" exists<br>
     * <b>PRE:</b> "pid" has no groups and is logged out<br>
     * <b>POST:</b> "pid" has one group and is logged in
     * <ul>
     * <li>login "pid"
     * <li>check if "pid" has groups already
     * <li>create a group with two initial members as "pid"
     * <li>check if the group has been created successfully
     * </ul>
     */
    private void pidCreatesGroupWithMembers() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);

        UserGroupDao groupDao = new UserGroupDao(newDbSession());
        //pid should not have a group yet
        Assert.assertEquals(0, groupDao.getNumberOfGroupsOfOwner(userPid));

        //Pid creates a group with 2 members
        restGroups(this.sPid, "{'cmd':'createUserGroup';'groupName':'PidsGroup';'groupMemberNames':['Member1','Member2'] }", "ok", Key.GROUP_CREATE_SUCCESS);
        Assert.assertEquals(1, groupDao.getNumberOfGroupsOfOwner(userPid));
        Assert.assertEquals("pid", groupDao.load("PidsGroup", userPid).getOwner().getAccount());
        Assert.assertEquals("PidsGroup", groupDao.load("PidsGroup", userPid).getName());

        //check if group has two members
        List<User> members = new ArrayList<>(groupDao.load("PidsGroup", userPid).getMembers());

        Assert.assertTrue(members.stream().anyMatch(p -> p.getAccount().equals("PidsGroup:Member1")));
        Assert.assertTrue(members.stream().anyMatch(p -> p.getAccount().equals("PidsGroup:Member2")));
        Assert.assertEquals(2, members.size());
    }

    /**
     * creates a new Group for "pid":<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <b>PRE:</b> "pid" has one group"<br>
     * <b>POST:</b> "pid" has two groups one without users
     * <ul>
     * <li>create an empty group as "pid"
     * <li>check if group was created successfully
     * </ul>
     */
    private void pidCreatesGroupWithoutMember() throws Exception {
        UserGroupDao groupDao = new UserGroupDao(newDbSession());
        //Pid creates a group with no members
        restGroups(this.sPid, "{'cmd':'createUserGroup';'groupName':'PidsGroup2';'groupMemberNames':'' }", "ok", Key.GROUP_CREATE_SUCCESS);

        Assert.assertEquals(2, groupDao.getNumberOfGroupsOfOwner(userPid));
        Assert.assertEquals("pid", groupDao.load("PidsGroup", userPid).getOwner().getAccount());
        Assert.assertEquals("PidsGroup2", groupDao.load("PidsGroup2", userPid).getName());

        List<User> members = new ArrayList<>(groupDao.load("PidsGroup2", userPid).getMembers());
        Assert.assertEquals(0, members.size());
    }

    // group program tests:

    /**
     * Group members login and save 3 programs:<br>
     * <b>INVARIANT:</b> "pid" has a group with two members<br>
     * <b>PRE:</b> both group members have no programs saved and are logged out<br>
     * <b>POST:</b> "Member1" has 2 programs for ev3, "Member2" has 1 program for ev3 all of which are shared to "pid" both users are logged in
     * <ul>
     * <li>"Member1" logs in
     * <li>"Member2" logs in
     * <li>"Member1" saves 2 ev3 programs
     * <li>"Member2" saves 1 ev3 program
     * <li>check if programs were created successfully
     * </ul>
     */
    private void GroupMember1And2LoginAndSavePrograms() throws Exception {
        //Group Members login
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
        Assert.assertTrue(this.groupMember1.isUserLoggedIn() && this.groupMember2.isUserLoggedIn());

        //Member1 saves ev3 Programs
        saveProgramAs(this.groupMember1, "PidsGroup:Member1", "PidsGroup:Member1", "gp1", ".1.PidsGroup:Member1", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.groupMember1, "PidsGroup:Member1", "PidsGroup:Member1", "gp2", ".1.PidsGroup:Member1", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        assertProgramListingAsExpected(this.groupMember1, "['gp1','gp2']");
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        //Member2 saves ev3 Program
        saveProgramAs(this.groupMember2, "PidsGroup:Member2", "PidsGroup:Member2", "gp3", ".2.PidsGroup:Member2", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        assertProgramListingAsExpected(this.groupMember2, "['gp3']");
        Assert.assertEquals(3, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        //check Pids programs
        assertProgramListingAsExpected(this.sPid, "['gp1','gp2','gp3']");
    }

    /**
     * "Member1" saves a calliope program:<br>
     * <b>INVARIANT:</b> "pid" has a group with "Member1" logged in<br>
     * <b>PRE:</b> "Member1" has 2 programs for ev3<br>
     * <b>POST:</b> "Member1" has 3 programs 2 for ev3 and 1 for calliope
     * <ul>
     * <li>change robot of "Member1" and "pid" to calliope
     * <li>"Member1" saves 1 calliope program
     * <li>check if program was created successfully
     * <li>change robot back to ev3
     * </ul>
     */
    private void member1SavesCalliopeProgram() throws Exception {
        restClient.setRobot(mkFRR(this.groupMember1.getInitToken(), "{'cmd':'setRobot'; 'robot':'calliope2017'}"));
        restClient.setRobot(mkFRR(this.sPid.getInitToken(), "{'cmd':'setRobot'; 'robot':'calliope2017'}"));
        Assert.assertEquals("calliope2017", groupMember1.getRobotName());

        //Member1 saves calliope program
        saveProgramAs(this.groupMember1, "PidsGroup:Member1", "PidsGroup:Member1", "gp4", ".1.PidsGroup:Member1", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        assertProgramListingAsExpected(this.groupMember1, "['gp4']");
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        //check Pids programs
        assertProgramListingAsExpected(this.sPid, "['gp4']");

        restClient.setRobot(mkFRR(this.sPid.getInitToken(), "{'cmd':'setRobot'; 'robot':'ev3lejosv1'}"));
        restClient.setRobot(mkFRR(this.groupMember1.getInitToken(), "{'cmd':'setRobot'; 'robot':'ev3lejosv1'}"));


    }

    /**
     * Pid creates a groupProgram:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <b>PRE:</b> no program exists that's shared with the group<br>
     * <b>POST:</b> one program exists that's shared with the group
     * <ul>
     * <li>"pid" creates a program
     * <li>"pid" shares the program with his group
     * <li>check if group members see the program
     * </ul>
     */
    private void pidCreatesAGroupProgram() throws Exception {
        //pid creates a Program
        saveProgramAs(this.sPid, "pid", "pid", "p1", ".1.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        //and shares it with the group
        String shareWrite = "{'type':'UserGroup';'label':'PidsGroup';'right':'WRITE'}";
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p1';'shareData':" + shareWrite + "}", "ok", Key.ACCESS_RIGHT_CHANGED);

        //check if every group member now has that program
        assertProgramListingAsExpected(this.groupMember1, "['gp1','gp2','p1']");
        assertProgramListingAsExpected(this.groupMember2, "['gp3','p1']");
        assertProgramListingAsExpected(this.sPid, "['gp1','gp2','gp3','p1']");
        Assert.assertEquals(5, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

    }

    //getUserGroup tests

    /**
     * Pid tries to get information about his group:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <ul>
     * <li>"pid" calls getUserGroup
     * <li>"pid" checks if the information about programs returned is correct
     * </ul>
     */
    private void getUserGroupDefaultTest() throws Exception {

        restGroups(this.sPid, "{'cmd':'getUserGroup';'groupName':'PidsGroup'}", "ok", Key.GROUP_GET_ONE_SUCCESS);

        //transform response into JSONObject with relevant information only
        JSONObject entity = new JSONObject((String) this.response.getEntity());
        JSONObject groupInfo = entity.getJSONObject("userGroup");

        checkProgramsOfGetGroup(groupInfo);
        checkMembersOfGetGroup(groupInfo);
    }

    /**
     * Pid tries to get information about his groups programs:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <ul>
     * <li>"pid" checks if the information about programs returned is correct
     * </ul>
     */
    private void checkProgramsOfGetGroup(JSONObject groupInfo) throws Exception {
        Assert.assertEquals("pid", groupInfo.getString("owner"));
        Assert.assertEquals("PidsGroup", groupInfo.getString("name"));

        //test if programs are returned correctly
        JSONObject program = groupInfo.getJSONArray("programs").getJSONObject(0);
        Assert.assertEquals("pid", program.getString("owner"));
        Assert.assertEquals("ev3", program.getString("robot"));
        Assert.assertEquals("pid", program.getString("author"));
        Assert.assertEquals("p1", program.getString("name"));
        Assert.assertEquals("WRITE", program.getString("right"));
    }

    /**
     * Pid tries to get information about his groups members:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <ul>
     * <li>"pid" checks if the information about members returned is correct
     * </ul>
     */
    private void checkMembersOfGetGroup(JSONObject groupInfo) throws Exception {
        //test if members are returned correctly
        JSONArray members = groupInfo.getJSONArray("members");
        //transfer to List
        List<JSONObject> member = IntStream.range(0, members.length()).mapToObj(members::getJSONObject).collect(Collectors.toList());

        //check amount of members
        Assert.assertEquals(3, member.size());
        //check names
        Assert.assertTrue(member.stream().anyMatch(p -> p.getString("account").equals("PidsGroup:Member1")));
        Assert.assertTrue(member.stream().anyMatch(p -> p.getString("account").equals("PidsGroup:Member2")));
        Assert.assertTrue(member.stream().anyMatch(p -> p.getString("account").equals("PidsGroup:Member3")));
    }

    /**
     * "Pid" tries to get information about all of his groups<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <ul>
     * <li>"pid" calls getUserGroupList
     * <li>check if the information returned is correct
     * </ul>
     */
    private void getUserGroupListTest() throws Exception {
        //get Pids groups and put them into group1 and group2
        restGroups(this.sPid, "{'cmd' : 'getUserGroupList'}", "ok", Key.GROUP_GET_ALL_SUCCESS);
        JSONObject entity = new JSONObject((String) this.response.getEntity());
        JSONArray groups = entity.getJSONArray("userGroups");

        JSONObject group1 = groups.getJSONObject(0);
        JSONObject group2 = groups.getJSONObject(1);

        //check if group names are correct
        Assert.assertEquals("PidsGroup", group1.getString("name"));
        Assert.assertEquals("PidsGroup2", group2.getString("name"));
        //check if owner is correct
        Assert.assertEquals("pid", group1.getString("owner"));
        Assert.assertEquals("pid", group2.getString("owner"));
    }

    /**
     * "Minscha" and a group member try to get information about their groups even though they can't own any:<br>
     * <b>INVARIANT:</b> user "minscha" exists and is logged in<br>
     * <ul>
     * <li>"Minscha" calls getUserGroupList --> fails because he has no rights to be group owner
     * <li>"Member1" calls getUserGroupList --> fails because he has no rights to be group owner
     * </ul>
     */
    private void getUserGroupListUnverifiedTest() throws Exception {
        //get Minschas groups should fail because Minscha can't own groups
        restGroups(this.sMinscha, "{'cmd' : 'getUserGroupList'}", "error", Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER);
        //get a GroupMembers GroupList should fail because the can't own groups
        restGroups(this.groupMember1, "{'cmd' : 'getUserGroupList'}", "error", Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER);
    }

    /**
     * Pid tries to get information about a non existent group:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <li>calls getUserGroup as "pid" on an non existent group name --> should fail
     */
    private void getUserGroupNonExistentGroupTest() throws Exception {
        restGroups(this.sPid, "{'cmd':'getUserGroup';'groupName':'DoesntExist'}", "error", Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
    }

    //group member password tests

    /**
     * group members change their passwords:<br>
     * <b>INVARIANT:</b> "pid" has a group with 2 members which are both logged in<br>
     * <b>PRE:</b> both group members have the default password<br>
     * <b>POST:</b> both group members do not have the default password
     * <ul>
     * <li>"Member1" and "Member2" change their password
     * <li>check if member1 password is changed
     * <li>check if member1 now does not have the default password
     * </ul>
     */
    private void groupMembersChangePasswords() throws Exception {
        Assert.assertTrue(this.groupMember1.isUserLoggedIn() && this.groupMember2.isUserLoggedIn());
        //first change  group members Passwords:
        restUser(
            this.groupMember1,
            "{'cmd':'changePassword';'accountName':'PidsGroup:Member1';'oldPassword':'PidsGroup:Member1';'newPassword':'12345'}",
            "ok",
            Key.USER_UPDATE_SUCCESS);
        restUser(
            this.groupMember2,
            "{'cmd':'changePassword';'accountName':'PidsGroup:Member2';'oldPassword':'PidsGroup:Member2';'newPassword':'123'}",
            "ok",
            Key.USER_UPDATE_SUCCESS);
        //check if it worked:
        restUser(this.groupMember1, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        restUser(
            this.groupMember1,
            "{'cmd':'login';'accountName':'PidsGroup:Member1';'password':'12345';'userGroupOwner':'pid';'userGroupName':'PidsGroup' }",
            "ok",
            Key.USER_GET_ONE_SUCCESS);

        //check if hasDefaultPassword boolean changed
        restGroups(this.sPid, "{'cmd':'getUserGroup';'groupName':'PidsGroup'}", "ok", Key.GROUP_GET_ONE_SUCCESS);
        JSONObject groupInfo = new JSONObject((String) this.response.getEntity()).getJSONObject("userGroup");
        JSONArray members = groupInfo.getJSONArray("members");
        List<JSONObject> member = IntStream.range(0, members.length()).mapToObj(members::getJSONObject).collect(Collectors.toList());

        Assert.assertTrue(member.stream().anyMatch(p -> p.getString("account").equals("PidsGroup:Member1") && !p.getBoolean("hasDefaultPassword")));
    }

    /**
     * "pid" resets the passwords of both group members to default<br>
     * <b>INVARIANT:</b> "pid" has a group with 2 members which are both logged in<br>
     * <b>PRE:</b> both group members do not have the default password<br>
     * <b>POST:</b> both group members have the default password
     * <ul>
     * <li>"pid" sets members password to the default password
     * <li>check if all members now have the default password
     * </ul>
     */
    private void pidResetsMembersPasswordsToDefault() throws Exception {
        restGroups(
            this.sPid,
            "{'cmd': 'setUserGroupMemberDefaultPasswords'; 'groupName':'PidsGroup'; 'groupMemberAccounts':['PidsGroup:Member1','PidsGroup:Member2']}",
            "ok",
            Key.GROUP_GET_ONE_SUCCESS);

        //check if it worked:
        restUser(this.groupMember1, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        restUser(
            this.groupMember1,
            "{'cmd':'login';'accountName':'PidsGroup:Member1';'password':'PidsGroup:Member1';'userGroupOwner':'pid';'userGroupName':'PidsGroup' }",
            "ok",
            Key.USER_GET_ONE_SUCCESS);

        restUser(this.groupMember2, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        restUser(
            this.groupMember2,
            "{'cmd':'login';'accountName':'PidsGroup:Member2';'password':'PidsGroup:Member2';'userGroupOwner':'pid';'userGroupName':'PidsGroup' }",
            "ok",
            Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(this.groupMember1.isUserLoggedIn() && this.groupMember2.isUserLoggedIn());

        //check if groupInfo is now changed to default
        restGroups(this.sPid, "{'cmd':'getUserGroup';'groupName':'PidsGroup'}", "ok", Key.GROUP_GET_ONE_SUCCESS);

        JSONObject groupInfo = new JSONObject((String) this.response.getEntity()).getJSONObject("userGroup");
        JSONArray members2 = groupInfo.getJSONArray("members");
        List<JSONObject> member = IntStream.range(0, members2.length()).mapToObj(members2::getJSONObject).collect(Collectors.toList());
        for ( JSONObject jsonObject : member ) {
            Assert.assertTrue(jsonObject.getBoolean("hasDefaultPassword"));
        }
    }

    /**
     * attempt to change passwords of non-existent users to default<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <ul>
     * <li>"pid" tries to change the password of a member that does not exist --> fails
     * <li>"pid" tries to change the password of a member in a group that does not exist --> fails
     * </ul>
     */
    private void changePasswordToDefaultOfNonExistentUsers() throws Exception {
        restGroups(
            this.sPid,
            "{'cmd': 'setUserGroupMemberDefaultPasswords'; 'groupName':'PidsGroup'; 'groupMemberAccounts':['PidsGroup:NOT','PidsGroup:AUSER']}",
            "ok",
            Key.GROUP_GET_ONE_SUCCESS);

        //try changing the password in a non-existent group
        restGroups(
            this.sPid,
            "{'cmd': 'setUserGroupMemberDefaultPasswords'; 'groupName':'DoesntExist'; 'groupMemberAccounts':['PidsGroup:NOT','PidsGroup:AUSER']}",
            "error",
            Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
    }

    /**
     * attempt to change passwords of a user with the default password to default<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <ul>
     * <li>"pid" tries to change passwords of a user with a default password --> nothing changes
     * </ul>
     */
    private void changeDefaultPasswordToDefault() throws Exception {
        restGroups(
            this.sPid,
            "{'cmd': 'setUserGroupMemberDefaultPasswords'; 'groupName':'PidsGroup'; 'groupMemberAccounts':['PidsGroup:Member1','PidsGroup:Member2']}",
            "ok",
            Key.GROUP_GET_ONE_SUCCESS);
    }

    /**
     * attempt to change passwords of a user while logged out<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in<br>
     * <ul>
     * <li>"pid" logs out
     * <li>"pid" tries to change passwords to default --> fails
     * <li>"pid" pid logs back in
     * </ul>
     */
    private void changePasswordToDefaultLoggedOut() throws Exception {
        restUser(this.sPid, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        restGroups(
            this.sPid,
            "{'cmd': 'setUserGroupMemberDefaultPasswords'; 'groupName':'PidsGroup'; 'groupMemberAccounts':['PidsGroup:Member1','PidsGroup:Member2']}",
            "error",
            Key.USER_ERROR_NOT_LOGGED_IN);
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
    }

    //update members account test

    /**
     * "pid" updates a group members account:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in and has a group with "Member1"<br>
     * <ul>
     * <li>"pid" changes "Member1"s account to "UpdatedMember1"
     * <li>check if member got updated correctly
     * <li>"pid" changes the account of "UpdatedMember1" back to "Member1"
     * </ul>
     */
    private void updateMemberAccount() throws Exception {
        restGroups(
            this.sPid,
            "{ 'cmd':'updateMemberAccount';'groupName':'PidsGroup';'currentGroupMemberAccount':'PidsGroup:Member1';'newGroupMemberAccount':'UpdatedMember1' }",
            "ok",
            Key.SERVER_SUCCESS);

        //check if groupInfo is now changed
        restGroups(this.sPid, "{'cmd':'getUserGroup';'groupName':'PidsGroup'}", "ok", Key.GROUP_GET_ONE_SUCCESS);

        JSONObject groupInfo = new JSONObject((String) this.response.getEntity()).getJSONObject("userGroup");
        JSONArray members = groupInfo.getJSONArray("members");
        List<JSONObject> member = IntStream.range(0, members.length()).mapToObj(members::getJSONObject).collect(Collectors.toList());

        //check if Member1 is now replaced with UpdatedMember1
        Assert.assertFalse(member.stream().anyMatch(p -> p.getString("account").equals("PidsGroup:Member1")));
        Assert.assertTrue(member.stream().anyMatch(p -> p.getString("account").equals("PidsGroup:UpdatedMember1")));

        //changing groupMember1 name back
        restGroups(
            this.sPid,
            "{ 'cmd':'updateMemberAccount';'groupName':'PidsGroup';'currentGroupMemberAccount':'PidsGroup:UpdatedMember1';'newGroupMemberAccount':'Member1' }",
            "ok",
            Key.SERVER_SUCCESS);
    }

    /**
     * <br>
     * "pid" updates non existent group members to create new ones:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in and has a group<br>
     * <b>PRE:</b> a group with Member1 exists<br>
     * <b>POST:</b> the group has an additional member "UpdatedMember2"
     * <ul>
     * <li>try to create a new Group member with updateMemberAccount by not giving a currentGroupMemberAccount
     * <li>try to create a new Group member with the same name as an already existing member --> fails
     * </ul>
     */
    private void updateMemberNoCurrentGroupMember() throws Exception {
        updateMemberCreateNewMember();
        updateMemberCreateAlreadyExisting();
    }

    private void updateMemberCreateAlreadyExisting() throws Exception {
        restGroups(
            this.sPid,
            "{ 'cmd':'updateMemberAccount';'groupName':'PidsGroup';'currentGroupMemberAccount':'';'newGroupMemberAccount':'Member1' }",
            "error",
            Key.GROUP_MEMBER_ERROR_ALREADY_EXISTS);
    }

    private void updateMemberCreateNewMember() throws Exception {
        restGroups(
            this.sPid,
            "{ 'cmd':'updateMemberAccount';'groupName':'PidsGroup';'currentGroupMemberAccount':'';'newGroupMemberAccount':'UpdatedMember2' }",
            "ok",
            Key.GROUP_ADD_MEMBER_SUCCESS);

        UserGroupDao groupDao = new UserGroupDao(newDbSession());
        List<User> groupUsers = new ArrayList<>(groupDao.load("PidsGroup", userPid).getMembers());
        Assert.assertTrue(groupUsers.stream().anyMatch(p -> p.getAccount().equals("PidsGroup:UpdatedMember2")));
    }

    /**
     * <br>
     * update Edge cases that should fail:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in and has a group<br>
     * <ul>
     * <li>try to update without giving a new account --> fails
     * <li>try to update a non-existent account --> fails
     * <li>try to update a non-existent group --> fails
     * <li>try to update while not being the group owner --> fails
     * </ul>
     */
    private void updateMemberShouldFail() throws Exception {
        updateMemberNoNewGroupMemberAccount();
        updateMemberNonExistentAccount();
        updateMemberInNonExistentGroup();
        updateMemberNotTheGroupOwner();
    }

    private void updateMemberNoNewGroupMemberAccount() throws Exception {

        restGroups(
            this.sPid,
            "{ 'cmd':'updateMemberAccount';'groupName':'PidsGroup';'currentGroupMemberAccount':'PidsGroup:UpdatedMember2';'newGroupMemberAccount':'' }",
            "error",
            Key.SERVER_ERROR);

        restGroups(this.sPid, "{'cmd':'getUserGroup';'groupName':'PidsGroup'}", "ok", Key.GROUP_GET_ONE_SUCCESS);
        JSONObject groupInfo2 = new JSONObject((String) this.response.getEntity()).getJSONObject("userGroup");
        JSONArray members2 = groupInfo2.getJSONArray("members");
        List<JSONObject> member2 = IntStream.range(0, members2.length()).mapToObj(members2::getJSONObject).collect(Collectors.toList());

        Assert.assertFalse(member2.stream().anyMatch(p -> p.getString("account").equals("PidsGroup:")));
        Assert.assertEquals(4, member2.size());
    }

    private void updateMemberNonExistentAccount() throws Exception {
        restGroups(
            this.sPid,
            "{ 'cmd':'updateMemberAccount';'groupName':'PidsGroup';'currentGroupMemberAccount':'PidsGroup:NonExistent';'newGroupMemberAccount':'UpdatedNonExistent' }",
            "error",
            Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
    }

    private void updateMemberInNonExistentGroup() throws Exception {
        restGroups(
            this.sPid,
            "{ 'cmd':'updateMemberAccount';'groupName':'doesntExist';'currentGroupMemberAccount':'PidsGroup:';'newGroupMemberAccount':'newMember' }",
            "error",
            Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
    }

    private void updateMemberNotTheGroupOwner() throws Exception {
        restGroups(
            this.groupMember1,
            "{'cmd': 'updateMemberAccount'; 'groupName':'PidsGroup'; 'currentGroupMemberAccount':'PidsGroup:Member1';'newGroupMemberAccount':'UpdatedMember1' }",
            "error",
            Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER);
    }
    //delete group members test

    private void deletingGroupMembersWhileNotTheOwner() throws Exception {
        restGroups(
            this.groupMember1,
            "{'cmd': 'deleteGroupMembers'; 'groupName':'PidsGroup'; 'groupMemberAccounts':['PidsGroup:Member1','PidsGroup:Member2']}",
            "error",
            Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER);
    }

    private void deletingGroupMemberNoNameGiven() throws Exception {
        UserGroupDao groupDao = new UserGroupDao(newDbSession());

        //try deleting with no group member given
        restGroups(this.sPid, "{'cmd': 'deleteGroupMembers'; 'groupName':'PidsGroup'; 'groupMemberAccounts':''}", "ok", Key.GROUP_GET_ONE_SUCCESS);
        Assert.assertNotNull(this.memoryDbSetup.getOne("select ACCOUNT from USER where ACCOUNT = 'PidsGroup:Member1'"));
        List<User> members = new ArrayList<>(groupDao.load("PidsGroup", userPid).getMembers());
        Assert.assertEquals(4, members.size());
    }

    /**
     * "pid" deletes group members from his group:<br>
     * <b>INVARIANT:</b> user "pid" exists and is logged in and has a group<br>
     * <b>PRE:</b> a group with "Member1" and "Member2" exists<br>
     * <b>POST:</b> "PidsGroup" no longer has "Member1" and "Member2"
     * <li>delete members
     * <li>check if members still exist
     * </ul>
     */
    private void deleteTwoGroupMembers() throws Exception {
        restGroups(
            this.sPid,
            "{'cmd': 'deleteGroupMembers'; 'groupName':'PidsGroup'; 'groupMemberAccounts':['PidsGroup:Member1','PidsGroup:Member2']}",
            "ok",
            Key.GROUP_GET_ONE_SUCCESS);

        //check if size changed and if deleted Group members can not be found
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER where ACCOUNT = 'PidsGroup:Member1'"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER where ACCOUNT = 'PidsGroup:Member2'"));
        UserGroupDao groupDao = new UserGroupDao(newDbSession());
        List<User> members = new ArrayList<>(groupDao.load("PidsGroup", userPid).getMembers());
        Assert.assertEquals(2, members.size());
    }

    private void deleteNonExistentGroupMembers() throws Exception {
        restGroups(
            this.sPid,
            "{'cmd': 'deleteGroupMembers'; 'groupName':'PidsGroup'; 'groupMemberAccounts':['doesntExist','doesntExistAlso']}",
            "ok",
            Key.GROUP_GET_ONE_SUCCESS);
    }

    private void deleteGroupMembersFromNonExistentGroup() throws Exception {
        restGroups(
            this.sPid,
            "{'cmd': 'deleteGroupMembers'; 'groupName':'doesntExist'; 'groupMemberAccounts':['PidsGroup:Member1','PidsGroup:Member2']}",
            "error",
            Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
    }

    private void deleteGroupMembersLoggedOut() throws Exception {
        restUser(this.sPid, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);

        restGroups(
            this.sPid,
            "{'cmd': 'deleteGroupMembers'; 'groupName':'doesntExist'; 'groupMemberAccounts':['PidsGroup:Member1','PidsGroup:Member2']}",
            "error",
            Key.USER_ERROR_NOT_LOGGED_IN);

        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
    }

    //deleteGroup tests

    private void deleteNonExistentUserGroup() throws Exception {
        restGroups(this.sPid, "{'cmd': 'deleteUserGroups'; 'groupNames':['DoesntExist','DoesntExistAlso']}", "error", Key.GROUP_DELETE_ERROR_GROUP_DOES_NOT_EXISTS);
        UserGroupDao groupDao = new UserGroupDao(newDbSession());
        Assert.assertEquals(2, groupDao.getNumberOfGroupsOfOwner(userPid));
    }

    private void deleteNothingAsGroup() throws Exception {
        restGroups(this.sPid, "{'cmd': 'deleteUserGroups'; 'groupNames':''}", "error", Key.GROUP_DELETE_ERROR);
    }

    private void deleteGroupAsNotTheOwner() throws Exception {
        restGroups(this.sMinscha, "{'cmd': 'deleteUserGroups'; 'groupNames':['PidsGroup','PidsGroup2']}", "error", Key.GROUP_DELETE_ERROR_GROUP_DOES_NOT_EXISTS);
        UserGroupDao groupDao = new UserGroupDao(newDbSession());
        Assert.assertEquals(2, groupDao.getNumberOfGroupsOfOwner(userPid));
    }

    private void deleteTwoGroups() throws Exception {
        UserGroupDao groupDao = new UserGroupDao(newDbSession());

        //delete pids groups
        Assert.assertNotNull(this.memoryDbSetup.getOne("select NAME from USERGROUP where NAME = 'PidsGroup'"));
        Assert.assertNotNull(this.memoryDbSetup.getOne("select NAME from USERGROUP where NAME = 'PidsGroup2'"));

        restGroups(this.sPid, "{'cmd': 'deleteUserGroups'; 'groupNames':['PidsGroup','PidsGroup2']}", "ok", Key.GROUP_DELETE_SUCCESS);
        Assert.assertEquals(0, groupDao.getNumberOfGroupsOfOwner(userPid));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USERGROUP where NAME = 'PidsGroup'"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USERGROUP where NAME = 'PidsGroup2'"));
    }

    //helper:

    /**
     * gets "pid" and "minscha" as UserObjects:<br>
     * <b>INVARIANT:</b> user "pid" and "minscha" exist and are logged out<br>
     * <ul>
     * <li>login users
     * <li>get User id of "pid" and "minscha"
     * <li>get User object of those ids
     * <li>logout "pid" and "minscha"
     */
    private void getUsers() throws Exception {
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
        UserDao userDao = new UserDao(newDbSession());

        int pidId = this.sPid.getUserId();
        userPid = userDao.get(pidId);

        int minschaId = this.sMinscha.getUserId();
        userMinscha = userDao.get(minschaId);

        restUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        restUser(this.sPid, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
    }

}