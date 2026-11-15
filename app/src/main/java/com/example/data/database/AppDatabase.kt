package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.EscrowDao
import com.example.data.entity.AuditLogEntity
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.DisputeEntity
import com.example.data.entity.ListingEntity
import com.example.data.entity.TimelineEventEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.WalletAccountEntity
import com.example.data.entity.WithdrawalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ListingEntity::class,
        TransactionEntity::class,
        TimelineEventEntity::class,
        ChatMessageEntity::class,
        DisputeEntity::class,
        WalletAccountEntity::class,
        WithdrawalEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun escrowDao(): EscrowDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nirapod_escrow.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            DatabaseSeeder.seed(getInstance(context).escrowDao())
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
