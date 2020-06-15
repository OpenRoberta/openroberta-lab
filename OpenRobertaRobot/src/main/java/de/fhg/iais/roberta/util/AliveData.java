package de.fhg.iais.roberta.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;

public class AliveData {
    private static final AtomicLong clientCallsTotal = new AtomicLong(0);
    private static final AtomicLong clientCallsDelta = new AtomicLong(0);
    private static final AtomicLong robotCallsTotal = new AtomicLong(0);
    private static final AtomicLong robotCallsDelta = new AtomicLong(0);
    private static final AtomicLong aliveCallsDelta = new AtomicLong(0);
    private static final AtomicLong loginsTotal = new AtomicLong(0);
    private static final AtomicLong loginsDelta = new AtomicLong(0);
    private static final AtomicLong robotCommunicationStatesTotal = new AtomicLong(0);
    private static final AtomicLong robotCommunicationStatesDelta = new AtomicLong(0);

    private static final Clock running = Clock.start();

    public static JSONObject getAndUpdateAliveState() throws Exception {
        long actualClientCallsTotal = clientCallsTotal.get();
        long actualClientCallsDelta = clientCallsDelta.getAndSet(0);
        long actualRobotCallsTotal = robotCallsTotal.get();
        long actualRobotCallsDelta = robotCallsDelta.getAndSet(0);
        long actualLoginsTotal = loginsTotal.get();
        long actualLoginsDelta = loginsDelta.getAndSet(0);
        long actualAliveCallsDelta = aliveCallsDelta.incrementAndGet();
        long actualRobotCommunicationStatesTotal = robotCommunicationStatesDelta.get();
        long actualRobotCommunicationStatesDelta =
            actualRobotCommunicationStatesTotal - robotCommunicationStatesDelta.getAndSet(actualRobotCommunicationStatesTotal);
        String runningSince = running.elapsedSecFormatted();
        JSONObject answer = new JSONObject();
        answer.put("clientCallsTotal", actualClientCallsTotal).put("clientCallsDelta", actualClientCallsDelta);
        answer.put("robotCallsTotal", actualRobotCallsTotal).put("robotCallsDelta", actualRobotCallsDelta);
        answer.put("loginsTotal", actualLoginsTotal).put("loginsDelta", actualLoginsDelta);
        answer.put("aliveCalls", actualAliveCallsDelta).put("runningSinceSec", runningSince).put("runningSinceHr", (running.elapsedSec() / 3600 + 1) + " h");
        answer.put("robotsDelta", actualRobotCommunicationStatesDelta).put("robotsTotal", actualRobotCommunicationStatesTotal);
        answer.put("serverDate", new Date().toString());
        return answer;
    }

    public static void rememberClientCall() {
        clientCallsTotal.incrementAndGet();
        clientCallsDelta.incrementAndGet();
    }

    /**
     * remember that a robot-related REST-call was accepted. Optionally save the number of robots connected or waiting for token approval
     *
     * @param robotCommunicationDataSize number of robots connected to the lab; -1 if its value is unknown for the REST call
     */
    public static void rememberRobotCall(long robotCommunicationDataSize) {
        robotCallsTotal.incrementAndGet();
        robotCallsDelta.incrementAndGet();
        if ( robotCommunicationDataSize >= 0 ) {
            robotCommunicationStatesTotal.set(robotCommunicationDataSize);
        }
    }

    public static void rememberLogin() {
        loginsTotal.incrementAndGet();
        loginsDelta.incrementAndGet();
    }
}