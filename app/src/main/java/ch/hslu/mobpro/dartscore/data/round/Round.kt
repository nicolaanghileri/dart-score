package ch.hslu.mobpro.dartscore.data.round

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ch.hslu.mobpro.dartscore.data.game.GameEntity
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity


@Entity(
    tableName = "round",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["game_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["player_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["game_id"]),
        Index(value = ["player_id"])
    ]
)
data class RoundEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val game_id: Int,
    val player_id: Int,
    val round_number: Int,
    val dart1: Int,
    val dart2: Int,
    val dart3: Int
)