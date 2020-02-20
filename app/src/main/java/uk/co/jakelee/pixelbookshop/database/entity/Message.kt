package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakelee.pixelbookshop.interfaces.Model
import uk.co.jakelee.pixelbookshop.lookups.MessageType

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo val type: MessageType,
    @ColumnInfo val message: String,
    @ColumnInfo val dismissed: Boolean,
    @ColumnInfo val time: Long
): Model