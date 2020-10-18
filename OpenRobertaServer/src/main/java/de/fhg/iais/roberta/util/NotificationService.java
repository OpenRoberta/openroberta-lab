package de.fhg.iais.roberta.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    public static final String FILENAME = "notifications.json";
    private static final JSONArray NO_NOTIFICATIONS = new JSONArray();
    private Path notificationFilePath; // is final, but injected :-<

    private String currentDigest; // digest of currentNotifications
    private JSONArray currentNotifications; // the notification to use. Must be consistent with the digest!

    /**
     * establish notication service. Read notifications from file, where the server stored them in the past.
     *
     * @param serverProperties
     */
    @Inject
    public NotificationService(ServerProperties serverProperties) {
        try {
            notificationFilePath = Paths.get(serverProperties.getStringProperty("server.admin.dir")).resolve(FILENAME);
            String notificationsToBeUsed;
            if ( !Files.exists(notificationFilePath) ) {
                notificationsToBeUsed = NO_NOTIFICATIONS.toString();
            } else {
                notificationsToBeUsed = Util.readFileContent(notificationFilePath.toString());
            }
            saveNotifications(notificationsToBeUsed); // check for right to overwrite notifications file
            LOG.info("NotificationsService started");
        } catch ( Exception e ) {
            currentDigest = null;
            currentNotifications = NO_NOTIFICATIONS;
            LOG.error("NotificationService could not be established. NO notifications!", e);
        }
    }

    /**
     * Validates notification String and saves notification to the file system. Updates the {@linkplain currentNotifications} and {@linkplain currentDigest}
     * fields
     *
     * @param notificationsString
     */
    public void saveNotifications(String notificationsString) {
        try {
            JSONArray jsonArray = new JSONArray(notificationsString); // TODO: check schema, consistency-checks missing!
            String jsonArrayAsString = jsonArray.toString();
            FileUtils.writeStringToFile(notificationFilePath.toFile(), jsonArrayAsString, StandardCharsets.UTF_8);
            currentDigest = digestOfString(jsonArrayAsString); // change both or none!
            currentNotifications = jsonArray;
        } catch ( Exception e ) {
            LOG.error("notificationsString is invalid or could not be stored. Old notifications remain in use", e); // a bit redundant :-)
            throw new RuntimeException("Could not store notifications and update hashed data. Old notifications remain in use", e);
        }

    }

    /**
     * @return the array of all currently used notifications
     */
    public JSONArray getNotifications() {
        return currentNotifications;
    }

    /**
     * @return the current digest for the notifications
     */
    public String getCurrentDigest() {
        return currentDigest;
    }

    /**
     * Hashes a String and returns the digest. Public for testing.
     *
     * @param notifications the String to be hashed
     * @return a hash of the string, never null
     */
    public static String digestOfString(String notifications) throws IOException {
        return DigestUtils.md5Hex(new ByteArrayInputStream(notifications.getBytes())).toUpperCase();
    }
}
