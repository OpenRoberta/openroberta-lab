package de.fhg.iais.roberta.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.assertj.core.api.JUnitSoftAssertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

    @Mock
    private ServerProperties serverProperties;

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    private NotificationService notificationService;
    private File adminDir;

    @Before
    public void setUp() {
        this.adminDir = new File(this.temporaryFolder.getRoot(), "adminDir");
        Mockito.doReturn(this.adminDir.toString()).when(this.serverProperties).getStringProperty("server.admin.dir");
        this.notificationService = new NotificationService(this.serverProperties);
    }

    @Test
    public void testGetNotifications() throws IOException {
        JSONObject notification0 = new JSONObject("{\n" + "  \"triggers\": [],\n" + "  \"conditions\": [],\n" + "  \"once\": true\n" + "}");
        JSONObject notification1 = new JSONObject("{\n" + "  \"handlers\": []\n" + "}");

        JSONArray jsonArray = new JSONArray(Arrays.asList(notification0, notification1));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("notifications", jsonArray);
        this.notificationService.saveNotifications(jsonObject.toString());
        JSONObject notificationsObject = this.notificationService.getNotifications();
        assertThat(notificationsObject.getJSONArray("notifications")).hasSize(2);
        assertThat(notificationsObject.getJSONArray("notifications").getJSONObject(0)).satisfies(result -> assertThat(result.similar(notification0)).isTrue());
        assertThat(notificationsObject.getJSONArray("notifications").getJSONObject(1)).satisfies(result -> assertThat(result.similar(notification1)).isTrue());
    }

    @Test
    public void testSaveNotifications() {
        JSONArray notifications = new JSONArray("[{\"once\": true, \"name\": \"Calliope\"}]");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("notifications", notifications);
        this.notificationService.saveNotifications(jsonObject.toString());

        Path notificationFile = this.adminDir.toPath().resolve("notifications.json");
        assertThat(notificationFile).exists().hasContent(jsonObject.toString());
    }

    @Test
    public void testSaveNotificationsInvalidJsonFormat() {
        assertThatThrownBy(() -> this.notificationService.saveNotifications("[{\n" + "  \"triggers\": []\n" + "]"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Could not store notifications and update hashed data.");
    }

    @Test
    public void testCurrentDigest() throws IOException {
        JSONArray notifications = new JSONArray("[{\"once\": true, \"name\": \"Calliope\"}]");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("notifications", notifications);
        this.notificationService.saveNotifications(jsonObject.toString());
        String currentDigest = this.notificationService.getCurrentDigest();
        assertThat(currentDigest).isEqualTo(NotificationService.digestOfString(jsonObject.toString()));
    }
}