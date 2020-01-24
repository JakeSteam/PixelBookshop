package uk.co.jakelee.pixelbookshop.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.co.jakelee.pixelbookshop.database.dao.OwnedBookDao
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFloorDao
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFurnitureDao
import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao
import uk.co.jakelee.pixelbookshop.database.entity.*
import uk.co.jakelee.pixelbookshop.model.*

@Database(entities = [OwnedBook::class, OwnedFloor::class, OwnedFurniture::class, Player::class, Shop::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ownedBookDao(): OwnedBookDao
    abstract fun ownedFloorDao(): OwnedFloorDao
    abstract fun ownedFurnitureDao(): OwnedFurnitureDao
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
                Player("Jake", 100, 50, System.currentTimeMillis())
            )
            database.ownedFloorDao().insert(
                OwnedFloor(3, 0, Floor.Wood),
                OwnedFloor(2, 0, Floor.Wood),
                OwnedFloor(1, 0, Floor.Wood),
                OwnedFloor(0, 0, Floor.Wood),

                OwnedFloor(3, 1, Floor.Wood),
                OwnedFloor(2, 1, null),
                OwnedFloor(1, 1, Floor.Dirt),
                OwnedFloor(0, 1, null),

                OwnedFloor(3, 2, null),
                OwnedFloor(2, 2, null),
                OwnedFloor(1, 2, Floor.Dirt),
                OwnedFloor(0, 2, null),

                OwnedFloor(3, 3, Floor.Dirt),
                OwnedFloor(2, 3, null),
                OwnedFloor(1, 3, Floor.Wood),
                OwnedFloor(0, 3, Floor.Dirt),

                OwnedFloor(3, 4, Floor.Wood),
                OwnedFloor(2, 4, Floor.Wood),
                OwnedFloor(1, 4, Floor.Marble),
                OwnedFloor(0, 4, Floor.Marble),

                OwnedFloor(3, 5, Floor.Marble),
                OwnedFloor(2, 5, Floor.Dirt),
                OwnedFloor(1, 5, Floor.Dirt),
                OwnedFloor(0, 5, Floor.Marble),

                OwnedFloor(3, 6, Floor.Marble),
                OwnedFloor(2, 6, Floor.Dirt),
                OwnedFloor(1, 6, Floor.Marble),
                OwnedFloor(0, 6, Floor.Marble)
            )
            database.ownedFurnitureDao().insert(
                OwnedFurniture(1, 3, 0, true, Furniture.RoundTable),
                OwnedFurniture(2, 2, 0, true, Furniture.Shelf),
                OwnedFurniture(3, 1, 0, true, Furniture.Bookcase),
                OwnedFurniture(4, 0, 0, true, Furniture.SmallCrate),
                OwnedFurniture(1, 3, 4, true, Furniture.TripleCandle),
                OwnedFurniture(2, 2, 4, true, Furniture.Chair),
                OwnedFurniture(3, 1, 4, true, Furniture.Bookcase),
                OwnedFurniture(4, 0, 4, true, Furniture.SmallCrate)
            )
            database.ownedBookDao().insert(
                OwnedBook(
                    1,
                    Book.Orwell1984,
                    1,
                    OwnedBookDefect.FoldedPages,
                    OwnedBookQuality.Poor,
                    OwnedBookSource.Gift,
                    OwnedBookType.Paperback
                )
            )
        }
    }
}
