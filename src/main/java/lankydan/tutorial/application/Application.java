package lankydan.tutorial.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.ErrorHandler;

import javax.jms.ConnectionFactory;

//import org.omg.PortableInterceptor.ACTIVE;

@Slf4j
@EnableJms
@ComponentScan(basePackages = "lankydan.tutorial")
@EnableMongoRepositories(basePackages = "lankydan.tutorial")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Only required due to defining myFactory in the receiver
    @Bean
    public JmsListenerContainerFactory<?> myFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        // anonymous class
        factory.setErrorHandler(
                new ErrorHandler() {
                    @Override
                    public void handleError(Throwable t) {
                        log.error("An error has occurred in the transaction {}", t.toString());
                    }
                });

        // lambda function
        factory.setErrorHandler(t -> log.error("An error has occurred in the transaction {}", t.toString()));

        configurer.configure(factory, connectionFactory);
        return factory;
    }

    // Serialize message content to json using TextMessage
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
