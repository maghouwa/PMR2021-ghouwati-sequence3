package com.App1.app1.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.App1.app1.data.model.Item
import com.App1.app1.data.model.Liste
import com.App1.app1.data.model.User

@Database(
    entities = [
        User::class,
        Liste::class,
        Item::class
    ],
    version = 2
)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "to_do_database"
                ).build()
                INSTANCE = instance
                return INSTANCE as ToDoDatabase
            }
        }
    }
}