package de.fhg.iais.roberta.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.JUnitSoftAssertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

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
        adminDir = new File(temporaryFolder.getRoot(), "adminDir");
        Mockito.doReturn(adminDir.toString()).when(serverProperties).getStringProperty("server.admin.dir");
        notificationService = new NotificationService(serverProperties);
    }

    @Test
    public void getNotifications() throws IOException {
        JSONObject notification1 = new JSONObject("{\n" + "  \"triggers\": [],\n" + "  \"conditions\": [],\n" + "  \"once\": true\n" + "}");
        JSONObject notification2 = new JSONObject("{\n" + "  \"handlers\": []\n" + "}");

        JSONArray jsonArray = new JSONArray(Arrays.asList(notification1, notification2));
        writeNotifications(jsonArray.toString());

        List<JSONObject> notificationsResult = notificationService.getNotifications();
        assertThat(notificationsResult)
            .hasSize(2)
            .anySatisfy(result -> assertThat(result.similar(notification1)).isTrue())
            .anySatisfy(result -> assertThat(result.similar(notification2)).isTrue());
    }

    @Test
    public void saveNotifications() {
        JSONArray notifications = new JSONArray("[{\"once\": true, \"name\": \"Calliope\"}]");
        notificationService.saveNotifications(notifications.toString());

        Path notificationFile = adminDir.toPath().resolve("notifications.json");
        assertThat(notificationFile)
                .exists()
                .hasContent(notifications.toString());
    }

    @Test
    public void saveNotifications_invalidJsonFormat() {
        assertThatThrownBy(() -> notificationService.saveNotifications("[{\n" + "  \"triggers\": []\n" + "]"))
                .isInstanceOf(JSONException.class)
                .hasMessageContaining("}");
    }

    @Test
    public void getCurrentDigest() throws IOException {
        JSONArray notifications = new JSONArray("[{\"once\": true, \"name\": \"Calliope\"}]");

        notificationService.saveNotifications(notifications.toString());
        String currentDigest = notificationService.getCurrentDigest();

        Path notificationFile = adminDir.toPath().resolve("notifications.json");

        assertThat(currentDigest)
                .isEqualTo(NotificationService.digestOfFile(notificationFile));
    }

    @Test
    public void digestOfFile() throws IOException {
        String randomString = RandomStringUtils.random(30);

        File file = temporaryFolder.newFile();
        FileUtils.writeStringToFile(file, randomString, StandardCharsets.UTF_8);

        String expected = DigestUtils.md5Hex(randomString).toUpperCase();
        assertThat(NotificationService.digestOfFile(file.toPath())).isEqualTo(expected);
    }

    private void writeNotifications(String json) throws IOException {
        Path notificationFile = adminDir.toPath().resolve("notifications.json");
        FileUtils.writeStringToFile(notificationFile.toFile(), json, StandardCharsets.UTF_8);
    }

}