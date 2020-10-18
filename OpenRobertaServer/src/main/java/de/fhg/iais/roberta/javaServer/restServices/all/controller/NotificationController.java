package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.NotificationsResponse;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.NotificationService;
import de.fhg.iais.roberta.util.UtilForREST;

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
        notificationsResponse.setNotifications(notificationService.getNotifications());
        httpSessionState.setReceivedNotificationsDigest(notificationService.getCurrentDigest());

        UtilForREST.addSuccessInfo(notificationsResponse, Key.SERVER_SUCCESS);
        return UtilForREST.responseWithFrontendInfo(notificationsResponse, httpSessionState, null);
    }

    @POST
    @Path("/postNotifications")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postNotifications(FullRestRequest fullRestRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRestRequest, true);
        BaseResponse response = BaseResponse.make();

        if ( isRobertaUser(httpSessionState) ) {
            try {
                String notificationsJSON = fullRestRequest.getData().getString("notifications");
                notificationService.saveNotifications(notificationsJSON);
                LOG.info("new notifications are in effect");
                UtilForREST.addSuccessInfo(response, Key.NOTIFICATION_SUCCESS);
            } catch ( Exception e ) {
                UtilForREST.addErrorInfo(response, Key.NOTIFICATION_ERROR_INVALID_SYNTAX, e.getMessage());
                LOG.warn("An error occurred while parsing JSON", e);
            }
        } else {
            LOG.warn("A unprivileged user with id {} tried to change notifications", httpSessionState.getUserId());
            UtilForREST.addErrorInfo(response, Key.NOTIFICATION_ERROR_INVALID_PERMISSION);
        }

        return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
    }

    private boolean isRobertaUser(HttpSessionState httpSessionState) {
        return httpSessionState.getUserId() == 1;
    }
}
