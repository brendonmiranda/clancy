package io.github.brendonmiranda.javabot.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author brendonmiranda
 */
@Configuration
public class RabbitConfiguration {

	private static final String LOCALHOST = "localhost";

	/*
	 * todo: Evaluate strategy for scaling. Read that section -> (Connection and Resource
	 * Management)
	 * https://docs.spring.io/spring-amqp/docs/2.2.7.RELEASE/reference/html/#connections
	 * For now, it should be simple in order to execute the prove of concept
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		return new CachingConnectionFactory(LOCALHOST);
	}

	/**
	 * We are not declaring {@link Exchange} given that {@link RabbitAdmin} has a default
	 * exchange {@link RabbitAdmin#DEFAULT_EXCHANGE_NAME} which is a direct exchange.
	 *
	 * Also, we are not declaring {@link Binding} given that the default one suits (thus,
	 * we can use the queue name as a routing key in the send).
	 */
	@Bean
	public RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	/**
	 * Message converter to override the usage of the SimpleMessageConverter default.
	 */
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}
