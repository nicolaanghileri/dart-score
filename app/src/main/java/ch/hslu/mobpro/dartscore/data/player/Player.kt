package ch.hslu.mobpro.dartscore.data.player

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ch.hslu.mobpro.dartscore.data.game.GameEntity

@Entity(
    tableName = "player",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["game_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["game_id"])
    ]
)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val game_id: Int,
    val name: String,
    val score: Int,
    val is_winner: Boolean
)