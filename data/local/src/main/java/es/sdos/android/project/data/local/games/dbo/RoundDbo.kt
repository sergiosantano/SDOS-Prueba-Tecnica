package es.sdos.android.project.data.local.games.dbo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "table_rounds",
    foreignKeys = [ForeignKey(
        entity = GameDbo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("gameId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class RoundDbo(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var gameId: Long,
    var roundNum: Int,
    var firstShot: Int,
    var secondShot: Int?,
    var thirdShot: Int?,
    var score: Int?
)