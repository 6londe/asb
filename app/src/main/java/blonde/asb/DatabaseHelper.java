package blonde.asb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "benchmarkDb";
    private static final int DATABASE_VERSION = 1;

    private Random random = new Random();
    private int randomInt;
    private String randomStr;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    void preparing(int[] settingValues) {
        //preparing test data
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS t1");
        db.execSQL("DROP TABLE IF EXISTS t2");
        db.execSQL("DROP TABLE IF EXISTS t3");

        db.execSQL("CREATE TABLE t1(a INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "b INTEGER, c INTEGER, d INTEGER, "
                + "e VARCHAR(100), f VARCHAR(100), g VARCHAR(100), h VARCHAR(100))");

        db.execSQL("CREATE TABLE t2(a INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "b INTEGER, c INTEGER, d INTEGER, "
                + "e VARCHAR(100), f VARCHAR(100), g VARCHAR(100), h VARCHAR(100))");

        db.execSQL("CREATE TABLE t3(a INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "b INTEGER, c INTEGER, d INTEGER, "
                + "e VARCHAR(100), f VARCHAR(100), g VARCHAR(100), h VARCHAR(100))");

        for (int i = 1; i <= 3; i++) {
            randomInt = random.nextInt(99999);
            randomStr = Integer.toString(randomInt);
            db.execSQL("INSERT INTO t1(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
            db.execSQL("INSERT INTO t2(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
            db.execSQL("INSERT INTO t3(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
        }

        if (settingValues[0] < settingValues[6]) {
            for (int i = 0; i < (settingValues[6] - settingValues[0]); i++) {
                db.execSQL("INSERT INTO t1(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
            }
        }

        int higher = (settingValues[7] > settingValues[8] ? settingValues[7] : settingValues[8]);
        if (settingValues[1] < higher) {
            for (int i = 0; i < (higher - settingValues[1]); i++) {
                db.execSQL("INSERT INTO t2(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
            }
        }
        db.close();
    }

    void test_case_1(int repeat) {
        //TestCase 1 : 1000 INSERTs
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        for (int i = 1; i <= repeat; i++) {
            randomInt = random.nextInt(90000) + 10000;
            randomStr = Integer.toString(randomInt);
            db.execSQL("INSERT INTO t1(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
            cursor = db.rawQuery("SELECT * FROM t1 WHERE a>=" + i + " AND a<" + (i + 4), null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }

    void test_case_2(int repeat) {
        //TestCase 2 : 25000 INSERTs in a transaction
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        db.beginTransaction();
        for (int i = 1; i <= repeat; i++) {
            randomInt = random.nextInt(90000) + 10000;
            randomStr = Integer.toString(randomInt);
            db.execSQL("INSERT INTO t2(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
            cursor = db.rawQuery("SELECT * FROM t2 WHERE a>=" + i + " AND a<" + (i + 4), null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    void test_case_3(int repeat) {
        //TestCase 3 : 25000 INSERTs into an indexed table
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        db.execSQL("CREATE INDEX i3 ON t3(c)");
        db.beginTransaction();
        for (int i = 1; i <= repeat; i++) {
            randomInt = random.nextInt(90000) + 10000;
            randomStr = Integer.toString(randomInt);
            db.execSQL("INSERT INTO t3(b,c,d,e,f,g,h) VALUES(" + randomInt + "," + randomInt + "," + randomInt + ",'" + randomStr + "','" + randomStr + "','" + randomStr + "','" + randomStr + "')");
            cursor = db.rawQuery("SELECT * FROM t3 WHERE a>=" + i + " AND a<" + (i + 4), null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    void test_case_4(int repeat) {
        //TestCase 4 : 100 SELECTs without an index
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        int min = 0, max;

        db.beginTransaction();
        for (int i = 1; i <= repeat; i++) {
            min = min + (i - 1) * 100;
            max = min + 1000;

            cursor = db.rawQuery("SELECT count(*), avg(b) FROM t2 WHERE b>=" + min + " AND b<" + max, null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("count(*)"));
                cursor.getInt(cursor.getColumnIndex("avg(b)"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    void test_case_5(int repeat) {
        //TestCase 5 : 100 SELECTs on a string comparison
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        db.beginTransaction();
        for (int i = 1; i <= repeat; i++) {
            cursor = db.rawQuery("SELECT count(*), avg(b) FROM t2 WHERE e LIKE '%" + Integer.toString(i) + "%'", null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("count(*)"));
                cursor.getInt(cursor.getColumnIndex("avg(b)"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    void test_case_6() {
        //TestCase 6 : Creating an index
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE INDEX i2b ON t2(b)");
        db.execSQL("CREATE INDEX i2c ON t2(c)");
        db.close();
    }

    void test_case_7(int repeat) {
        //TestCase 7 : 5000 SELECTs with an index
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        int min = 0, max;

        for (int i = 1; i <= repeat; i++) {
            min = min + (i - 1) * 100;
            max = min + 1000;

            cursor = db.rawQuery("SELECT count(*), avg(b) FROM t2 WHERE b>=" + min + " AND b<" + max, null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("count(*)"));
                cursor.getInt(cursor.getColumnIndex("avg(b)"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }

    void test_case_8(int repeat) {
        //TestCase 8 : 1000 UPDATEs without an index
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        int min = 0, max;

        db.beginTransaction();
        for (int i = 1; i <= repeat; i++) {
            min = min + (i - 1) * 10;
            max = min + 10;

            db.execSQL("UPDATE t1 SET b=b*2 WHERE c>=" + min + " AND c<" + max);
            cursor = db.rawQuery("SELECT * FROM t1 WHERE a>=" + i + " AND a<" + (i + 4), null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    void test_case_9(int repeat) {
        //TestCase 9 : 25000 UPDATEs with an index
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        db.beginTransaction();
        for (int i = 1; i <= repeat; i++) {
            randomInt = random.nextInt(90000) + 10000;

            db.execSQL("UPDATE t2 SET b=" + randomInt + " WHERE c=" + i);
            cursor = db.rawQuery("SELECT * FROM t2 WHERE a>=" + i + " AND a<" + (i + 4), null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    void test_case_10(int repeat) {
        //TestCase 10 : 25000 text UPDATEs with an index
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        db.beginTransaction();
        for (int i = 1; i <= repeat; i++) {
            randomInt = random.nextInt(90000) + 10000;
            randomStr = Integer.toString(randomInt);
            db.execSQL("UPDATE t2 SET e='" + randomStr + "' WHERE d=" + i);
            cursor = db.rawQuery("SELECT * FROM t2 WHERE a>=" + i + " AND a<" + (i + 4), null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    void test_case_11() {
        //TestCase 11 : INSERTs from a SELECT
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        db.beginTransaction();

        db.execSQL("INSERT INTO t1(b,c,d,e,f,g,h) SELECT e,f,b,g,c,h,d FROM t2");
        cursor = db.rawQuery("SELECT * FROM t1 WHERE a>=1 AND a<4", null);
        if (cursor != null) cursor.moveToFirst();
        do {
            cursor.getInt(cursor.getColumnIndex("a"));
            cursor.getInt(cursor.getColumnIndex("b"));
            cursor.getInt(cursor.getColumnIndex("c"));
            cursor.getInt(cursor.getColumnIndex("d"));
            cursor.getString(cursor.getColumnIndex("e"));
            cursor.getString(cursor.getColumnIndex("f"));
            cursor.getString(cursor.getColumnIndex("g"));
            cursor.getString(cursor.getColumnIndex("h"));
        } while (cursor.moveToNext());

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
    }

    void test_case_12(int repeat) {
        //TestCase 12 : DELETE without an index
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        for (int i = 1; i <= repeat; i++) {
            db.execSQL("DELETE FROM t2 WHERE e LIKE '%" + (i * 10) + "%'");
            cursor = db.rawQuery("SELECT * FROM t2 WHERE a>=1 AND a<4", null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }

    void test_case_13(int repeat) {
        //TestCase 13 : DELETE with an index
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        int min = 0, max;

        for (int i = 1; i <= repeat; i++) {
            min = min + (i - 1) * 10;
            max = min + 10;

            db.execSQL("DELETE FROM t2 WHERE c>" + min + " AND c<" + max);
            cursor = db.rawQuery("SELECT * FROM t2 WHERE a>=1 AND a<4", null);
            if (cursor != null) cursor.moveToFirst();
            do {
                cursor.getInt(cursor.getColumnIndex("a"));
                cursor.getInt(cursor.getColumnIndex("b"));
                cursor.getInt(cursor.getColumnIndex("c"));
                cursor.getInt(cursor.getColumnIndex("d"));
                cursor.getString(cursor.getColumnIndex("e"));
                cursor.getString(cursor.getColumnIndex("f"));
                cursor.getString(cursor.getColumnIndex("g"));
                cursor.getString(cursor.getColumnIndex("h"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }

    void test_case_14() {
        //TestCase 14 : DROP TABLE
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE t1");
        db.execSQL("DROP TABLE t2");
        db.execSQL("DROP TABLE t3");
    }
}
