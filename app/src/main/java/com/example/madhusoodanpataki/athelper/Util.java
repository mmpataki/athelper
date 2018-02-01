package com.example.madhusoodanpataki.athelper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Madhusoodan Pataki on 1/26/2017.
 */
public class Util {

    static DateFormat format;
    static {
        format = new SimpleDateFormat(AConsts.dateFormat);
    }
    static Context con;

    public static void deleteFile(String fileName) {
        File inf = new File(con.getFilesDir(), fileName.trim());
        inf.delete();
    }

    public static void writeFile(String fileName, String sb) {
        try {
            FileOutputStream ostr = con.openFileOutput(fileName, Context.MODE_PRIVATE);
            ostr.write(sb.getBytes());
            ostr.flush();
            ostr.close();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    public static String readFile(String filename) {

        try {

            String line;
            StringBuilder sb = new StringBuilder();
            File inf = new File(con.getFilesDir(), filename.trim());

            BufferedReader bfd = new BufferedReader(new FileReader(inf));
            while((line = bfd.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } catch (IOException ex) {
            writeFile(filename, "");
        }
        return "";
    }

    public static Date readDate(String line) {
        try {
            return format.parse(line);
        } catch (ParseException ex) {
            Logger.getLogger(AttendanceSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static CharSequence toDateString(Date to) {
        return format.format(to);
    }
}
