package com.example.master.hfsql020818;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.master.hfsql020818.DBHelper.DBHelper;

public class MainActivity extends AppCompatActivity {


    public static final String KEY_POSITION = "POSITION";
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mListViewMenu = (ListView) findViewById(R.id.listVievMainMenu);
        AdapterView.OnItemClickListener listVievMenu = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListAdapter.class);
                intent.putExtra(KEY_POSITION, position);
                startActivity(intent);
            }
        };
        mListViewMenu.setOnItemClickListener(listVievMenu);
        fillListViewFawor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    protected void onResume() {//обновляем курсор
        super.onResume();

        DBHelper dbHelper = new DBHelper(MainActivity.this);
        db = dbHelper.getReadableDatabase();
        Cursor newCursor = db.query(DBHelper.TABLE_NAME, new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_FAVORITE}, DBHelper.COLUMN_FAVORITE + "=?", new String[]{String.valueOf(1)}, null, null, null);
        ListView listView = (ListView) findViewById(R.id.listVievMainFavorite);
        CursorAdapter cursorAdapter = (CursorAdapter) listView.getAdapter();
        cursorAdapter.changeCursor(newCursor);
        cursor = newCursor;
    }

    void fillListViewFawor() {//Заполняем лист виев фаворит
        ListView listView = (ListView) findViewById(R.id.listVievMainFavorite);
        try {
            DBHelper dbHelper = new DBHelper(MainActivity.this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_NAME, new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_FAVORITE}, DBHelper.COLUMN_FAVORITE + "=?", new String[]{String.valueOf(1)}, null, null, null);
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cursor, new String[]{DBHelper.COLUMN_NAME}, new int[]{android.R.id.text1}, 0);
            listView.setAdapter(cursorAdapter);
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                    intent.putExtra(ListAdapter.KEY_NUMBER_ID, (int) id);
                    startActivity(intent);
                }
            };
            listView.setOnItemClickListener(onItemClickListener);
        } catch (Exception e) {
        }
    }


}
