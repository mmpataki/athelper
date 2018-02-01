package com.example.madhusoodanpataki.athelper;

/**
 * Created by Madhusoodan Pataki on 1/26/2017.
 */

import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AttendanceSheetListEntry {

    static File contextDir;


    String title;
    String fileName;

    public static void addToList(String title, String path) {
        Util.writeFile(AConsts.attendanceFilesList, encode(title, path) + Util.readFile(AConsts.attendanceFilesList));
    }

    public static String encode(String title, String path) {
        return (title + '`' + path + "\n");
    }
    public static String encode(AttendanceSheetListEntry asle) {
        return (asle.title + '`' + asle.fileName + "\n");
    }

    public static String[] decode(String estr) {
        return estr.split("`");
    }

    public static AttendanceSheetListEntry[] getAttendanceSheetList() {

        String ret = Util.readFile(AConsts.attendanceFilesList);

        if(ret.trim().equals("")) {
            return new AttendanceSheetListEntry[0];
        }

        String[] arr = ret.split("\n");
        AttendanceSheetListEntry[] asle = new AttendanceSheetListEntry[arr.length];

        int i = 0;
        for (String s : arr) {
            asle[i] = new AttendanceSheetListEntry();
            asle[i].title = s.split("`")[0];
            asle[i].fileName = s.split("`")[1];
            i++;
        }
        return asle;
    }

    public static void remove(AttendanceSheetListEntry sheet) {
        File f = new File(sheet.fileName);
        f.delete();
        String fcnt = Util.readFile(AConsts.attendanceFilesList), twrt = "";
        String[] shts = fcnt.split("\n");
        for (String sht : shts) {
            if(sht.split("`")[0].equals(sheet.title)) {
                sht = "";
            }
            twrt += sht + "\n";
        }
        Util.writeFile(AConsts.attendanceFilesList, twrt);
    }
}
