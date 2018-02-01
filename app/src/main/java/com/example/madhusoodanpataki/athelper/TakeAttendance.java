package com.example.madhusoodanpataki.athelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TakeAttendance extends AppCompatActivity {

    EditText SearchBox;
    AttendanceSheet sheet;
    TextView CurrentSerial, Statistics;

    AttendanceSheet currentSheet;

    private void initAttendance(String encoded_asle) {

        String asle[] = AttendanceSheetListEntry.decode(encoded_asle);

        currentSheet = new AttendanceSheet(asle[1]);
        currentSheet.readSheet();

        currentSheet.initAttendanceState();
        setEntryLabel(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        SearchBox = (EditText) findViewById(R.id.SearchBox);
        CurrentSerial = (TextView) findViewById(R.id.CurrentSerial);

        (findViewById(R.id.PresentButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSheet.setCurAttendance(AttendanceSheet.ATT_PRESENT);
                setEntryLabel();
            }
        });
        (findViewById(R.id.AbsentButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSheet.setCurAttendance(AttendanceSheet.ATT_ABSENT);
                setEntryLabel();
            }
        });

        (findViewById(R.id.SearchButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });

        (findViewById(R.id.SaveAttendance)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
            }
        });

        (findViewById(R.id.ExportButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportButtonClicked();
            }
        });

        Statistics = (TextView)findViewById(R.id.Statistics);

        ((EditText)findViewById(R.id.SearchBox)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* do nothing */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* do nothing */
            }

            @Override
            public void afterTextChanged(Editable s) {
                setEntryLabel(currentSheet.moveCursor(s.toString()));
            }
        });

        initAttendance(this.getIntent().getStringExtra(AConsts.exchangedString));
    }

    private void searchButtonClicked() {
        int vis = (SearchBox.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        SearchBox.setVisibility(vis);
    }

    private void saveButtonClicked() {
        currentSheet.saveAttendance();
        finish();
    }

    private void exportButtonClicked() {

        String output = sheet.gen_html();

        /* write to the file here. */

        /* convert to PDF here */
    }

    private void setLabel() {

        AttendanceEntry ent = currentSheet.getCurrentEntry();

        if(ent == null) {
            return;
        }

        int p = ent.getPresents();
        int t = currentSheet.classDates.size();
        double percent = (t == 0) ? 0.0 : ((double)p / t);

        CurrentSerial.setText(ent.serialNum);
        Statistics.setText(p + "/" + t + " " + percent + "%");
    }

    private void setEntryLabel(int i) {
        currentSheet.moveCursor(i);
        setLabel();
    }

    private void setEntryLabel() {
        setLabel();
    }
}
