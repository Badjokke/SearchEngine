package com.example.ir_semestralka.utils;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
//wrapper class for java logger
public class Log {
    private Logger logger = Logger.getLogger(Log.class.getName());
    public Log(){
        logger.addHandler(new ConsoleHandler());
    }


    public void log(Level level, String message){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        logger.log(level,hour+":"+minute+":"+second+":"+year+"."+day+"."+month+" "+message+"\n");
    }

}
