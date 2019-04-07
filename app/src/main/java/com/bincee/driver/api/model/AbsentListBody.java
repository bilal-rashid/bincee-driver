package com.bincee.driver.api.model;

import java.util.ArrayList;
import java.util.List;

public class AbsentListBody {

    public AbsentListBody(int driver_id, int shifts) {
        this.driver_id = driver_id;
        this.shifts = new ArrayList<>();
        this.shifts.add(shifts);
    }

    public int driver_id;
    public List<Integer> shifts = new ArrayList<>();

}
