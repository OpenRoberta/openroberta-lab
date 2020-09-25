package de.fhg.iais.roberta.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.exists;

public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private static final String NOTIFICATION_EXTENSION = "json";
    private final ServerProperties serverProperties;

    private final Set<String> allNotificationIds = new HashSet<>();

    @Inject
    public NotificationService(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
        updateNotificationIds();
    }

    public Set<String> getAllNotificationIds() {
        return allNotificationIds;
    }

    public boolean areNotificationsComplete(Set<String> receivedNotifications) {
        return receivedNotifications == null || receivedNotifications.equals(getAllNotificationIds());
    }

    public List<JSONObject> getAllNotifications() {
        return readNotifications();
    }

    public JSONObject saveNotification(JSONObject notification) {
        String jsonString = notification.toString();

        String notificationId = generateUniqueId();
        String filename = getFilename(notificationId);

        Path notificationDirectory = getNotificationDirectory();
        Path notificationFile = notificationDirectory.resolve(filename);

        try {
            FileUtils.writeStringToFile(notificationFile.toFile(), jsonString, StandardCharsets.UTF_8);

            allNotificationIds.add(notificationId);

            return new JSONObject(jsonString).put("id", notificationId);
        } catch ( IOException e ) {
            throw new RuntimeException("Could not write notification file " + notificationFile.toString(), e);
        }
    }

    public JSONObject deleteNotification(String notificationId) throws FileNotFoundException {
        String filename = getFilename(notificationId);

        Path notificationFile = getNotificationDirectory().resolve(filename);
        if (!exists(notificationFile)) {
            throw new FileNotFoundException("The notification does not exist " + notificationFile);
        }

        try {
            JSONObject notification = readJsonFile(notificationFile.toFile());

            Files.delete(notificationFile);
            allNotificationIds.remove(notificationId);

            return notification;
        } catch ( IOException e ) {
            throw new RuntimeException("Could not delete notification file " + notificationFile, e);
        }
    }

    void updateNotificationIds() {
        allNotificationIds.addAll(readNotificationIds());
    }

    private String getFilename(String id) {
        return String.format("%s.%s", id, NOTIFICATION_EXTENSION);
    }

    private String generateUniqueId() {
        String uuid = UUID.randomUUID().toString();
        return String.format("notification-%s", uuid);
    }

    private List<String> readNotificationIds() {
        return giveAllNotificationFilePaths().map(this::filenameWithoutExtension).collect(Collectors.toList());
    }

    private List<JSONObject> readNotifications() {
        return giveAllNotificationFilePaths().map(Path::toFile).map(this::readJsonFile).collect(Collectors.toList());
    }

    private Stream<Path> giveAllNotificationFilePaths() {
        Path notificationDirectory = getNotificationDirectory();
        try {
            return exists(notificationDirectory)
                ? Files.list(notificationDirectory).filter(path -> path.toString().endsWith(NOTIFICATION_EXTENSION))
                : Stream.empty();
        } catch ( IOException e ) {
            throw new RuntimeException("Could not read the notification directory" + notificationDirectory, e);
        }
    }

    private Path getNotificationDirectory() {
        return Paths.get(serverProperties.getStringProperty("server.admin.dir")).resolve("notifications");
    }

    private JSONObject readJsonFile(File file) {
        return new JSONObject(Util.readFileContent(file.getAbsolutePath())).put("id", filenameWithoutExtension(file.toPath()));
    }

    private String filenameWithoutExtension(Path file) {
        return FilenameUtils.removeExtension(file.getFileName().toString());
    }
}
