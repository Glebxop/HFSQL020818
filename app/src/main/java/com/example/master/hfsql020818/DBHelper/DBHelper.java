package com.example.master.hfsql020818.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.master.hfsql020818.R;

public class DBHelper extends SQLiteOpenHelper {

   public static final int VERSION_DB=2;
   public static final String DB_NAME="mydb";
   public static final String TABLE_NAME="drinks";
   public static final String COLUMN_ID="_id";
   public static final String COLUMN_NAME="name";
   public static final String COLUMN_DESCRIPTION ="description";
   public static final String COLUMN_INT_DRAV="intdrav";
   public static final String COLUMN_FAVORITE="favorite";
   public static final String COLUMN_KIND ="kind";
   /* static final String
    static final String*/


    public DBHelper(Context context) {
        super(context, DB_NAME,null,VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createOrUpdate(0,VERSION_DB,db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createOrUpdate(oldVersion,newVersion,db);
    }

   private void createOrUpdate(int oldVersion,int newVersion,SQLiteDatabase db){
        if (oldVersion==0){
        String sql1="Create table "+TABLE_NAME+" ( "+
                COLUMN_ID+" integer primary key autoincrement, "+
                COLUMN_NAME+" text, "+
                COLUMN_DESCRIPTION+" text, "+
                COLUMN_INT_DRAV+" integer, "+
                COLUMN_KIND+" integer )";

        db.execSQL(sql1);
            insert(db, "Latte", "Espresso and steamed milk", R.drawable.latte,0);
            insert(db,  "Cappuccino", "Espresso, hot milk and steamed-milk foam",
                    R.drawable.blackcofee,0);
            insert(db,  "Filter", "Our best drip coffee", R.drawable.blackcofee,0);
            insert(db,"Beef","Our beef is very bloody",R.drawable.meat,1);
            insert(db,"Carrot","Our carrot is orange",R.drawable.vegetable,2);}
        if (oldVersion<2){
            db.execSQL("alter table "+TABLE_NAME+" add column "+COLUMN_FAVORITE+" numeric;");
        }

   }

    public void insert(SQLiteDatabase db,String name,String tasty,int id_draver,int kind){

        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_DESCRIPTION,tasty);
        contentValues.put(COLUMN_INT_DRAV,id_draver);
        contentValues.put(COLUMN_KIND,kind);
        db.insert(TABLE_NAME,null,contentValues);}
}
