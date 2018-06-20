package net.task.replicator.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "scheduler", ignoreUnknownFields = false)
public class SchedulerProperties {

    private String fixedRate;


    public String getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(String fixedRate) {
        this.fixedRate = fixedRate;
    }

}
