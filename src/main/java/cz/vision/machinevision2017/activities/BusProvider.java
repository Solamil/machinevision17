package cz.vision.machinevision2017.activities;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Michal on 31.12.2017.
 */

public class BusProvider {
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance(){
        return BUS;
    }
}
