package ccb.smonica.recitar_api.tasks;

import ccb.smonica.recitar_api.service.ReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Log4j2
public class ReportTasks {
    @Autowired
    private ReportService reportService;

    /**
     * This Method generates reports monthly
     *
     * Pesquisar depois como agendar uma vez no mes so
     */
    @Scheduled(fixedRate = 10000)
    public void report() {
        log.info("===================Reporting tasks===============");
        LocalDate now = LocalDate.now();
        reportService.generateMonthlyReport(String.valueOf(now.getYear()), "07");
    }
}
