package com.example.madhusoodanpataki.athelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listItems;
    AttendanceSheetListEntry[] sheets;
    int selectedIndex = -1;
    String addedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.con = this.getApplicationContext();

        listView = (ListView)findViewById(R.id.AttendanceSheetList);

        loadList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
            }
        });

        (findViewById(R.id.AddButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButtonClicked();
            }
        });

        (findViewById(R.id.OpenButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openButtonClicked();
            }
        });

        (findViewById(R.id.DeleteButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButtonClicked();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(addedItem != null) {
            listItems.add(addedItem);
            addedItem = null;
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

        String ret = data.getStringExtra(AConsts.exchangedString);

        if(ret != null && !ret.equals("")) {
            addedItem = ret.split("`")[0];
        }
    }

    private void loadList() {

        sheets = AttendanceSheetListEntry.getAttendanceSheetList();
        listItems = new ArrayList<>(sheets.length);

        String[] titleList = new String[sheets.length];

        int i = 0;
        for (AttendanceSheetListEntry asle: sheets) {
            listItems.add(asle.title);
            titleList[i++] = asle.title;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_item_template, listItems);
        listView.setAdapter(adapter);
    }

    private void addButtonClicked() {
        Intent intent = new Intent(this, AddSheet.class);
        startActivityForResult(intent, 1);
    }

    private void openButtonClicked() {

        if(selectedIndex == -1) {
            return;
        }

        Intent intent = new Intent(this, TakeAttendance.class);
        AttendanceSheetListEntry asle = sheets[selectedIndex];
        intent.putExtra(AConsts.exchangedString, AttendanceSheetListEntry.encode(asle));
        startActivity(intent);
    }

    private void deleteButtonClicked() {

        if(selectedIndex == -1) {
            return;
        }

        AttendanceSheetListEntry.remove(sheets[selectedIndex]);
        listView.removeViewAt(selectedIndex);
    }
}
