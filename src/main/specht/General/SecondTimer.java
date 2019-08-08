package specht.General;

import java.util.Date;
import java.util.TimerTask;

public class SecondTimer extends TimerTask {

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    long seconds;



    @Override
    public void run() {
        Date test = new Date();
        setSeconds(test.getTime()/1000);
        test.getSeconds();

    }
}
