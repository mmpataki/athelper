package com.example.madhusoodanpataki.athelper;

/**
 * Created by Madhusoodan Pataki on 1/26/2017.
 */

import java.util.ArrayList;

public class AttendanceEntry {

    String serialNum;
    private int presents;
    ArrayList<Integer>attendance;

    public AttendanceEntry(String entryLine) {

        presents = 0;

        this.attendance = new ArrayList<>();

        entryLine = entryLine.replaceAll("[\t\n ]", "");

        String splits[] = entryLine.split(":");
        this.serialNum = splits[0];

        if(splits.length < 2)
            return;

        String groups[] = splits[1].split(",");

        for (int i = 0; i < groups.length; i++) {

            int type = Integer.parseInt(groups[i].split("-")[0]);
            int count = Integer.parseInt(groups[i].split("-")[1]);

            for (int j = 0; j < count; j++) {
                this.attendance.add(type);
                if(type == 1) {
                    presents++;
                }
            }
        }
    }

    public int getPresents() {
        return presents;
    }

    @Override
    public String toString() {

        String ret = serialNum + ":";

        if(attendance.isEmpty())
            return ret;

        int bit = attendance.get(0);
        int count = 0;
        for (int i = 0; i < attendance.size(); i++) {
            if(bit != attendance.get(i)) {
                ret += bit + "-" + count + ",";
                count = 1;
                bit = attendance.get(i);
            } else {
                count++;
            }
        }
        ret += bit + "-" + count;
        return ret;
    }

    public void addAttendance(int value) {
        attendance.add(value);
    }
}
