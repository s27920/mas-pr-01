package App.Util;

import java.util.concurrent.CountDownLatch;

public class MissionMarkerCreationCountDownLatch {
    private static CountDownLatch countDownLatch;

    private MissionMarkerCreationCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    public static CountDownLatch getInstance(){
        if (countDownLatch == null){
            countDownLatch = new CountDownLatch(1);
        }
        return countDownLatch;
    }
}
