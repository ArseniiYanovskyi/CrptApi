package ru.crpt.ismp.CrptApi;

import lombok.Data;

@Data
public class TimeUnit {
    private long days;
    private long hours;
    private long minutes;
    private long second;

    public long getSeconds() {
        long result = 0L;
        if (days != 0 && days > 0) {
            result += days*24*60*60;
        }
        if (hours != 0 && hours > 0) {
            result += hours*60*60;
        }
        if (minutes != 0 && minutes > 0) {
            result += minutes*60;
        }
        return result + second;
    }
}
