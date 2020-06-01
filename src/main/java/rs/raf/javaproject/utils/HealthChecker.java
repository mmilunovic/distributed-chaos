package rs.raf.javaproject.utils;

import rs.raf.javaproject.requests.node.Ping;

public class HealthChecker implements Runnable{

    private final long SLEEP_TIME = 1000;

    @Override
    public void run() {
        while(true){
            boolean alive = isAlive();

            if(!alive){
                // TODO: Zapocni restart sistema
            }else{
                sleep(SLEEP_TIME);
            }
        }
    }

    private boolean isAlive() {
        // TODO: Dohvati sledbenika
        String nodeID = "";
        Ping pingRequest = new Ping(nodeID, TimeoutType.LOW);
        Boolean pingResult = pingRequest.execute();

        if(pingResult == null){
            // TODO: Dohvatamo prethodnika
            String predecesorID = ""; // Ovo je za prethodnika
            Ping longPingRequest = new Ping(predecesorID, TimeoutType.LOW);
            Boolean longPingResult = longPingRequest.execute();

            if(longPingResult == null){
                return false;
            }
            return true;
        }

        return true;
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }
}
