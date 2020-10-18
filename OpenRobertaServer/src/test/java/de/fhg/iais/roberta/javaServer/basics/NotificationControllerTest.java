package de.fhg.iais.roberta.javaServer.basics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.NotificationsResponse;
import de.fhg.iais.roberta.generated.restEntities.PingResponse;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientPing;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.NotificationController;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.NotificationService;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.UtilForREST;

@RunWith(MockitoJUnitRunner.class)
public class NotificationControllerTest {
    public static final int ADMIN_USER_ID = 1;
    public static final int NOT_ADMIN_USER_ID = 2;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private ServerProperties serverProperties;

    @Mock
    private NotificationService notificationService;

    private NotificationController notificationController;

    @Before
    public void setUp() {
        notificationController = new NotificationController(notificationService);
        UtilForREST.setNotificationService(notificationService);
        mockGetCurrentDigest("HASH");
    }

    @Test
    public void testGetNotifications() {
        JSONObject firstNotification = new JSONObject("{\n" + "  \"triggers\": [],\n" + "  \"name\": \"notification1\"\n" + "}");
        JSONObject secondNotification = new JSONObject("{\n" + "  \"conditions\": [" + "],\n" + "  \"name\": \"notification2\"\n" + "}");

        mockGetNotifications(firstNotification, secondNotification);
        mockGetCurrentDigest("HASH");

        HttpSessionState httpSession = createEmptyHttpSession();
        FullRestRequest fullRestRequest = createFullRequestFromSession(httpSession);

        Response response = notificationController.getNotifications(fullRestRequest);
        NotificationsResponse notificationsResponse = parseNotificationResponse(response);
        List<JSONObject> notificationList = parseNotificationObjects(notificationsResponse.getNotifications());

        assertThat(httpSession.getReceivedNotificationsDigest()).isEqualTo("HASH");
        assertThat(notificationList).hasSize(2).anySatisfy(json -> {
            assertThat(json.getString("name")).isEqualTo("notification1");
            assertThat(json.getJSONArray("triggers")).isEmpty();
        }).anySatisfy(json -> {
            assertThat(json.getString("name")).isEqualTo("notification2");
            assertThat(json.getJSONArray("conditions")).isEmpty();
        });

        assertThat(notificationsResponse.getNotificationsAvailable()).isEqualTo(false);
    }

    @Test
    public void testNotificationsAvailableFlag() throws Exception {
        ClientPing clientPing = new ClientPing("", null);

        HttpSessionState httpSession = createEmptyHttpSession();
        FullRestRequest fullRestRequest = createFullRequestFromSession(httpSession);

        mockGetCurrentDigest("HASH");
        mockGetNotifications();
        notificationController.getNotifications(fullRestRequest);

        BaseResponse firstBaseResponse = baseResponsePing(clientPing, fullRestRequest);
        assertThat(firstBaseResponse.getNotificationsAvailable()).isEqualTo(false);

        mockGetCurrentDigest("DIFFERENT_HASH");
        BaseResponse secondBaseResponse = baseResponsePing(clientPing, fullRestRequest);
        assertThat(secondBaseResponse.getNotificationsAvailable()).isEqualTo(true);
    }

    @Test
    public void testPostNotificationsNotAsAdmin() {
        JSONArray notifications = new JSONArray("[{\"triggers\": [] }]");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(NOT_ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, notifications.toString());

        Response response = notificationController.postNotifications(fullRestRequest);

        verify(notificationService, never()).saveNotifications(anyString());

        BaseResponse baseResponse = BaseResponse.makeFromString(((String) response.getEntity()));

        assertThat(baseResponse.getRc()).isEqualTo("error");
        assertThat(baseResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_ERROR_INVALID_PERMISSION");
        assertThat(baseResponse.getCause()).isEqualTo("ORA_NOTIFICATION_ERROR_INVALID_PERMISSION");
    }

    @Test
    public void testPostNotificationsAsAdminJSONError() {
        JSONArray notifications = new JSONArray("[\n" + "  {\n" + "    \"triggers\": []\n" + "  " + "}\n" + "]");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, notifications.toString());

        doThrow(new JSONException("A JSONArray text must start with '[' at 1 [character 2 line 1]")).when(notificationService).saveNotifications(anyString());

        Response response = notificationController.postNotifications(fullRestRequest);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificationService).saveNotifications(argumentCaptor.capture());

        String notificationsJson = argumentCaptor.getValue();
        assertThat(notificationsJson).isEqualTo(notifications.toString());

        BaseResponse baseResponse = BaseResponse.makeFromString(((String) response.getEntity()));
        assertThat(baseResponse.getRc()).isEqualTo("error");
        assertThat(baseResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_ERROR_INVALID_SYNTAX");
        assertThat(baseResponse.getParameters().getString("MESSAGE")).isEqualTo("A JSONArray text must start with '[' at 1 [character 2 line 1]");
    }

    @Test
    public void testPostNotificationsAsAdmin() {
        JSONArray notifications = new JSONArray("[\n" + "  {\n" + "    \"triggers\": []\n" + "  " + "}\n" + "]");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, notifications.toString());

        doAnswer(invocation -> new JSONArray(invocation.getArgumentAt(0, String.class))).when(notificationService).saveNotifications(anyString());

        Response response = notificationController.postNotifications(fullRestRequest);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(notificationService).saveNotifications(argumentCaptor.capture());

        String notificationsJson = argumentCaptor.getValue();
        assertThat(notificationsJson).isEqualTo(notifications.toString());

        BaseResponse baseResponse = BaseResponse.makeFromString(((String) response.getEntity()));
        assertThat(baseResponse.getRc()).isEqualTo("ok");
        assertThat(baseResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_SUCCESS");
    }

    private void mockGetNotifications(JSONObject... toBeReturned) {
        doReturn(new JSONArray(toBeReturned)).when(notificationService).getNotifications();
    }

    private void mockGetCurrentDigest(String digest) {
        when(notificationService.getCurrentDigest()).thenReturn(digest);
    }

    private BaseResponse baseResponsePing(ClientPing clientPing, FullRestRequest fullRestRequest) throws Exception {
        Response pingResponse = clientPing.command(fullRestRequest);
        return PingResponse.makeFromString(((String) pingResponse.getEntity()));
    }

    private List<JSONObject> parseNotificationObjects(JSONArray notificationsOutput) {
        List<JSONObject> notificationList = new ArrayList<>();
        notificationsOutput.iterator().forEachRemaining(o -> notificationList.add((JSONObject) o));
        return notificationList;
    }

    private NotificationsResponse parseNotificationResponse(Response response) {
        assertThat(response.getEntity()).isInstanceOf(String.class);
        return NotificationsResponse.makeFromString(((String) response.getEntity()));
    }

    private FullRestRequest createFullRequestFromSession(HttpSessionState httpSession) {
        return createFullRequestFromSession(httpSession, "[]");
    }

    private FullRestRequest createFullRequestFromSession(HttpSessionState httpSession, String notifications) {
        return FullRestRequest
            .make()
            .setInitToken(httpSession.getInitToken())
            .setData(new JSONObject().put("notifications", notifications))
            .setLog(Collections.emptyList())
            .immutable();
    }

    private HttpSessionState createEmptyHttpSession() {
        return HttpSessionState.init(Collections.emptyMap(), serverProperties, "");
    }
}
