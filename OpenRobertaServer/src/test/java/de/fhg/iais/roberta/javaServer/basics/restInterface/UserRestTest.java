package de.fhg.iais.roberta.javaServer.basics.restInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.util.Key;

public class UserRestTest extends AbstractRestInterfaceTest {

    @Before
    public void init() throws Exception {
        setup();
    }

    @Test
    public void userWorkflowTest() throws Exception {
        createTwoUsers();
        activateUser();
        updateUser();
        changeUserPassword();
        loginLogoutPid();
    }

    /**
     * create two user:<br>
     * <b>PRE:</b> no user exists<br>
     * <b>POST:</b> two user exist, no user has logged in
     * <ul>
     * <li>USER table empty; create user "pid" -> success; USER table has 1 row
     * <li>create "pid" a second time -> error; USER table has 1 row
     * <li>create second user "minscha" -> success; USER table has 2 rows
     * </ul>
     */
    private void createTwoUsers() throws Exception {
        {
            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
            Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'pid';'userName':'cavy';'password':'dip';'userEmail':'cavy1@home.de';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
                "ok",
                Key.USER_ACTIVATION_SENT_MAIL_SUCCESS);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'pid';'userName':'secondTry';'password':'dip';'userEmail':'cavy2@home.de';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
                "error",
                Key.USER_CREATE_ERROR_NOT_SAVED_TO_DB);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'mailAlreadyUsed';'userName':'thirdTry';'password':'dip';'userEmail':'cavy1@home.de';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
                "error",
                Key.USER_ERROR_EMAIL_USED);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'minscha';'userName':'cavy';'password':'12';'userEmail':'';'role':'STUDENT', 'isYoungerThen14': true, 'language': 'de'}",
                "ok",
                Key.USER_CREATE_SUCCESS);
            Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        }
    }

    /**
     * activate user account:<br>
     * <b>PRE:</b> two user exists with no activated accounts<br>
     * <b>POST:</b> one user with activated account, no user has logged in
     * <ul>
     * </ul>
     */
    private void activateUser() throws Exception {
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PENDING_EMAIL_CONFIRMATIONS"));

        String url = this.memoryDbSetup.getOne("select URL_POSTFIX from PENDING_EMAIL_CONFIRMATIONS").toString();
        restUser(this.sPid, "{'cmd':'activateUser'; 'userActivationLink': '" + url + "';}", "ok", Key.USER_ACTIVATION_SUCCESS);
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PENDING_EMAIL_CONFIRMATIONS"));
    }

    /**
     * update user:<br>
     * <b>PRE:</b> two user exist, no user has logged in<br>
     * <b>POST:</b> two user exist, no user has logged in
     * <ul>
     * <li>USER table has 2 rows
     * <li>update of user "minscha" fails, because the user is not logged in
     * <li>login with "minscha" -> success
     * <li>update user name of "minscha" -> fail
     * <li>user "minscha" logs out -> success
     * </ul>
     */
    private void updateUser() throws Exception {

        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
        restUser(
            this.sMinscha,
            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home.de';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
            "error",
            Key.USER_ERROR_NOT_LOGGED_IN);

        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12' }", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());

        restUser(
            this.sMinscha,
            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home.de';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
            "ok",
            Key.USER_ACTIVATION_SENT_MAIL_SUCCESS);

        restUser(this.sMinscha, "{'cmd':'getUser'}", "ok", Key.USER_GET_ONE_SUCCESS);
        this.response.getEntity().toString().contains("cavy1231");
        restUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());

    }

    /**
     * change user password:<br>
     * <b>PRE:</b> two user exists, no user has logged in<br>
     * <b>POST:</b> two user exist, no user has logged in
     * <ul>
     * <li>password change of user "minscha" fails, because the user is not logged in
     * <li>user "minscha" logs in -> success
     * <li>password change of user "minscha" fails, because the old password supplied is wrong
     * <li>password change of user "minscha" with valid data -> success
     * <li>user "minscha" logs out -> success
     * <li>user "minscha" logs in with the new password -> success
     * <li>password change of user "minscha" with valid data (change the password to the old value) -> success
     * <li>user "minscha" logs out -> success
     * </ul>
     */
    private void changeUserPassword() throws Exception {

        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
        restUser(
            this.sMinscha,
            "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12';'newPassword':'12345'}",
            "error",
            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);

        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        restUser(
            this.sMinscha,
            "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'123';'newPassword':'12345'}",
            "error",
            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);
        restUser(this.sMinscha, "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12';'newPassword':'12345'}", "ok", Key.USER_UPDATE_SUCCESS);

        restUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());

        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12345'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        restUser(this.sMinscha, "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12345';'newPassword':'12'}", "ok", Key.USER_UPDATE_SUCCESS);

        restUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
    }

    /**
     * test login and logout<br>
     * <b>PRE:</b> two user exist, no user has logged in<br>
     * <b>POST:</b> two user exist, both user have logged in
     * <ul>
     * <li>login as "pid", use wrong password -> ERROR; pid-session tells that nobody is logged in
     * <li>login as "pid", use right password -> success; pid-session remembers the login
     * <li>login as "minscha" using the same session as for "pid" -> ERROR; pid-session has remembered the first login
     * <li>logout -> success; pid-session tells that nobody is logged in
     * <li>login as "pid", use right password -> success; pid-session remembers the login
     * <li>login as "minscha", use right password -> success; minscha-session remembers the login
     * </ul>
     */
    private void loginLogoutPid() throws Exception {
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'wrong'}", "error", Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "error", Key.COMMAND_INVALID); // because pid already has logged in
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'21'}", "error", Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
    }

}