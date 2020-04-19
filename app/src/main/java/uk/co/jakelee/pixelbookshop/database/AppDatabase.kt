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
import uk.co.jakelee.pixelbookshop.database.dao.*
import uk.co.jakelee.pixelbookshop.database.entity.*
import uk.co.jakelee.pixelbookshop.lookups.*
import uk.co.jakelee.pixelbookshop.utils.OwnedBookGenerator
import java.math.BigDecimal

@Database(entities = [Message::class, OwnedBook::class, OwnedFloor::class, OwnedFurniture::class,
    PastPurchase::class, PendingPurchase::class, Player::class, Shop::class],
    version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ownedBookDao(): OwnedBookDao
    abstract fun ownedFloorDao(): OwnedFloorDao
    abstract fun ownedFurnitureDao(): OwnedFurnitureDao
    abstract fun pastPurchaseDao(): PastPurchaseDao
    abstract fun pendingPurchaseDao(): PendingPurchaseDao
    abstract fun playerDao(): PlayerDao
    abstract fun shopDao(): ShopDao
    abstract fun messageDao(): MessageDao

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
            database.messageDao().insert(Message(0, MessageType.Positive, "Welcome to the game!", true, System.currentTimeMillis()))

            database.playerDao().insert(Player("Jake", 100, BigDecimal(5000), 5, 0))

            val wallInfo = WallInfo(Wall.Brick, false, 3)
            database.shopDao().insert(Shop(1, "Jake's Shop", wallInfo, System.currentTimeMillis()))

            database.ownedFloorDao().insert(listOf(
                OwnedFloor(1, 0, 0, true, Floor.StoneMissing),
                OwnedFloor(1, 1, 0, true, Floor.StoneMissing),
                OwnedFloor(1, 2, 0, true, Floor.StoneMissing),
                OwnedFloor(1, 3, 0, true, Floor.StoneMissing),
                OwnedFloor(1, 4, 0, true, Floor.StoneMissing),
                OwnedFloor(1, 5, 0, true, Floor.StoneMissing),
                OwnedFloor(1, 6, 0, true, Floor.StoneMissing),
                OwnedFloor(1, 7, 0, true, Floor.StoneMissing),

                OwnedFloor(1, 0, 1, true, Floor.Dirt),
                OwnedFloor(1, 1, 1, true, Floor.Dirt),
                OwnedFloor(1, 2, 1, true, Floor.Dirt),
                OwnedFloor(1, 3, 1, true, Floor.Dirt),
                OwnedFloor(1, 4, 1, true, Floor.Dirt),
                OwnedFloor(1, 5, 1, true, Floor.Dirt),
                OwnedFloor(1, 6, 1, true, Floor.Dirt),
                OwnedFloor(1, 7, 1, true, Floor.Dirt),

                OwnedFloor(1, 0, 2, true, Floor.StoneUneven),
                OwnedFloor(1, 1, 2, true, Floor.StoneUneven),
                OwnedFloor(1, 2, 2, true, Floor.StoneUneven),
                OwnedFloor(1, 3, 2, true, Floor.StoneUneven),
                OwnedFloor(1, 4, 2, true, Floor.StoneUneven),
                OwnedFloor(1, 5, 2, true, Floor.StoneUneven),
                OwnedFloor(1, 6, 2, true, Floor.StoneUneven),
                OwnedFloor(1, 7, 2, true, Floor.StoneUneven),

                OwnedFloor(1, 0, 3, true, Floor.WoodOld),
                OwnedFloor(1, 1, 3, true, Floor.WoodOld),
                OwnedFloor(1, 2, 3, true, Floor.WoodOld),
                OwnedFloor(1, 3, 3, true, Floor.WoodOld),
                OwnedFloor(1, 4, 3, true, Floor.WoodOld),
                OwnedFloor(1, 5, 3, true, Floor.WoodOld),
                OwnedFloor(1, 6, 3, true, Floor.WoodOld),
                OwnedFloor(1, 7, 3, true, Floor.WoodOld),

                OwnedFloor(1, 0, 4, true, Floor.Stone),
                OwnedFloor(1, 1, 4, true, Floor.Stone),
                OwnedFloor(1, 2, 4, true, Floor.Stone),
                OwnedFloor(1, 3, 4, true, Floor.Stone),
                OwnedFloor(1, 4, 4, true, Floor.Stone),
                OwnedFloor(1, 5, 4, true, Floor.Stone),
                OwnedFloor(1, 6, 4, true, Floor.Stone),
                OwnedFloor(1, 7, 4, true, Floor.Stone),

                OwnedFloor(1, 0, 5, true, Floor.Stone),
                OwnedFloor(1, 1, 5, true, Floor.Stone),
                OwnedFloor(1, 2, 5, true, Floor.Stone),
                OwnedFloor(1, 3, 5, true, Floor.Stone),
                OwnedFloor(1, 4, 5, true, Floor.Stone),
                OwnedFloor(1, 5, 5, true, Floor.Stone),
                OwnedFloor(1, 6, 5, true, Floor.Stone),
                OwnedFloor(1, 7, 5, true, Floor.Stone)
            ))
            database.ownedFurnitureDao().insert(
                OwnedFurniture(1, 1, 0, 0, true, Furniture.WideBookcase),
                OwnedFurniture(2, 1, 1, 0, true, Furniture.WideShelf),
                OwnedFurniture(3, 1, 2, 0, true, Furniture.DisplayShelf),
                OwnedFurniture(4, 1, 3, 0, true, Furniture.PlainTable),
                OwnedFurniture(5, 1, 4, 0, true, Furniture.SingleCandle),
                OwnedFurniture(6, 1, 6, 0, true, Furniture.TripleCrate),
                OwnedFurniture(7, 1, 7, 0, true, Furniture.TripleBarrel),

                OwnedFurniture(11, 1, 0, 1, true, Furniture.WideBookcase),
                OwnedFurniture(12, 1, 1, 1, true, Furniture.WideShelf),
                OwnedFurniture(13, 1, 2, 1, true, Furniture.DisplayShelf),
                OwnedFurniture(14, 1, 3, 1, true, Furniture.PlainTable),
                OwnedFurniture(15, 1, 4, 1, true, Furniture.Lectern),
                OwnedFurniture(16, 1, 6, 1, true, Furniture.Crate),
                OwnedFurniture(17, 1, 7, 1, false, Furniture.QuintupleBarrel),

                OwnedFurniture(21, 1, 0, 2, true, Furniture.WideBookcase),
                OwnedFurniture(22, 1, 1, 2, true, Furniture.WideShelf),
                OwnedFurniture(23, 1, 2, 2, true, Furniture.DisplayShelf),
                OwnedFurniture(24, 1, 3, 2, true, Furniture.PlainTable),
                OwnedFurniture(25, 1, 4, 2, true, Furniture.SingleCandle),
                OwnedFurniture(26, 1, 6, 2, true, Furniture.TripleCrate),
                OwnedFurniture(27, 1, 7, 2, true, Furniture.Barrel),

                OwnedFurniture(32, 1, 1, 3, true, Furniture.Rug),
                OwnedFurniture(33, 1, 2, 3, true, Furniture.Rug),
                OwnedFurniture(34, 1, 3, 3, true, Furniture.Rug),
                OwnedFurniture(35, 1, 4, 3, true, Furniture.Rug),
                OwnedFurniture(36, 1, 5, 3, true, Furniture.Rug),
                OwnedFurniture(37, 1, 6, 3, true, Furniture.Rug),
                OwnedFurniture(38, 1, 7, 3, true, Furniture.Rug),

                OwnedFurniture(41, 1, 1, 4, false, Furniture.Shelf),
                OwnedFurniture(42, 1, 2, 4, false, Furniture.DisplayShelf),

                OwnedFurniture(51, 1, 0, 5, true, Furniture.Bookcase),
                OwnedFurniture(52, 1, 1, 5, false, Furniture.WideBookcase),
                OwnedFurniture(53, 1, 2, 5, false, Furniture.WideBookcase),
                OwnedFurniture(54, 1, 3, 5, false, Furniture.WideBookcase),
                OwnedFurniture(55, 1, 4, 5, true, Furniture.TripleCandle),
                OwnedFurniture(56, 1, 5, 5, false, Furniture.WideShelf),
                OwnedFurniture(57, 1, 6, 5, false, Furniture.WideBookcase),
                OwnedFurniture(58, 1, 7, 5, false, Furniture.WideBookcase)
            )
            val bookList = OwnedBookGenerator(OwnedBookSource.Gift).generate(50)
            database.ownedBookDao().insert(*bookList.toTypedArray())

            val wallInfo2 = WallInfo(Wall.StoneHoles, false, 1)
            database.shopDao().insert(Shop(2, "Other's Shop", wallInfo2, System.currentTimeMillis()))
            database.ownedFloorDao().insert(listOf(
                OwnedFloor(2, 0, 0, true, Floor.Wood),
                OwnedFloor(2, 1, 0, true, Floor.Wood),
                OwnedFloor(2, 2, 0, true, Floor.Wood),
                OwnedFloor(2, 0, 1, true, Floor.Wood),
                OwnedFloor(2, 1, 1, true, Floor.Wood),
                OwnedFloor(2, 2, 1, true, Floor.Wood),
                OwnedFloor(2, 0, 2, true, Floor.Wood),
                OwnedFloor(2, 1, 2, true, Floor.Wood),
                OwnedFloor(2, 2, 2, true, Floor.Wood)
            ))
            database.ownedFurnitureDao().insert(
                OwnedFurniture(999, 2, 0, 0, true, Furniture.Chair),
                OwnedFurniture(998, 2, 1, 0, true, Furniture.RoundTable))
        }
    }
}
