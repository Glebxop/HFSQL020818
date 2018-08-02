package com.example.master.hfsql020818;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.master.hfsql020818.DBHelper.DBHelper;


public class ListAdapter extends ListActivity implements AdapterView.OnItemClickListener {

    SQLiteDatabase db;
    Cursor cursor;
    public static final String KEY_NUMBER_ID = "NUMBER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = getListView();
        Intent intent=getIntent();
        int position=intent.getIntExtra(MainActivity.KEY_POSITION,0);
        try {
            DBHelper dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_NAME, new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION,DBHelper.COLUMN_KIND}, DBHelper.COLUMN_KIND+"=?", new String[]{String.valueOf(position)}, null, null, null);
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            listView.setAdapter(cursorAdapter);
            listView.setOnItemClickListener(this);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "DB unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    protected void onDestroy() {//Закрываем базу данных и курсор
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(ListAdapter.this,ShowActivity.class);
        intent.putExtra(KEY_NUMBER_ID,(int)id);
        startActivity(intent);
    }
}
