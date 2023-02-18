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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    public static final String FILENAME = "notifications.json";
    private static final JSONObject NO_NOTIFICATIONS = new JSONObject();
    private Path notificationFilePath; // is final, but injected :-<

    private String currentDigest; // digest of currentNotifications
    private JSONObject currentNotifications; // the notification to use. Must be consistent with the digest!

    /**
     * establish notication service. Read notifications from file, where the server stored them in the past.
     *
     * @param serverProperties
     */
    @Inject
    public NotificationService(ServerProperties serverProperties) {
        try {
            this.notificationFilePath = Paths.get(serverProperties.getStringProperty("server.admin.dir")).resolve(FILENAME);
            String notificationsToBeUsed;
            if ( !Files.exists(this.notificationFilePath) ) {
                notificationsToBeUsed = NO_NOTIFICATIONS.toString();
            } else {
                notificationsToBeUsed = Util.readFileContent(this.notificationFilePath.toString());
            }
            saveNotifications(notificationsToBeUsed); // check for right to overwrite notifications file
            LOG.info("NotificationsService started");
        } catch ( Exception e ) {
            this.currentDigest = null;
            this.currentNotifications = NO_NOTIFICATIONS;
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
            JSONObject jsonObject = new JSONObject(notificationsString); // TODO: check schema, consistency-checks missing!
            String jsonObjectAsString = jsonObject.toString();
            FileUtils.writeStringToFile(this.notificationFilePath.toFile(), jsonObjectAsString, StandardCharsets.UTF_8);
            this.currentDigest = digestOfString(jsonObjectAsString); // change both or none!
            this.currentNotifications = jsonObject;
        } catch ( Exception e ) {
            LOG.error("notificationsString is invalid or could not be stored. Old notifications remain in use", e); // a bit redundant :-)
            throw new RuntimeException("Could not store notifications and update hashed data. Old notifications remain in use", e);
        }

    }

    /**
     * @return the array of all currently used notifications
     */
    public JSONObject getNotifications() {
        return this.currentNotifications;
    }

    /**
     * @return the current digest for the notifications
     */
    public String getCurrentDigest() {
        return this.currentDigest;
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
