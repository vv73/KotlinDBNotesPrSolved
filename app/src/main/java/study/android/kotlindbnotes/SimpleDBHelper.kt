package study.android.kotlindbnotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import org.jetbrains.annotations.Contract

class SimpleDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object  DBContract  {
        // Группируем данные
        object  Entry  : BaseColumns {
            const val TABLE_NAME =  "results"
            const val COLUMN_NAME_NAME =  "name"
            const val COLUMN_NAME_RESULT = "result"
        }
        // Команды для работы
        const val SQL_CREATE =
            "CREATE TABLE ${Entry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY NOT NULL," +
                    "${Entry.COLUMN_NAME_NAME} TEXT," +
                    "${Entry.COLUMN_NAME_RESULT} INTEGER)"
        const val SQL_DELETE = "DROP TABLE IF EXISTS ${Entry.TABLE_NAME}"


    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DBContract.SQL_CREATE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DBContract.SQL_DELETE)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "results.db"
    }

    fun insert(result: Result){
        // Открываем базу на запись
        val db = writableDatabase
        // Комплектуем данные для вставки
        val values = ContentValues().apply {
            put(DBContract.Entry.COLUMN_NAME_NAME, result.name)
            put(DBContract.Entry.COLUMN_NAME_RESULT, result.result)
        }
        // вставляем данные в базу данных
        db?.insert(DBContract.Entry.TABLE_NAME, null, values)
    }

    fun getAll(order: String): List<Result> {
        val allRecords = mutableListOf<Result>()
        val cursor = readableDatabase.query(DBContract.Entry.TABLE_NAME, null, null,
            null, null,null, order)
        cursor.moveToFirst()
        while(cursor.moveToNext()){
            allRecords.add(Result(cursor.getString(cursor.getColumnIndex(DBContract.Entry.COLUMN_NAME_NAME)),
                           cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COLUMN_NAME_RESULT))))
        }
        return allRecords;
    }

    fun clearAll(){
        val db = writableDatabase;
        db.execSQL(DBContract.SQL_DELETE)
        onCreate(db);
    }

    fun deleteSubStringName(part: String){
        val db = writableDatabase;
        db.delete(DBContract.Entry.TABLE_NAME,DBContract.Entry.COLUMN_NAME_NAME + " LIKE ?", arrayOf("%"+part+"%"))
    }

    fun all() : Int{
        val db = writableDatabase;
        val cursor = db.query(DBContract.Entry.TABLE_NAME, arrayOf("SUM(RESULT)"), null, null, null, null, null)
        var res = 0
        if (cursor.moveToFirst()){
            res = cursor.getInt(0)
        }
        return res
    }

    fun good() : Int{
        val db = writableDatabase;

        val cursor1 = db.query(DBContract.Entry.TABLE_NAME, arrayOf("AVG(RESULT)"), null, null, null, null, null)
        var avg = 0
        if (cursor1.moveToFirst()){
            avg = cursor1.getInt(0)
        }
        val cursor2 = db.query(DBContract.Entry.TABLE_NAME, arrayOf("COUNT(*)"), "RESULT > " + avg, null, null, null, null)
        var res = 0
        if (cursor2.moveToFirst()){
            res = cursor2.getInt(0)
        }
        return res
    }

    fun longestName() : String{
        val db = writableDatabase;
        val cursor = db.query(DBContract.Entry.TABLE_NAME, arrayOf(DBContract.Entry.COLUMN_NAME_NAME), null, null, null, null, "LENGTH(NAME) DESC", "1")
        var res = ""
        if (cursor.moveToFirst()){
            res = cursor.getString(0)
        }
        return res
    }

    fun best() : String{
        val db = writableDatabase;
        val cursor = db.query(DBContract.Entry.TABLE_NAME, arrayOf(DBContract.Entry.COLUMN_NAME_NAME), null, null, null, null, "RESULT DESC", "1")
        var res = ""
        if (cursor.moveToFirst()){
            res = cursor.getString(0)
        }
        return res
    }

    fun english() : Int{
        val db = writableDatabase;
        val cursor = db.query(DBContract.Entry.TABLE_NAME, arrayOf("COUNT(*)"), "NAME < 'А'", null, null, null, null, null)
        var res = 0
        if (cursor.moveToFirst()){
            res = cursor.getInt(0)
        }
        return res
    }




}