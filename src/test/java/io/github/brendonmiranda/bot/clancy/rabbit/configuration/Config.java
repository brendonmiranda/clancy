package io.github.brendonmiranda.bot.clancy.rabbit.configuration;

import io.github.brendonmiranda.bot.clancy.configuration.RabbitConfiguration;
import io.github.brendonmiranda.bot.clancy.rabbit.listener.Listener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurations class for testing rabbit-mq implementations. This is a complement of
 * {@link RabbitConfiguration} class for testing.
 *
 * @author brendonmiranda
 */
@Configuration
@RabbitListenerTest
public class Config {

	@Bean
	public Queue myQueue() {
		return new Queue("bar");
	}

	@Bean
	public Listener listener() {
		return new Listener();
	}

}
