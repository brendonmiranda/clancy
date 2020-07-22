package io.github.brendonmiranda.bot.clancy.rabbit.configuration;

import io.github.brendonmiranda.bot.clancy.rabbit.listener.Listener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
