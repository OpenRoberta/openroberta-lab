package de.fhg.iais.roberta.util;

import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.jettison.json.JSONObject;

public class AliveData {
    private static final AtomicLong clientCallsTotal = new AtomicLong(0);
    private static final AtomicLong clientCallsDelta = new AtomicLong(0);
    private static final AtomicLong robotCallsTotal = new AtomicLong(0);
    private static final AtomicLong robotCallsDelta = new AtomicLong(0);
    private static final AtomicLong aliveCallsDelta = new AtomicLong(0);
    private static final AtomicLong loginsTotal = new AtomicLong(0);
    private static final AtomicLong loginsDelta = new AtomicLong(0);
    private static final Clock running = Clock.start();

    public static JSONObject getAndUpdateAliveState() throws Exception {
        long actualclientCallsTotal = clientCallsTotal.get();
        long actualclientCallsDelta = clientCallsDelta.getAndSet(0);
        long actualrobotCallsTotal = robotCallsTotal.get();
        long actualrobotCallsDelta = robotCallsDelta.getAndSet(0);
        long actualloginsTotal = loginsTotal.get();
        long actualloginsDelta = loginsDelta.getAndSet(0);
        long actualaliveCallsDelta = aliveCallsDelta.incrementAndGet();
        String runningSince = running.elapsedSecFormatted();
        JSONObject answer = new JSONObject();
        answer.put("clientCallsTotal", actualclientCallsTotal).put("clientCallsDelta", actualclientCallsDelta);
        answer.put("robotCallsTotal", actualrobotCallsTotal).put("robotCallsDelta", actualrobotCallsDelta);
        answer.put("loginsTotal", actualloginsTotal).put("loginsDelta", actualloginsDelta);
        answer.put("aliveCalls", actualaliveCallsDelta).put("runningSince", runningSince);
        return answer;
    }

    public static void rememberClientCall() {
        clientCallsTotal.incrementAndGet();
        clientCallsDelta.incrementAndGet();
    }

    public static void rememberRobotCall() {
        robotCallsTotal.incrementAndGet();
        robotCallsDelta.incrementAndGet();
    }

    public static void rememberLogin() {
        loginsTotal.incrementAndGet();
        loginsDelta.incrementAndGet();
    }
}