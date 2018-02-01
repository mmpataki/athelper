package com.example.madhusoodanpataki.athelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddSheet extends AppCompatActivity {

    EditText SheetName, UserName, FromDate, ToDate, SerialExpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sheet);

        Button SaveButton = (Button) findViewById(R.id.SaveButton);
        SheetName = (EditText) findViewById(R.id.SheetName);
        UserName = (EditText) findViewById(R.id.UserName);
        FromDate = (EditText) findViewById(R.id.FromDate);
        ToDate = (EditText) findViewById(R.id.ToDate);
        SerialExpression = (EditText) findViewById(R.id.SeriesExpression);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSheet();
            }
        });
    }

    private void addSheet() {

        AttendanceSheet sheet = new AttendanceSheet(SheetName.getText() + ".att");
        sheet.title = SheetName.getText().toString();
        sheet.from = Util.readDate(FromDate.getText().toString());
        sheet.to = Util.readDate(ToDate.getText().toString());
        sheet.attTaker = UserName.getText().toString();

        int status = sheet.createSheet(SerialExpression.getText().toString());

        if(status == -1) {
            /* show the error message. */
            Toast.makeText(AddSheet.this, "Error in the serial expression", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddSheet.this, "Sheet created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(AConsts.exchangedString, sheet.title + "`" + sheet.fileName);
            setResult(0, intent);
            finish();
        }
    }
}
