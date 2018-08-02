package com.example.master.hfsql020818;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.master.hfsql020818.DBHelper.DBHelper;

public class ShowActivity extends AppCompatActivity {

    int numberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent intent = getIntent();
        numberId = intent.getIntExtra(ListAdapter.KEY_NUMBER_ID, 0);
        new AssyncShow().execute(numberId);

    }

    public void onClickFavorite(View view) {
        new Assync().execute(numberId);//запускаем асинхронное добавление фаворит

    }

    private class Assync extends AsyncTask<Integer, Void, Boolean> {//асинхронное выполнение

        ContentValues contentValues;

        @Override
        protected void onPreExecute() {//выполняется первым в главном потоке
            super.onPreExecute();
            CheckBox checkBoxFavor = (CheckBox) findViewById(R.id.checkBoxFavor);
            contentValues = new ContentValues();
            contentValues.put(DBHelper.COLUMN_FAVORITE, checkBoxFavor.isChecked());
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {//в конце
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                Toast toast = Toast.makeText(ShowActivity.this, "DB unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {//в новом потоке между остальными
            int number = integers[0];
            try {
                DBHelper dbHelper = new DBHelper(ShowActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                db.update(DBHelper.TABLE_NAME, contentValues, DBHelper.COLUMN_ID + "= ?", new String[]{String.valueOf(number)});
                db.close();
                return true;
            } catch (Exception e) {
                return false;
            }

        }
    }

    private class AssyncShow extends AsyncTask<Integer, Void, Boolean> {


        int dravNumber;
        String nameDrink;
        String deskr;
        boolean favor;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                TextView textName = (TextView) findViewById(R.id.textViewShowActName);
                TextView textDiscr = (TextView) findViewById(R.id.textViewShowActDescr);
                ImageView imShow = (ImageView) findViewById(R.id.imageVievShowActiv);
                CheckBox checkBoxShow = (CheckBox) findViewById(R.id.checkBoxFavor);
                imShow.setImageResource(dravNumber);
                checkBoxShow.setChecked(favor);
                textName.setText(nameDrink);
                textDiscr.setText(deskr);
            }
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int numberId = integers[0];
            try {
                DBHelper dbHelper = new DBHelper(ShowActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query(DBHelper.TABLE_NAME, new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION, DBHelper.COLUMN_INT_DRAV, DBHelper.COLUMN_FAVORITE}, DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(numberId)}, null, null, null, null);
                if (cursor.moveToFirst()) {
                    nameDrink = cursor.getString(1);
                    deskr = cursor.getString(2);
                    dravNumber = cursor.getInt(3);
                    favor = (cursor.getInt(4) == 1);
                }
                cursor.close();
                db.close();
                return true;
            } catch (Exception e) {
                return false;
            }

        }
    }
}
