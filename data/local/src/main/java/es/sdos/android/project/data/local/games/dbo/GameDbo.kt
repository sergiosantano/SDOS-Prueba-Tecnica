package es.sdos.android.project.data.local.games.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "table_games")
data class GameDbo(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var date: Date,
    var totalScore: Int,
    var finished: Boolean
)