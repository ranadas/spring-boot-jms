package lankydan.tutorial.jms;

import lankydan.tutorial.documents.OrderTransaction;
import lankydan.tutorial.repositories.OrderTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.Session;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class OrderTransactionReceiver {

    @Autowired
    private OrderTransactionRepository transactionRepository;

    private AtomicInteger count;

    @JmsListener(destination = "OrderTransactionQueue", containerFactory = "myFactory")
    public void receiveMessage(
            @Payload OrderTransaction transaction,
            @Headers MessageHeaders headers,
            Message message, Session session) {

        log.info("<" + count.incrementAndGet() + "> Received <" + transaction + ">");
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("######          Message Details           #####");
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("headers: {}" , headers);
        log.info("message: {}" , message);
        log.info("session: {}" , session);
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");

        //System.out.println("<" + count + "> Received <" + transaction + ">");
        //count++;
        //    throw new RuntimeException();
        transactionRepository.save(transaction);
    }
}
