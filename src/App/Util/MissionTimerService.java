package App.Util;

import App.Models.Mission.Mission;
import App.Models.Mission.MissionStatus;
import App.Types.MissionControlLink;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class MissionTimerService {
    private static MissionTimerService instance;
    private final ScheduledExecutorService executor;
    private final MissionControlLink[] guiControlRegistry;
    private final int missionCount;
    private final Set<Mission> registeredMissions;


    private MissionTimerService() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.missionCount = SuperObject.getObjectsFromClass(Mission.class).size();
        this.guiControlRegistry = new MissionControlLink[missionCount];
        this.registeredMissions = new HashSet<>(SuperObject.getObjectsFromClass(Mission.class).stream().filter(m->m.getStatus()== MissionStatus.IN_PROGRESS).collect(Collectors.toList()));
    }

    public synchronized static MissionTimerService getInstance() {
        if (instance == null) {
            instance = new MissionTimerService();
        }
        return instance;
    }

    public void rebuildMissions(){
        try {
            MissionMarkerCreationCountDownLatch.getInstance().await();
            System.out.println("got through");
            if (registeredMissions.size() > 0){
                registeredMissions.forEach((m)->{
                    createTask(m);
                });
            }else{
                System.out.println("no missions to restore");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void registerMission(Mission mission /*left in for now, remove cleanly later*/) {
        registeredMissions.add(mission);
        createTask(mission);
    }

    private int linearRegisterSearch(Mission mission) {
        int index = -1;
        for (int i = 0; i < missionCount; i++) {
            if (guiControlRegistry[i].mission() == mission) {
                index = i;
            }
        }
        return index;
    }

    private int findEmptySlot() {
        for (int i = 0; i < missionCount; i++) {
            if (guiControlRegistry[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void setRegisterControlTask(Mission mission, Runnable callback) {
        this.guiControlRegistry[findEmptySlot()] = new MissionControlLink(mission, callback);
    }

    public static void shutdown() {
        getInstance().executor.shutdown();
    }

    private void createTask(Mission mission){
        mission.getMissionCompletionTime();
        long missionCompletionTime = mission.getMissionCompletionTime();
        long startTime = mission.getStartTimeMillis();
        long currTime = System.currentTimeMillis();
        long elapseTime = currTime - startTime;
        int index = linearRegisterSearch(mission);
        if (index != -1) {
            Runnable task = guiControlRegistry[index].runnable();
            if (elapseTime >= missionCompletionTime) {
                mission.freeMembers();
            }else{
                executor.schedule(()->{
                    task.run();
                    mission.freeMembers();
                    registeredMissions.remove(mission);
                    }, missionCompletionTime - elapseTime, TimeUnit.MILLISECONDS);
            }
        }
    }
}


