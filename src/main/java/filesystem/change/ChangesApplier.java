package filesystem.change;

import javax.inject.Singleton;
import filesystem.change.local.LocalChangesHandler;
import filesystem.change.local.LocalChangesWatcher;
import filesystem.change.remote.RemoteChangesHandler;
import filesystem.change.remote.RemoteChangesWatcher;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: Ivan Lyutov
 * Date: 10/15/13
 * Time: 5:33 PM
 */
@Singleton
public class ChangesApplier {
    private static final Logger logger = Logger.getLogger(ChangesApplier.class);
    @Inject
    private LocalChangesHandler localChangesHandler;
    @Inject
    private RemoteChangesHandler remoteChangesHandler;
    @Inject
    private ScheduledExecutorService executorService;

    public void start() {
        logger.info("Trying to start ChangesApplier");
        executorService.scheduleWithFixedDelay(new MergeTask(), 0, 15, TimeUnit.SECONDS);
        logger.info("ChangesApplier has been successfully started");
    }

    class MergeTask implements Runnable {

        @Override
        public void run() {
            logger.info("Change merge iteration started");
            localChangesHandler.handle();
            remoteChangesHandler.handle();
            logger.info("Change merge iteration ended");
        }
    }
}
