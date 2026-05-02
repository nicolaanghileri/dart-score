package ch.hslu.mobpro.dartscore.data.game

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "game")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Long,
    val status: String
)