package specht.General;

import java.util.Timer;

public class SzenarioTwo {

    int GameID = 1;
    int Second = 0;

    public void go() {
        Timer t = new Timer();

        t.schedule(new SzenarioTwoTimer(GameID, Second), 0, 5000);


    }

}
