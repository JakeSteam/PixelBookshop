package uk.co.jakelee.pixelbookshop.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.co.jakelee.pixelbookshop.database.entity.Book
import uk.co.jakelee.pixelbookshop.database.entity.Player

@Database(entities = [Book::class, Player::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        initialiseDatabase(database)
                    }
                }
            }
        }

        suspend fun initialiseDatabase(database: AppDatabase) {
            database.playerDao().insert(
                Player("", 100, 50, System.currentTimeMillis())
            )

            database.bookDao().insert(
                Book(1, "Foundation", "Isaac Asimov", 1),
                Book(2, "Animal Farm", "George Orwell", 7),
                Book(3, "Thud!", "Terry Pratchett", 9),
                Book(4, "Misery", "Stephen King", 2)
            )
        }
    }
}
