package com.example.madhusoodanpataki.athelper;

/**
 * Created by Madhusoodan Pataki on 1/26/2017.
 *
 * This file contains all the methods to manipulate a attendance sheet
 */

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.max;


/*
 * Format of the sheet is as follows
 *


    1  . Name_Of_AttendanceTaker
    2  . title
    3  . From
    4  . To
    5  . NumTimes
    6  . NumEntries
    7+0.
    7+1.
    7+2. DateTaken
    ..
    7+n.
    8+0.
    8+1.
    8+2. AttendanceEntry
    ...
    8+k.

*/


public class AttendanceSheet {

    private boolean read = false;
    String fileName;
    String attTaker;
    String title;
    Date from, to;
    private int[] tempList;
    private int completed;
    ArrayList<Date> classDates;
    ArrayList<AttendanceEntry> attEntries;

    /*
     * Cursors !
     */
    private Integer cursor;
    private AttendanceEntry currentSerial;
    private HashMap<String, Integer> hMap;

    /* some constants */
    static final int ATT_PRESENT = 1;
    static final int ATT_ABSENT = 0;

    public AttendanceSheet(String fileName) {

        classDates = new ArrayList<>();
        attEntries = new ArrayList<>();
        hMap = new HashMap<>();
        this.fileName = fileName;
    }

    public int moveCursor(int pos) {
        if(pos < attEntries.size() && pos > -1) {
            cursor = pos;
            currentSerial = attEntries.get(pos);
        }
        return cursor;
    }

    public int moveCursor(String tosearch) {
        Integer pos = hMap.get(tosearch);
        if(pos != null) {
            cursor = pos;
            currentSerial = attEntries.get(pos);
        }
        return cursor;
    }

    public AttendanceEntry getCurrentEntry() {
        return currentSerial;
    }

    public int incrementCursor() {
        cursor = (++cursor) % attEntries.size();
        currentSerial = attEntries.get(cursor);
        return cursor;
    }

    public int decrementCursor() {
        cursor = (--cursor) % attEntries.size();
        currentSerial = attEntries.get(cursor);
        return cursor;
    }



    public int createSheet(String serExp) {
        ArrayList<String> sList = getSerialNumbers(serExp);

        if(sList == null) {
            return -1;  // Error in expression.
        }

        for(String s: sList) {
            attEntries.add(new AttendanceEntry(s + ":0-0"));
        }
        saveSheet();
        AttendanceSheetListEntry.addToList(title, fileName);
        return 0;
    }

    public void saveAttendance() {
        if (completed != attEntries.size()) {
            return;
        }
        addNewAttendanceColumn();
        saveSheet();
    }

    public void saveSheet() {

        classDates.add(new Date());

        StringBuilder sb = new StringBuilder();

        sb.append(attTaker + "\n");
        sb.append(title + "\n");
        sb.append(Util.toDateString(from) + "\n");
        sb.append(Util.toDateString(to) + "\n");
        sb.append(classDates.size() + "\n");
        sb.append(attEntries.size() + "\n");

        for (int i = 0; i < classDates.size(); i++) {
            sb.append(Util.toDateString(classDates.get(i)) + "\n");
        }
        for (int i = 0; i < attEntries.size(); i++) {
            sb.append(attEntries.get(i).toString() + "\n");
        }

        Util.deleteFile(fileName);
        Util.writeFile(fileName, sb.toString());
    }

    public void readSheet() {

        if (read)
            return;
        read = true;

        int lineno = 0, numTimes = 0, numEntries = 0;

        String line;
        String[] lines = Util.readFile(fileName).split("\n");

        for (int j = 0; j < lines.length; j++) {

            line = lines[j];

            switch (++lineno) {
                case 1:
                    attTaker = line;
                    break;
                case 2:
                    title = line;
                    break;
                case 3:
                    from = Util.readDate(line);
                    break;
                case 4:
                    to = Util.readDate(line);
                    break;
                case 5:
                    numTimes = Integer.parseInt(line);
                    break;
                case 6:
                    numEntries = Integer.parseInt(line);
                    break;
                case 7:
                    for (int i = 0; i < numTimes; i++) {
                        if (j < lines.length) {
                            line = lines[j++];
                            classDates.add(Util.readDate(line));
                        }
                    }
                    for (int i = 0; i < numEntries; i++) {
                        if (j < lines.length) {
                            line = lines[j++];
                            attEntries.add(new AttendanceEntry(line));
                        }
                    }
            }
        }
    }

    private static int imax(int a, int b) {
        return (a > b) ? a : b;
    }

    public void setCurAttendance(int value) {
        if (tempList[cursor] == -1) {
            completed++;
        }
        tempList[cursor] = value;
        incrementCursor();
    }

    public void initAttendanceState() {

        readSheet();
        tempList = new int[attEntries.size()];

        for (int i = 0; i < tempList.length; i++) {
            tempList[i] = -1; //not yet taken
        }
        completed = 0;
        cursor = 0;
    }

    public void addNewAttendanceColumn() {
        for (int i = 0; i < tempList.length; i++) {
            attEntries.get(i).addAttendance(tempList[i]);
        }
    }

    public static ArrayList<String> getSerialNumbers(String serExpr) {

        ArrayList<String> output = new ArrayList<>();
        String chunks[];

        serExpr = serExpr.replaceAll("[\t\n\r ]", "");
        chunks = serExpr.split(",");

        for (String chunk : chunks) {

            if ("".equals(chunk)) {
                continue;
            }

            String hsplits[] = chunk.split("-");

            if (hsplits.length > 1) {
                /* it's a series decode it */

                String csubexp = "";
                int from, to, begin, csubexplen;

                /* find the common subexpression */
                for (begin = 0; begin < hsplits[0].length(); begin++) {
                    if(hsplits[0].charAt(begin) != hsplits[1].charAt(begin)) {
                        break;
                    }
                    csubexp += hsplits[0].charAt(begin);
                }

                csubexplen = imax(hsplits[0].length(), hsplits[1].length()) - csubexp.length();

                try {

                    String s1 = hsplits[0].substring(begin);
                    String s2 = hsplits[1].substring(begin);

                    System.out.println(s1 + " " + s2);

                    from = Integer.parseInt(s1);
                    to = Integer.parseInt(s2);

                    if(from >= to) {
                        from -= to; //shut compilers mouth
                        return null;
                    }

                } catch(Exception e) {
                    return null;
                }

                for (int i = from; i <= to; i++) {

                    String toins = "" + i;
                    while(toins.length() != csubexplen)
                        toins = "0" + toins;

                    output.add(csubexp + toins);
                }
            } else {
                output.add(hsplits[0]);
            }
        }
        return output;
    }

    public static int getNumDays(Date d1, Date d2) {
        final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
        return (int) ((d1.getTime() - d2.getTime()) / DAY_IN_MILLIS);
    }

    public String gen_html() {

        String ret = "", temp;
        Calendar c = new GregorianCalendar();
        String dayOfMonth = String.format(AConsts.dayOfMonth, c.get(Calendar.DAY_OF_MONTH));

        StringBuilder tcontent;

        int numDays = getNumDays(to, from);

        for (int i = 0; i < attEntries.size(); i += AConsts.numRecordsPerPage) {
            tcontent = new StringBuilder();
            for (int j = i; j < (i + AConsts.numRecordsPerPage); j++) {


                for (int k = 0; k < numDays; k += 31) {
                    String attLine = "";
                    for (int l = k; l < k+31; l++) {
                        try { attLine += (attEntries.get(j).attendance.get(l) == 1) ? AConsts.attPresentTemplate : AConsts.attAbsentTemplate; }
                        catch(Exception e) {attLine += AConsts.attAbsentTemplate; }
                    }

                    /* build a line */
                    String lineRecord;
                    try { lineRecord = String.format(AConsts.attEntryTemplate, attEntries.get(j).serialNum, attLine); }
                    catch(Exception e) {lineRecord = "";}

                    /* add it to content */
                    tcontent.append("<tr>").append(lineRecord).append("</tr>");
                }
            }
            ret += String.format(AConsts.tableTemplate, title, attTaker, from, to, "", "", tcontent.toString());
        }

        return String.format(AConsts.sheetTemplate, title, ret);
    }
}

