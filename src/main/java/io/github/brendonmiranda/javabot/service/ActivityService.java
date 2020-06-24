package io.github.brendonmiranda.javabot.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    public static final String DEFAULT_ACTIVITY_VALUE = "você";

    public static final Activity.ActivityType DEFAULT_ACTIVITY_TYPE = Activity.ActivityType.LISTENING;

    /**
     * Sets a personalized activity
     * @param activityType activity type
     * @param value value
     */
    public void setActivity(JDA jda, Activity.ActivityType activityType, String value) {
        jda.getPresence().setActivity(Activity.of(activityType, value));
    }

    /**
     * Sets the default activity
     */
    public void setActivityDefault(JDA jda) {
        jda.getPresence().setActivity(Activity.of(DEFAULT_ACTIVITY_TYPE, DEFAULT_ACTIVITY_VALUE));
    }

}
