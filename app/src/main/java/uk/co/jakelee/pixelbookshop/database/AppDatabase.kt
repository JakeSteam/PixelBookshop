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
import uk.co.jakelee.pixelbookshop.lookups.*

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
                Player("Jake", 100, 50, Wall.Brick, System.currentTimeMillis())
            )
            database.ownedFloorDao().insert(
                OwnedFloor(0, 0, true, Floor.Stone),
                OwnedFloor(1, 0, true, Floor.Stone),
                OwnedFloor(2, 0, true, Floor.Stone),
                OwnedFloor(3, 0, true, Floor.Stone),
                OwnedFloor(4, 0, true, Floor.Stone),
                OwnedFloor(5, 0, true, Floor.Stone),
                OwnedFloor(6, 0, true, Floor.Stone),
                OwnedFloor(7, 0, true, Floor.Stone),

                OwnedFloor(0, 1, true, Floor.Stone),
                OwnedFloor(1, 1, true, Floor.Stone),
                OwnedFloor(2, 1, true, Floor.Stone),
                OwnedFloor(3, 1, true, Floor.Stone),
                OwnedFloor(4, 1, true, Floor.Stone),
                OwnedFloor(5, 1, true, Floor.Stone),
                OwnedFloor(6, 1, true, Floor.Stone),
                OwnedFloor(7, 1, true, Floor.Stone),

                OwnedFloor(0, 2, true, Floor.Stone),
                OwnedFloor(1, 2, true, Floor.Stone),
                OwnedFloor(2, 2, true, Floor.Stone),
                OwnedFloor(3, 2, true, Floor.Stone),
                OwnedFloor(4, 2, true, Floor.Stone),
                OwnedFloor(5, 2, true, Floor.Stone),
                OwnedFloor(6, 2, true, Floor.Stone),
                OwnedFloor(7, 2, true, Floor.Stone),

                OwnedFloor(0, 3, true, Floor.Stone),
                OwnedFloor(1, 3, true, Floor.Stone),
                OwnedFloor(2, 3, true, Floor.Stone),
                OwnedFloor(3, 3, true, Floor.Stone),
                OwnedFloor(4, 3, true, Floor.Stone),
                OwnedFloor(5, 3, true, Floor.Stone),
                OwnedFloor(6, 3, true, Floor.Stone),
                OwnedFloor(7, 3, true, Floor.Stone),

                OwnedFloor(0, 4, true, Floor.Stone),
                OwnedFloor(1, 4, true, Floor.Stone),
                OwnedFloor(2, 4, true, Floor.Stone),
                OwnedFloor(3, 4, true, Floor.Stone),
                OwnedFloor(4, 4, true, Floor.Stone),
                OwnedFloor(5, 4, true, Floor.Stone),
                OwnedFloor(6, 4, true, Floor.Stone),
                OwnedFloor(7, 4, true, Floor.Stone),

                OwnedFloor(0, 5, true, Floor.Stone),
                OwnedFloor(1, 5, true, Floor.Stone),
                OwnedFloor(2, 5, true, Floor.Stone),
                OwnedFloor(3, 5, true, Floor.Stone),
                OwnedFloor(4, 5, true, Floor.Stone),
                OwnedFloor(5, 5, true, Floor.Stone),
                OwnedFloor(6, 5, true, Floor.Stone),
                OwnedFloor(7, 5, true, Floor.Stone)
            )
            database.ownedFurnitureDao().insert(
                OwnedFurniture(1, 0, 0, true, Furniture.WideBookcase),
                OwnedFurniture(2, 1, 0, true, Furniture.WideShelf),
                OwnedFurniture(3, 2, 0, true, Furniture.DisplayShelf),
                OwnedFurniture(4, 3, 0, true, Furniture.PlainTable),
                OwnedFurniture(5, 4, 0, true, Furniture.SingleCandle),
                OwnedFurniture(6, 6, 0, true, Furniture.TripleCrate),
                OwnedFurniture(7, 7, 0, true, Furniture.TripleBarrel),

                OwnedFurniture(11, 0, 1, true, Furniture.WideBookcase),
                OwnedFurniture(12, 1, 1, true, Furniture.WideShelf),
                OwnedFurniture(13, 2, 1, true, Furniture.DisplayShelf),
                OwnedFurniture(14, 3, 1, true, Furniture.PlainTable),
                OwnedFurniture(15, 4, 1, true, Furniture.Lectern),
                OwnedFurniture(16, 6, 1, true, Furniture.Crate),
                OwnedFurniture(17, 7, 1, false, Furniture.QuintupleBarrel),

                OwnedFurniture(21, 0, 2, true, Furniture.WideBookcase),
                OwnedFurniture(22, 1, 2, true, Furniture.WideShelf),
                OwnedFurniture(23, 2, 2, true, Furniture.DisplayShelf),
                OwnedFurniture(24, 3, 2, true, Furniture.PlainTable),
                OwnedFurniture(25, 4, 2, true, Furniture.SingleCandle),
                OwnedFurniture(26, 6, 2, true, Furniture.TripleCrate),
                OwnedFurniture(27, 7, 2, true, Furniture.Barrel),

                OwnedFurniture(31, 0, 3, true, Furniture.Rug),
                OwnedFurniture(32, 1, 3, true, Furniture.Rug),
                OwnedFurniture(33, 2, 3, true, Furniture.Rug),
                OwnedFurniture(34, 3, 3, true, Furniture.Rug),
                OwnedFurniture(35, 4, 3, true, Furniture.Rug),
                OwnedFurniture(36, 5, 3, true, Furniture.Rug),
                OwnedFurniture(37, 6, 3, true, Furniture.Rug),
                OwnedFurniture(38, 7, 3, true, Furniture.Rug),

                OwnedFurniture(41, 1, 4, false, Furniture.Shelf),
                OwnedFurniture(42, 2, 4, false, Furniture.DisplayShelf),

                OwnedFurniture(51, 0, 5, true, Furniture.Bookcase),
                OwnedFurniture(52, 1, 5, false, Furniture.WideBookcase),
                OwnedFurniture(53, 2, 5, false, Furniture.WideBookcase),
                OwnedFurniture(54, 3, 5, false, Furniture.WideBookcase),
                OwnedFurniture(55, 4, 5, true, Furniture.TripleCandle),
                OwnedFurniture(56, 5, 5, false, Furniture.WideShelf),
                OwnedFurniture(57, 6, 5, false, Furniture.WideBookcase),
                OwnedFurniture(58, 7, 5, false, Furniture.WideBookcase)
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
