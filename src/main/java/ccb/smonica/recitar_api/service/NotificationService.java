package ccb.smonica.recitar_api.service;

import ccb.smonica.recitar_api.dto.ReportDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
    private RabbitTemplate rabbitTemplate;

    public void notificateEmailSender(ReportDTO dto) {
        this.rabbitTemplate.convertAndSend("recitar_report.email_sender", dto);
    }
}
