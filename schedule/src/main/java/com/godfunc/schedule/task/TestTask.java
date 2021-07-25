package com.godfunc.schedule.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestTask {

    public Integer up(Long id, String status) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + id + " " + status);
        return 1;
    }
}
