package de.fhg.iais.roberta.util;

import org.assertj.core.api.JUnitSoftAssertions;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

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
    public void setUp() throws Exception {
        adminDir = new File(temporaryFolder.getRoot(), "adminDir");
        Mockito.doReturn(adminDir.toString()).when(serverProperties).getStringProperty("server.admin.dir");
        notificationService = new NotificationService(serverProperties);
    }

    @Test
    public void getAllNotificationsShouldReturnAllNotifications() throws IOException {
        Map<String, String> notificationsInput = new HashMap<>();

        JSONObject notification1 = new JSONObject("{\n" + "  \"triggers\": [],\n" + "  \"conditions\": [],\n" + "  \"once\": true\n" + "}");

        notificationsInput.put("notification1", notification1.toString());

        JSONObject notification2 = new JSONObject("{\n" + "  \"handlers\": []\n" + "}");
        notificationsInput.put("notification2", notification2.toString());

        setupNotifications(notificationsInput);

        List<JSONObject> notificationsResult = notificationService.getAllNotifications();
        assertThat(notificationsResult).hasSize(2).anySatisfy(result -> {
            assertThat(result.similar(notification1.put("id", "notification1"))).isTrue();
        }).anySatisfy(result -> {
            assertThat(result.similar(notification2.put("id", "notification2"))).isTrue();
        });
    }

    @Test
    public void saveNotificationShouldSaveNotificationToFileSystem() throws IOException {
        File notificationDirectory = new File(adminDir, "notifications");

        JSONObject notificationToSave =
            new JSONObject("{\n" + "  \"triggers\": [],\n" + "  \"conditions\": [],\n" + "  \"handlers\": [],\n" + "  \"once\": true\n" + "}");

        JSONObject firstNotification = notificationService.saveNotification(notificationToSave);
        JSONObject secondNotification = notificationService.saveNotification(notificationToSave);

        assertThat(notificationDirectory.listFiles()).hasSize(2).allSatisfy(file -> {
            assertThat(file).hasContent(notificationToSave.toString());
        });

        assertThat(firstNotification.getString("id")).isNotEqualTo(secondNotification.getString("id"));

        assertThat(firstNotification.getString("id")).matches("notification(-.*){5}");
        assertThat(secondNotification.getString("id")).matches("notification(-.*){5}");

        assertThat(notificationService.getAllNotificationIds()).contains(firstNotification.getString("id"), secondNotification.getString("id"));

        firstNotification.remove("id");
        secondNotification.remove("id");


        assertThat(firstNotification.similar(notificationToSave)).isTrue();
        assertThat(secondNotification.similar(notificationToSave)).isTrue();
    }

    @Test
    public void notificationsShouldBeUpdatedOnInitialization() throws IOException {
        setupNotifications(false, "notification1", "notification2");

        notificationService = new NotificationService(serverProperties);
        assertThat(notificationService.getAllNotificationIds()).containsExactly("notification1", "notification2");
    }

    @Test
    public void areNotificationsCompleteShouldDependOnFilesystem() throws IOException {
        setupNotifications();
        softly.assertThat(notificationService.areNotificationsComplete(Collections.emptySet())).isTrue();

        setupNotifications("notification1", "notification2");
        softly.assertThat(notificationService.areNotificationsComplete(Collections.emptySet())).isFalse();

        HashSet<String> expectedNotifications = new HashSet<>(Arrays.asList("notification1", "notification2"));
        softly.assertThat(notificationService.areNotificationsComplete(expectedNotifications)).isTrue();
    }

    @Test
    public void deleteNotificationShouldDeleteNotificationOnFilesystem() throws IOException {
        File notificationDir = setupNotifications("notification1");
        assertThat(notificationDir).isDirectoryContaining(file -> file.getName().equals("notification1.json"));
        assertThat(notificationService.getAllNotificationIds()).contains("notification1");

        JSONObject deletedNotification = notificationService.deleteNotification("notification1");
        assertThat(notificationDir).isDirectoryNotContaining(file -> file.getName().equals("notification1.json"));
        assertThat(notificationService.getAllNotificationIds()).doesNotContain("notification1");

        assertThat(deletedNotification.getString("id")).isEqualTo("notification1");
    }

    @Test
    public void deleteNotificationShouldThrowFileNotFound() {
        assertThatThrownBy(() -> notificationService.deleteNotification("notification1"))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContainingAll("The notification does not exist", "notifications\\notification1.json");
    }

    private File setupNotifications(String... ids) throws IOException {
        return setupNotifications(true, ids);
    }

    private File setupNotifications(boolean notify, String... ids) throws IOException {
        Map<String, String> notifications = Arrays.stream(ids).collect(Collectors.toMap(id -> id, id -> "{}"));
        return setupNotifications(notify, notifications);
    }

    private File setupNotifications(Map<String, String> notifications) throws IOException {
        return setupNotifications(true, notifications);
    }

    private File setupNotifications(boolean notify, Map<String, String> notifications) throws IOException {
        File notificationsDir = new File(adminDir, "notifications");

        if (!notificationsDir.exists()) notificationsDir.mkdirs();

        pasteNotificationsToDir(notificationsDir, notifications);

        if (notify) {
            notificationService.updateNotificationIds();
        }
        return notificationsDir;
    }

    private void pasteNotificationsToDir(File notificationsDir, Map<String, String> notifications) throws IOException {
        final String extension = "json";
        if ( !notificationsDir.exists() || !notificationsDir.isDirectory() ) {
            return;
        }

        for ( Map.Entry<String, String> entry : notifications.entrySet() ) {
            String notificationId = entry.getKey();

            String filename = String.format("%s.%s", notificationId, extension);
            File notificationFile = new File(notificationsDir, filename);
            try (FileWriter fileWriter = new FileWriter(notificationFile); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                String content = entry.getValue();
                bufferedWriter.write(content);
            }
        }
    }
}