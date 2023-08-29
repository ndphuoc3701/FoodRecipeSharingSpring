package com.hcmut.dacn.request;

import com.hcmut.dacn.dto.ScheduleRecipeTimerDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.TimerTask;

public class ScheduleRecipeTimer extends TimerTask {
    private final Long userId;
    private final ScheduleRecipeTimerDto scheduleRecipeTimerDto;
    //    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(ScheduleRecipeTimerDto scheduleRecipeTimerDto, Long userId) {
        String address = "/queue/" + userId + "/notification";
        simpMessagingTemplate.convertAndSend(address, scheduleRecipeTimerDto);
    }

    public ScheduleRecipeTimer(ScheduleRecipeTimerDto scheduleRecipeTimerDto, Long userId, SimpMessagingTemplate simpMessagingTemplate) {
        this.userId = userId;
        this.scheduleRecipeTimerDto = scheduleRecipeTimerDto;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void run() {
        sendNotification(scheduleRecipeTimerDto, userId);
    }
}
