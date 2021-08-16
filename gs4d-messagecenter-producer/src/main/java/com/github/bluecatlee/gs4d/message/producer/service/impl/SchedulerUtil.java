package com.github.bluecatlee.gs4d.message.producer.service.impl;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.Date;

public class SchedulerUtil {

    private static Scheduler scheduler;

    public static void scheduleJob(String jobName, String jobGroup, String triggerName, String triggerGroup, Class jobClass, String cronExpression) {
        try {
            if (scheduler == null) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            }

            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).usingJobData("series", jobName).build();
            TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(triggerName, triggerGroup);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
            CronTrigger trigger = (CronTrigger)triggerBuilder.build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void rescheduleJob(String jobName, String jobGroup, String triggerName, String triggerGroup, String cronExpression) {
        try {
            if (scheduler == null) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            if (trigger != null) {
                String currentCronExpression = trigger.getCronExpression();
                if (!currentCronExpression.equalsIgnoreCase(cronExpression)) {
                    TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
                    triggerBuilder.withIdentity(triggerName, triggerGroup);
                    triggerBuilder.startNow();
                    triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
                    trigger = (CronTrigger)triggerBuilder.build();
                    scheduler.rescheduleJob(triggerKey, trigger);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void cancelJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            if (scheduler == null) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断cron表达式相对于当前时间是否能触发
     * @param cronExpression
     * @return
     */
    public static boolean canTrigger(String cronExpression) {
        CronTriggerImpl trigger = new CronTriggerImpl();

        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime((Calendar)null);
            return date != null && date.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static void startScheduler() {
        try {
            if (scheduler == null) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            }

            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopScheduler() {
        try {
            if (scheduler == null) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            }

            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

