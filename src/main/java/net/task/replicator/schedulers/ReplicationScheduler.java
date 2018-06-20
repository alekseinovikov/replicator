package net.task.replicator.schedulers;

import net.task.replicator.services.ReplicationProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Used to call replication by the schedule
 */
@Component
public class ReplicationScheduler {

    private final ReplicationProcessor replicationProcessor;


    public ReplicationScheduler(ReplicationProcessor replicationProcessor) {
        this.replicationProcessor = replicationProcessor;
    }

    /**
     * Starts replication by the fixed rate from the config file
     */
    @Scheduled(fixedRateString = "#{schedulerProperties.fixedRate}")
    public void run() {
        replicationProcessor.startReplication();
    }

}
