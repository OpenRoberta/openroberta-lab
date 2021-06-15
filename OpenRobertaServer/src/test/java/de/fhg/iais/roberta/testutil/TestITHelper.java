package de.fhg.iais.roberta.testutil;

import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.javaServer.integrationTest.CompilerWorkflowRobotSpecificIT;
public class TestITHelper {
    private JSONObject jo;
    private Response rsp;

    @Test
    public void testCheckEntity() {
        jo = JSONUtilForServer.mk("{'rc':'ok','cause': '', 'server.version': '4.1.2', 'cmd': 'compileP', '_version': '1'}");
        rsp = Response.ok().entity(jo.toString()).build();
        Assert.assertNotNull(CompilerWorkflowRobotSpecificIT.checkEntityRc(rsp,"ok"));
        Assert.assertNull(CompilerWorkflowRobotSpecificIT.checkEntityRc(rsp,"error"));

        jo.put("rc","error").put("cause","ORA_PROGRAM_INVALID_STATEMETNS");
        rsp = Response.ok().entity(jo.toString()).build();
        Assert.assertNull(CompilerWorkflowRobotSpecificIT.checkEntityRc(rsp,"ok"));
        Assert.assertNotNull(CompilerWorkflowRobotSpecificIT.checkEntityRc(rsp,"error"));
        Assert.assertNotNull(CompilerWorkflowRobotSpecificIT.checkEntityRc(rsp,"ok", "ORA_PROGRAM_INVALID_STATEMETNS"));
    }
}
