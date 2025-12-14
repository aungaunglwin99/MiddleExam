package com.example.loginsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "LoginDB", null, 2) {   // <-- version 2

    override fun onCreate(db: SQLiteDatabase) {

        // USERS TABLE (USERNAME IS CASE-INSENSITIVE)
        db.execSQL(
            """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE COLLATE NOCASE,
                password TEXT
            )
            """.trimIndent()
        )

        // STATUS TABLE
        db.execSQL(
            """
            CREATE TABLE status (
                user_id INTEGER,
                text TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS status")
        onCreate(db)
    }

    // ================= REGISTER =================
    fun register(username: String, password: String): Boolean {
        val cv = ContentValues()
        cv.put("username", username.trim()) // COLLATE NOCASE handles case
        cv.put("password", password)

        return writableDatabase.insert("users", null, cv) != -1L
    }

    // ================= LOGIN =================
    fun login(username: String, password: String): Int {
        val c = readableDatabase.rawQuery(
            "SELECT id FROM users WHERE username=? AND password=?",
            arrayOf(username.trim(), password)
        )

        val result = if (c.moveToFirst()) c.getInt(0) else -1
        c.close()
        return result
    }

    // ================= STATUS =================
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

        val result = if (c.moveToFirst()) c.getString(0) else null
        c.close()
        return result
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
