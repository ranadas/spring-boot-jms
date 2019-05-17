package lankydan.tutorial.rest;

import lankydan.tutorial.documents.OrderTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class OrderTransactionController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/send")
    public void send(@RequestBody OrderTransaction transaction) {
        log.info("Sending a transaction. {} ", transaction.toString());
        jmsTemplate.convertAndSend(
                "OrderTransactionQueue", transaction);
    }
}
