package io.github.brendonmiranda.javabot.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class QueueService<O, M> {

    @Autowired
    protected RabbitAdmin rabbitAdmin;

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    public abstract void enqueue(String routingKey, O object);

    public abstract M receive(String queueName);

    public void destroy(String queueName) {
        rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * Checks if a queue exists. If the queue doesn't exist it tries to create it.
     * @param queueName
     * @return
     */
    protected boolean hasQueue(String queueName) {
        // getQueueProperties() can be used to determine if a queue exists on the broker
        if (rabbitAdmin.getQueueProperties(queueName) == null)
            return createQueue(queueName) == null ? false : true;
        else
            return true;
    }

    /**
     * Creates a queue on the broker and returns the queue name if successful, otherwise
     * it returns null.
     * @return queueName
     */
    protected String createQueue(String queueName) {
        return rabbitAdmin.declareQueue(new Queue(queueName));
    }

}
