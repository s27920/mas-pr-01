package App.Util;

import App.Models.Mission.Mission;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MissionTimerService {
    private static MissionTimerService instance;
    private final ScheduledExecutorService executor;

    private MissionTimerService() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public static MissionTimerService getInstance(){
        if (instance == null){
            instance = new MissionTimerService();
        }

        return instance;
    }

    public void registerMission(long completionTimeInMillis, Runnable callback){
        executor.schedule(callback, completionTimeInMillis, TimeUnit.MILLISECONDS);
    }

    public void dispose(){
        executor.shutdownNow();
    }
}
