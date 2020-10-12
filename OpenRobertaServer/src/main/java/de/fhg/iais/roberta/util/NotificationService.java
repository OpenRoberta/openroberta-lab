package de.fhg.iais.roberta.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NotificationService {

    public static final String FILENAME = "notifications.json";
    private final ServerProperties serverProperties;

    private String currentDigest;

    @Inject
    public NotificationService(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
        updateDigest();
    }

    /**
     * Parse all notifications from notification json file saved on filesystem
     * @return a list of json objects each representing one notification
     */
    public List<JSONObject> getNotifications() {
        Path notificationFile = getNotificationsFilePath();

        createNotificationsFileIfNotExist(notificationFile);

        String fileContent = Util.readFileContent(notificationFile.toString());

        JSONArray notificationArray = new JSONArray(fileContent);
        return IntStream.range(0, notificationArray.length())
                .mapToObj(notificationArray::getJSONObject)
                .collect(Collectors.toList());
    }

    private void createNotificationsFileIfNotExist(Path notificationFile) {
        if (!Files.exists(notificationFile)) {
            try {
                FileUtils.writeStringToFile(notificationFile.toFile(), new JSONArray().toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Could not write notification file " + notificationFile.toString(), e);
            }
        }
    }

    /**
     * Validates notifications and saves notification to the filesystem if everything is valid
     * Also updates the digest field, see {@link #getCurrentDigest()}
     * @param notifications JSON Array as String
     * @throws JSONException if the json format is invalid
     */
    public void saveNotifications(String notifications) throws JSONException {
        Path notificationFile = getNotificationsFilePath();
        try {
            JSONArray jsonArray = new JSONArray(notifications);
            FileUtils.writeStringToFile(notificationFile.toFile(), jsonArray.toString(), StandardCharsets.UTF_8);
            currentDigest = digestOfFile(notificationFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not write notification file " + notificationFile.toString(), e);
        }
    }

    /**
     * Get the current digest for the notifications
     * @return the current digest
     */
    public String getCurrentDigest() {
        return currentDigest;
    }

    void updateDigest() {
        try {
            currentDigest = digestOfFile(getNotificationsFilePath());
        } catch (IOException e) {
            throw new RuntimeException("Could not hash notifications file", e);
        }
    }

    /**
     * Hashes a file and returns the digest
     * @param file the file which shall be hashed
     * @return a hash of an empty string if the file does not exists
     */
    static String digestOfFile(Path file) throws IOException {
        if (!Files.exists(file)) {
            return DigestUtils.md5Hex("").toUpperCase();
        }

        try (InputStream inputStream = Files.newInputStream(file)) {
            return DigestUtils.md5Hex(inputStream).toUpperCase();
        }
    }

    private Path getNotificationsFilePath() {
        return Paths.get(serverProperties.getStringProperty("server.admin.dir")).resolve(FILENAME);
    }
}
