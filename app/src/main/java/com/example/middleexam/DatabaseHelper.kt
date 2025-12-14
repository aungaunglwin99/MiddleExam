package com.example.loginsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "LoginDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT)"
        )

        db.execSQL(
            "CREATE TABLE status (" +
                    "user_id INTEGER," +
                    "text TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS status")
        onCreate(db)
    }

    fun register(username: String, password: String): Boolean {
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("password", password)
        return writableDatabase.insert("users", null, cv) != -1L
    }

    fun login(username: String, password: String): Int {
        val c = readableDatabase.rawQuery(
            "SELECT id FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        return if (c.moveToFirst()) c.getInt(0) else -1
    }

    fun saveStatus(userId: Int, text: String) {
        writableDatabase.delete("status", "user_id=?", arrayOf(userId.toString()))
        val cv = ContentValues()
        cv.put("user_id", userId)
        cv.put("text", text)
        writableDatabase.insert("status", null, cv)
    }

    fun getStatus(userId: Int): String? {
        val c = readableDatabase.rawQuery(
            "SELECT text FROM status WHERE user_id=?",
            arrayOf(userId.toString())
        )
        return if (c.moveToFirst()) c.getString(0) else null
    }

    fun deleteStatus(userId: Int) {
        writableDatabase.delete("status", "user_id=?", arrayOf(userId.toString()))
    }

    fun hasStatus(userId: Int): Boolean {
        val c = readableDatabase.rawQuery(
            "SELECT 1 FROM status WHERE user_id=?",
            arrayOf(userId.toString())
        )
        val exists = c.moveToFirst()
        c.close()
        return exists
    }
}
