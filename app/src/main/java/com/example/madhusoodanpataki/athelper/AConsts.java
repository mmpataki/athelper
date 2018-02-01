package com.example.madhusoodanpataki.athelper;

/**
 * Created by Madhusoodan Pataki on 1/26/2017.
 */

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Madhusoodan Pataki
 */
class AConsts {

    static String dateFormat = "mm/dd/yyyy";

    static String sheetTemplate
            =
            "<html>\n"
                    + "<head>\n"
                    + "<title>%s</title>"
                    + "<style>\n"
                    + "table {\n"
                    + "	border-collapse: collapse;\n"
                    + "	width : 842px;\n"
                    + "}\n"
                    + "table, th, td, caption {\n"
                    + "    border: 1px solid black;\n"
                    + "}\n"
                    + "td {\n"
                    + "	width: 25px;\n"
                    + "	text-align: center;\n"
                    + "}\n"
                    + "td#daterange {\n"
                    + "	width: 25px;\n"
                    + "	text-align: center;\n"
                    + "	height: 30px;\n"
                    + "}\n"
                    + "td#entry {\n"
                    + "	padding: 0px 10px;\n"
                    + "	text-align: center;\n"
                    + "}\n"
                    + "caption {\n"
                    + "	height: 40px;\n"
                    + "	text-align: center;\n"
                    + "	line-height: 40px;\n"
                    + "}\n"
                    + "</style>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "%s"
                    + "</body></html>"
            ;

    static String tableTemplate =

            "<table>\n"
                    + "<thead>\n"
                    + "<caption><b>"
                    + "%s"
                    + //Title
                    "<b></caption>\n"
                    + "</thead>\n"
                    + "<tr>\n"
                    + "<td colspan=\"32\" style=\"text-align: left; padding: 3px 10px;\">\n"
                    + "%s."
                    + //attTakerName
                    "</td>\n"
                    + "</tr>\n"
                    + "<tr>\n"
                    + "<td></td>\n"
                    + "<td colspan=\"15\" id=\"daterange\">From : %s</td>\n"
                    + //From
                    "<td colspan=\"16\" id=\"daterange\">To : %s</td>\n"
                    + //To
                    "</tr>\n"
                    + "<tr>"
                    + "%s"
                    + //Day of Month
                    "</tr>"
                    + "<tr>"
                    + "%s"
                    + //Class
                    "</tr>"
                    + "%s"
                    + //attEntries
                    "</table>\n";

    static String dayOfMonth = "<td><b>%d</b></td>\n";
    static String classNum = "<td><i>%d</b></i>\n";
    static String attEntryTemplate = "<td id=\"entry\" >%s</td>%s";
    static String attPresentTemplate = "<td>&bull;</td>";
    static String attAbsentTemplate = "<td>  </td>";
    static int numRecordsPerPage = 40;
    static String attendanceFilesList = "AttendanceList.list";
    static String exchangedString = "SheetName";
}

