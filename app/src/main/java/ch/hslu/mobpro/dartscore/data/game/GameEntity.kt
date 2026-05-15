package ch.hslu.mobpro.dartscore.data.game

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity


@Entity(
    tableName = "game",
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["winner_player_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["winner_player_id"])
    ]
)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Long,
    val type: String,
    val status: String,
    val winner_player_id: Int
)