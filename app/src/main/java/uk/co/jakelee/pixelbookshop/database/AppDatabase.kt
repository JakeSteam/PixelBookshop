package uk.co.jakelee.pixelbookshop.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.co.jakelee.pixelbookshop.database.entity.Book

@Database(entities = [Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
