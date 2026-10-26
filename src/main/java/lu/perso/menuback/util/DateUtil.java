package lu.perso.menuback.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.time.Duration;

@Log4j2
@Component
public class DateUtil {

    public String formatUptime() {
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        Duration d = Duration.ofMillis(uptimeMs);
        long days = d.toDays();
        long hours = d.toHoursPart();
        long minutes = d.toMinutesPart();
        if (days > 0) return days + "j " + hours + "h " + minutes + "m";
        if (hours > 0) return hours + "h " + minutes + "m";
        return minutes + "m";
    }
}
