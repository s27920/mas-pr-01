package Models.Util;

import Models.Mission.Mission;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

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

    public void registerMission(Mission mission, Runnable callback){
        executor.schedule(callback, mission.getMissionCompletionTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public void dispose(){
        executor.shutdownNow();
    }
}
