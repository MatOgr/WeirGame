package com.example.gralosujaca

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.core.widget.ContentLoadingProgressBar

object FeedReaderContract {
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "records"
        const val COLUMN_NAME_NICK = "nick"
        const val COLUMN_NAME_SCORE = "score"
        const val COLUMN_NAME_PASSWORD = "password"
    }

    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_NICK} TEXT," +
                "${FeedEntry.COLUMN_NAME_SCORE} INTEGER," +
                "${FeedEntry.COLUMN_NAME_PASSWORD} TEXT)"

    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"

    fun getCreate(): String {
        return SQL_CREATE_ENTRIES
    }

    fun getDelete(): String {
        return SQL_DELETE_ENTRIES
    }

}


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object {
        private const val DATABASE_NAME = "MyDB.db"
        private const val DATABASE_VER = 2
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(FeedReaderContract.getCreate())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(FeedReaderContract.getDelete())
        onCreate(db)
    }

    fun getAll() : List<String> {
        val list : MutableList<String> = mutableListOf()
        val db = this.readableDatabase

        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE} DESC"
        val limit = "10"
        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            sortOrder,
            limit
        )
        // loop if not empty
        while (cursor.moveToNext()) {
            list.add("${cursor.getString(1)}\t:\t${cursor.getInt(2)}\t[ ${cursor.getString(3)} ]")
        }

        cursor.close()
        db.close()

        return list
    }

    fun addOne(name: String?, score: Int?, passwd: String?) : Boolean? {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_NICK, name)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, score)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_PASSWORD, passwd)
        }
        val result = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
        db.close()
        if (result!!.equals(-1))
            return false
        return true
    }

    fun getOne(nick: String?, passwd: String?) : String? {
        val db = this.readableDatabase
        var result = ""
        val where = "nick LIKE ? AND password LIKE ?"
        val whereArgs = arrayOf(nick, passwd)

        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            null,
            where,
            whereArgs,
            null,
            null,
            null
        )

        if (cursor.moveToFirst())
            result = "${cursor.getString(1)}%@%${cursor.getInt(2)}%@%${cursor.getString(3)}"

        cursor.close()
        db.close()
        return result
    }

    fun deleteOne(nick: String?) : Boolean {
        val db = this.writableDatabase
        val result = db?.delete(FeedReaderContract.FeedEntry.TABLE_NAME, "nick LIKE ?", arrayOf(nick))
        db.close()
        if (result == 0)
            return false
        return true
    }

    fun updateData(nick: String?, passwd: String?, score: Int?) : Int? {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, score)
        }
        val result = db?.update(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            values,
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_NICK} LIKE ? AND ${FeedReaderContract.FeedEntry.COLUMN_NAME_PASSWORD} LIKE ?",
            arrayOf(nick, passwd)
        )
        db.close()
        return result
    }
}