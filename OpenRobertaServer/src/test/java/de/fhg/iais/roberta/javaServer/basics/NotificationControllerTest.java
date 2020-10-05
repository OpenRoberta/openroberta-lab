package de.fhg.iais.roberta.javaServer.basics;

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
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationControllerTest {
    public static final int ADMIN_USER_ID = 1;
    public static final int NOT_ADMIN_USER_ID = 2;
    private final Logger LOG = LoggerFactory.getLogger(NotificationControllerTest.class);

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private ServerProperties serverProperties;

    @Mock
    private NotificationService notificationService;

    private NotificationController notificationController;

    @Before
    public void setUp() throws Exception {
        notificationController = new NotificationController(notificationService);
        UtilForREST.setNotificationService(notificationService);

        setupNotifications();
    }

    @Test
    public void getNotificationsAndSaveSeenNotificationsToSession() throws IOException {
        setupNotificationIds("notification1", "notification2");

        HttpSessionState httpSession = createEmptyHttpSession();
        FullRestRequest fullRestRequest = createFullRequestFromSession(httpSession);

        notificationController.getNotifications(fullRestRequest);
        assertThat(httpSession.getReceivedNotifications()).containsExactly("notification1", "notification2");
    }

    @Test
    public void getNotificationsInCorrectStructure() throws IOException {
        setupNotifications(
            "{\n" + "  \"triggers\": [],\n" + "  \"id\": \"notification1\"\n" + "}",
            "{\n" + "  \"conditions\": [" + "],\n" + "  \"id\": \"notification2\"\n" + "}");

        HttpSessionState httpSession = createEmptyHttpSession();
        FullRestRequest fullRestRequest = createFullRequestFromSession(httpSession);

        Response response = notificationController.getNotifications(fullRestRequest);
        List<JSONObject> notificationList = parseNotificationsFromResponse(response);

        assertThat(httpSession.getReceivedNotifications()).containsExactly("notification1", "notification2");
        assertThat(notificationList).anySatisfy(json -> {
            assertThat(json.getString("id")).isEqualTo("notification1");
            assertThat(json.getJSONArray("triggers")).isEmpty();
        }).anySatisfy(json -> {
            assertThat(json.getString("id")).isEqualTo("notification2");
            assertThat(json.getJSONArray("conditions")).isEmpty();
        });

    }

    @Test
    public void getNotificationsAndNotificationsAvailableFlag() throws Exception {
        ClientPing clientPing = new ClientPing("", null);

        setupNotificationIds("notifications1", "notification2");

        HttpSessionState httpSession = createEmptyHttpSession();
        FullRestRequest fullRestRequest = createFullRequestFromSession(httpSession);

        Response response = notificationController.getNotifications(fullRestRequest);

        mockNotificationsComplete(true);
        BaseResponse firstBaseResponse = baseResponsePing(clientPing, fullRestRequest);
        assertThat(firstBaseResponse.getNotificationsAvailable()).isEqualTo(false);

        setupNotificationIds("notifications1", "notification2", "notification3");

        mockNotificationsComplete(false);
        BaseResponse secondBaseResponse = baseResponsePing(clientPing, fullRestRequest);
        assertThat(secondBaseResponse.getNotificationsAvailable()).isEqualTo(true);
    }

    @Test
    public void postNotificationNotAsAdmin() {
        JSONObject notification = new JSONObject("{\n" + "  \"triggers\": []\n" + "}");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(NOT_ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, notification);

        Response response = notificationController.postNotification(fullRestRequest);

        verify(notificationService, never()).saveNotification(any(JSONObject.class));

        NotificationsResponse baseResponse = NotificationsResponse.makeFromString(((String) response.getEntity()));

        assertThat(baseResponse.getRc()).isEqualTo("error");
        assertThat(baseResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_ERROR_INVALID_PERMISSION");
        assertThat(baseResponse.getCause()).isEqualTo("ORA_NOTIFICATION_ERROR_INVALID_PERMISSION");
    }

    @Test
    public void postNotificationAsAdmin() {
        JSONObject notification = new JSONObject("{\n" + "  \"triggers\": []\n" + "}");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, notification);

        doAnswer(invocation -> invocation.getArgumentAt(0, JSONObject.class).put("id", "notification1"))
            .when(notificationService)
            .saveNotification(any(JSONObject.class));

        Response response = notificationController.postNotification(fullRestRequest);

        ArgumentCaptor<JSONObject> argumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        verify(notificationService).saveNotification(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(notification);

        NotificationsResponse notificationsResponse = parseNotificationResponse(response);
        List<JSONObject> jsonObjects = parseNotificationObjects(notificationsResponse.getNotifications());

        assertThat(notificationsResponse.getRc()).isEqualTo("ok");
        assertThat(notificationsResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_SUCCESS");

        assertThat(jsonObjects).singleElement().satisfies(jsonResponse -> {
            assertThat(jsonResponse.getString("id")).isEqualTo("notification1");
            assertThat(jsonResponse.getJSONArray("triggers")).isEmpty();
        });

    }

    @Test
    public void deleteNotificationNotAsAdmin() throws FileNotFoundException {
        String notificationId = "notification1";
        JSONObject data = new JSONObject("{\n" + "  \"id\": \"" + notificationId + "\"\n" + "}");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(NOT_ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, data);

        Response response = notificationController.deleteNotification(fullRestRequest);
        verify(notificationService, never()).deleteNotification(notificationId);

        NotificationsResponse notificationsResponse = parseNotificationResponse(response);
        assertThat(notificationsResponse.getRc()).isEqualTo("error");
        assertThat(notificationsResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_ERROR_INVALID_PERMISSION");
    }

    @Test
    public void deleteNotificationAsAdmin() throws FileNotFoundException {
        String notificationId = "notification1";
        JSONObject data = new JSONObject("{\n" + "  \"id\": \"" + notificationId + "\"\n" + "}");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, data);

        JSONObject notification = new JSONObject("{\n" + "  \"id\": \"" + notificationId + "\",\n" + "  \"triggers\": []\n" + "}");
        doReturn(notification).when(notificationService).deleteNotification(notificationId);
        Response response = notificationController.deleteNotification(fullRestRequest);
        verify(notificationService).deleteNotification(notificationId);

        NotificationsResponse notificationsResponse = parseNotificationResponse(response);
        assertThat(notificationsResponse.getRc()).isEqualTo("ok");
        assertThat(notificationsResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_SUCCESS");

        List<JSONObject> notifications = parseNotificationObjects(notificationsResponse.getNotifications());
        assertThat(notifications).singleElement().satisfies(deletedNotification -> {
            assertThat(deletedNotification.getString("id")).isEqualTo(notificationId);
            assertThat(deletedNotification.getJSONArray("triggers")).isEmpty();
        });
    }

    @Test
    public void deleteNotificationAsAdminNotExistingNotification() throws FileNotFoundException {
        String notificationId = "notification1";
        JSONObject data = new JSONObject("{\n" + "  \"id\": \"" + notificationId + "\"\n" + "}");

        HttpSessionState session = createEmptyHttpSession();
        session.setUserClearDataKeepTokenAndRobotId(ADMIN_USER_ID);
        FullRestRequest fullRestRequest = createFullRequestFromSession(session, data);

        doThrow(new FileNotFoundException("The notification could not be found")).when(notificationService).deleteNotification(notificationId);
        Response response = notificationController.deleteNotification(fullRestRequest);
        verify(notificationService).deleteNotification(notificationId);

        NotificationsResponse notificationsResponse = parseNotificationResponse(response);
        assertThat(notificationsResponse.getRc()).isEqualTo("error");
        assertThat(notificationsResponse.getMessage()).isEqualTo("ORA_NOTIFICATION_ERROR_NOT_FOUND");
    }

    private void mockNotificationsComplete(boolean notificationsComplete) {
        doReturn(notificationsComplete).when(notificationService).areNotificationsSynchronized(any());
    }

    private BaseResponse baseResponsePing(ClientPing clientPing, FullRestRequest fullRestRequest) throws Exception {
        Response pingResponse = clientPing.command(fullRestRequest);
        return PingResponse.makeFromString(((String) pingResponse.getEntity()));
    }

    private List<JSONObject> parseNotificationsFromResponse(Response response) {
        NotificationsResponse notificationsResponse = parseNotificationResponse(response);
        return parseNotificationObjects(notificationsResponse.getNotifications());
    }

    private List<JSONObject> parseNotificationObjects(JSONArray notificationsOutput) {
        List<JSONObject> notificationList = new ArrayList<>();
        notificationsOutput.iterator().forEachRemaining(o -> notificationList.add((JSONObject) o));
        return notificationList;
    }

    private NotificationsResponse parseNotificationResponse(Response response) {
        assertThat(response.getEntity()).isInstanceOf(String.class);
        NotificationsResponse notificationsResponse = NotificationsResponse.makeFromString(((String) response.getEntity()));
        return notificationsResponse;
    }

    private void setupNotifications(String... notifications) throws IOException {
        List<JSONObject> jsonObjects = Arrays.stream(notifications).map(JSONObject::new).collect(Collectors.toList());
        List<String> ids = jsonObjects.stream().map(json -> json.getString("id")).collect(Collectors.toList());

        doReturn(jsonObjects).when(notificationService).getAllNotifications();
        doReturn(new HashSet<>(ids)).when(notificationService).getAllNotificationIds();
    }

    private void setupNotificationIds(String... ids) throws IOException {
        String[] notifications = Arrays.stream(ids).map(id -> new JSONObject().put("id", id)).map(JSONObject::toString).toArray(String[]::new);

        setupNotifications(notifications);
    }

    private FullRestRequest createFullRequestFromSession(HttpSessionState httpSession) {
        return createFullRequestFromSession(httpSession, new JSONObject());
    }

    private FullRestRequest createFullRequestFromSession(HttpSessionState httpSession, JSONObject data) {
        return FullRestRequest.make().setInitToken(httpSession.getInitToken()).setData(data).setLog(Collections.emptyList()).immutable();
    }

    private HttpSessionState createEmptyHttpSession() {
        return HttpSessionState.init(Collections.emptyMap(), serverProperties, "");
    }
}
