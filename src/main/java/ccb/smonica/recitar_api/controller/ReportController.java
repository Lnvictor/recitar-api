package ccb.smonica.recitar_api.controller;

import ccb.smonica.recitar_api.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("reports")
@RequiredArgsConstructor
@Log4j2
public class ReportController {
    private final ReportService reportService;

    @PostMapping(value = "monthly", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> monthly(@RequestParam String year, @RequestParam String month) {
        log.info("Request received in monthly report with filters: year -> {}, month -> {}", year, month);
        return ResponseEntity.ok(this.reportService.generateMonthlyReport(year, month));
    }
}
