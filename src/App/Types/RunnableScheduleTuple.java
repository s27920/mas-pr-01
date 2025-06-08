package App.Types;

import java.util.concurrent.ScheduledFuture;

public record RunnableScheduleTuple(ScheduledFuture<?> taskRegister, Runnable task){ }
