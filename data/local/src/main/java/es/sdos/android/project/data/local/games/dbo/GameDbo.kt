package es.sdos.android.project.data.local.games.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameDbo(
    @PrimaryKey var id: Long?
)