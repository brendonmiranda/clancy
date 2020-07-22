package io.github.brendonmiranda.bot.clancy.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author brendonmiranda
 */
@Configuration
public class RabbitConfiguration {

	@Value("${rabbit.hostname}")
	private String hostname;

	@Value("${rabbit.username}")
	private String username;

	@Value("${rabbit.password}")
	private String password;

	@Value("${rabbit.virtualhost}")
	private String virtualHost;

	@Value("${rabbit.connection.timeout}")
	private int connectionTimeout;

	@Value("${rabbit.requested.heartbeat}")
	private int requestedHeartBeat;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(virtualHost);
		connectionFactory.setConnectionTimeout(connectionTimeout);
		connectionFactory.setRequestedHeartBeat(requestedHeartBeat);
		return connectionFactory;
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
