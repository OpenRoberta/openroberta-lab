package de.fhg.iais.roberta.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        adminDir = new File(temporaryFolder.getRoot(), "adminDir");
        Mockito.doReturn(adminDir.toString()).when(serverProperties).getStringProperty("server.admin.dir");
        notificationService = new NotificationService(serverProperties);
    }

    @Test
    public void testGetNotifications() throws IOException {
        JSONObject notification0 = new JSONObject("{\n" + "  \"triggers\": [],\n" + "  \"conditions\": [],\n" + "  \"once\": true\n" + "}");
        JSONObject notification1 = new JSONObject("{\n" + "  \"handlers\": []\n" + "}");

        JSONArray jsonArray = new JSONArray(Arrays.asList(notification0, notification1));
        notificationService.saveNotifications(jsonArray.toString());
        JSONArray notificationsArray = notificationService.getNotifications();
        assertThat(notificationsArray).hasSize(2);
        assertThat(notificationsArray.getJSONObject(0)).satisfies(result -> assertThat(result.similar(notification0)).isTrue());
        assertThat(notificationsArray.getJSONObject(1)).satisfies(result -> assertThat(result.similar(notification1)).isTrue());
    }

    @Test
    public void testSaveNotifications() {
        JSONArray notifications = new JSONArray("[{\"once\": true, \"name\": \"Calliope\"}]");
        notificationService.saveNotifications(notifications.toString());

        Path notificationFile = adminDir.toPath().resolve("notifications.json");
        assertThat(notificationFile).exists().hasContent(notifications.toString());
    }

    @Test
    public void testSaveNotificationsInvalidJsonFormat() {
        assertThatThrownBy(() -> notificationService.saveNotifications("[{\n" + "  \"triggers\": []\n" + "]"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Could not store notifications and update hashed data.");
    }

    @Test
    public void testCurrentDigest() throws IOException {
        JSONArray notifications = new JSONArray("[{\"once\": true, \"name\": \"Calliope\"}]");
        notificationService.saveNotifications(notifications.toString());
        String currentDigest = notificationService.getCurrentDigest();
        assertThat(currentDigest).isEqualTo(NotificationService.digestOfString(notifications.toString()));
    }
}