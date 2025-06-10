package App.Util;

import App.Models.Mission.Mission;
import App.Models.Mission.MissionStatus;
import App.Types.MissionControlLink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class MissionTimerService {
    private static MissionTimerService instance;
    private final ScheduledExecutorService executor;
//    private final MissionControlLink[] guiControlRegistry;
    private final ConcurrentHashMap<Mission, Runnable> guiControlRegistry;
    private final ConcurrentHashMap<Mission, ScheduledFuture<?>> runningTasks;
    private final Set<Mission> registeredMissions;


    private MissionTimerService() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.guiControlRegistry = new ConcurrentHashMap<>();
        this.runningTasks = new ConcurrentHashMap<>();
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
            System.out.println("lock release. Gui ready");
            if (registeredMissions.size() > 0){
                System.out.printf("found: %s unfinished missions. Rebuilding...\n", registeredMissions.size());
                registeredMissions.forEach((m)->{
                    createTask(m);
                });
                System.out.printf("%s missions rebuilt\n", registeredMissions.size());
            }else{
                System.out.println("no missions to restore");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void registerMission(Mission mission) {
        registeredMissions.add(mission);
        createTask(mission);
    }

    private Runnable getControlTask(Mission mission) {
        return guiControlRegistry.get(mission);
    }


    public void setRegisterControlTask(Mission mission, Runnable callback) {
        this.guiControlRegistry.put(mission, callback);
    }

    public static void shutdown() {
        getInstance().executor.shutdown();
    }

    private void createTask(Mission mission){
        Runnable task = getControlTask(mission);
        long missionRemainingTime = calculateMissionRemainingTime(mission);
        if (task != null) {
            scheduleTask(missionRemainingTime, mission, task);
        }
    }

    public void scheduleTask(long missionRemainingTime, Mission mission, Runnable task){
        if (missionRemainingTime <= 0) {
            mission.freeMembers();
            task.run();
        }else{
            ScheduledFuture<?> schedule = executor.schedule(() -> {
                task.run();
                mission.freeMembers();
                registeredMissions.remove(mission);
                runningTasks.remove(mission);
                System.out.println("finished");
            }, missionRemainingTime, TimeUnit.MILLISECONDS);
            runningTasks.put(mission, schedule);
        }
    }

    public long calculateMissionRemainingTime(Mission mission){
        mission.getMissionCompletionTime();
        long missionCompletionTime = mission.getMissionCompletionTime();
        long startTime = mission.getStartTimeMillis();
        long currTime = System.currentTimeMillis();
        long elapseTime = currTime - startTime;
        return missionCompletionTime - elapseTime;
    }

    public void rescheduleMissions(){
        Map<Mission, ScheduledFuture<?>> snapshot = new HashMap<>(runningTasks);
        if (snapshot.size() > 0){
            System.out.printf("Reschedule called for: %s tasks\n", snapshot.size());
            snapshot.forEach((k,v) -> {
                long previousMissionCompletionTime = k.getMissionCompletionTime();
                k.getMissionCompletionTimeForced();
                long newMissionCompletionTime = k.getMissionCompletionTime();
                double scaledBy = (double)newMissionCompletionTime / previousMissionCompletionTime;

                long remainingTime = v.getDelay(TimeUnit.MILLISECONDS);
                v.cancel(true);

                long newRemainingTime = (long)(remainingTime * scaledBy);
                scheduleTask(newRemainingTime, k, guiControlRegistry.get(k));
            });
        }
    }
}