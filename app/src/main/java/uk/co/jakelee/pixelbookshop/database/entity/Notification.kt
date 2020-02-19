package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakelee.pixelbookshop.interfaces.Model
import uk.co.jakelee.pixelbookshop.lookups.NotificationType

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo val type: NotificationType,
    @ColumnInfo val message: String,
    @ColumnInfo val time: Long
): Model