package com.example.house;
import com.example.house.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class TimerTask {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 每天凌晨执行一次
     * 更新订单状态，默认过期的订单都已完成
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledTask(){
        appointmentService.updateAppointmentStatusEveryday();
    }
}
