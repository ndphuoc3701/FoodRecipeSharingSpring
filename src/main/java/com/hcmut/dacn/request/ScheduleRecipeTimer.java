package com.hcmut.dacn.request;

import com.hcmut.dacn.dto.ScheduleRecipeTimerDto;
import com.hcmut.dacn.repository.ScheduleRecipeRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.TimerTask;

public class ScheduleRecipeTimer extends TimerTask {
    private final Long userId;
    private final ScheduleRecipeTimerDto scheduleRecipeTimerDto;
    private final ScheduleRecipeRepository scheduleRecipeRepository;
    //    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

//    public void sendNotification() {
//
//
//    }

    public ScheduleRecipeTimer(ScheduleRecipeTimerDto scheduleRecipeTimerDto, Long userId, SimpMessagingTemplate simpMessagingTemplate,ScheduleRecipeRepository scheduleRecipeRepository) {
        this.userId = userId;
        this.scheduleRecipeTimerDto = scheduleRecipeTimerDto;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.scheduleRecipeRepository=scheduleRecipeRepository;
    }

    @Override
    public void run() {
        if(scheduleRecipeRepository.findByUser_IdAndRecipe_Id(userId,scheduleRecipeTimerDto.getId())!=null){
            String address = "/queue/" + userId + "/notification";
            simpMessagingTemplate.convertAndSend(address, scheduleRecipeTimerDto);
        }
    }
}
