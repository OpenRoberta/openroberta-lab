package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.NotificationsResponse;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.NotificationService;
import de.fhg.iais.roberta.util.UtilForREST;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

@Path("/notifications")
public class NotificationController {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    @Inject
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @POST
    @Path("/getNotifications")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotifications(FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, false);

        NotificationsResponse notificationsResponse = NotificationsResponse.make();

        List<JSONObject> allNotifications = notificationService.getAllNotifications();

        httpSessionState.setReceivedNotifications(notificationService.getAllNotificationIds());
        JSONArray jsonArray = new JSONArray(allNotifications);
        notificationsResponse.setNotifications(jsonArray);

        UtilForREST.addSuccessInfo(notificationsResponse, Key.SERVER_SUCCESS);
        return UtilForREST.responseWithFrontendInfo(notificationsResponse, httpSessionState, null);
    }

    @POST
    @Path("/postNotification")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postNotification(FullRestRequest fullRestRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRestRequest, true);

        NotificationsResponse notificationsResponse = NotificationsResponse.make();
        notificationsResponse.setNotifications(new JSONArray());

        if ( isRobertaUser(httpSessionState) ) {
            JSONObject notification = notificationService.saveNotification(fullRestRequest.getData());
            notificationsResponse.setNotifications(new JSONArray(Collections.singletonList(notification)));
            UtilForREST.addSuccessInfo(notificationsResponse, Key.NOTIFICATION_SUCCESS);
        } else {
            LOG.warn("A unprivileged user with id {} tried to save a notification", httpSessionState.getUserId());
            UtilForREST.addErrorInfo(notificationsResponse, Key.NOTIFICATION_ERROR_INVALID_PERMISSION);
        }

        return UtilForREST.responseWithFrontendInfo(notificationsResponse, httpSessionState, null);
    }

    @POST
    @Path("/deleteNotification")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteNotification(FullRestRequest fullRestRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRestRequest, true);

        NotificationsResponse notificationsResponse = NotificationsResponse.make();
        notificationsResponse.setNotifications(new JSONArray());

        if ( isRobertaUser(httpSessionState) ) {
            JSONObject data = fullRestRequest.getData();
            try {
                JSONObject notification = notificationService.deleteNotification(data.getString("id"));
                notificationsResponse.setNotifications(new JSONArray(Collections.singletonList(notification)));
                UtilForREST.addSuccessInfo(notificationsResponse, Key.NOTIFICATION_SUCCESS);
            } catch ( FileNotFoundException e ) {
                LOG.warn("Requested to delete not existing notification", e);
                UtilForREST.addErrorInfo(notificationsResponse, Key.NOTIFICATION_ERROR_NOT_FOUND);
            }
        } else {
            LOG.warn("A unprivileged user with id {} tried to delete a notification", httpSessionState.getUserId());
            UtilForREST.addErrorInfo(notificationsResponse, Key.NOTIFICATION_ERROR_INVALID_PERMISSION);
        }

        return UtilForREST.responseWithFrontendInfo(notificationsResponse, httpSessionState, null);
    }

    private boolean isRobertaUser(HttpSessionState httpSessionState) {
        return httpSessionState.getUserId() == 1;
    }
}
