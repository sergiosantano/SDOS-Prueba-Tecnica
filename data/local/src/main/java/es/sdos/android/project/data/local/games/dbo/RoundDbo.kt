package es.sdos.android.project.data.local.games.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoundDbo(
    @PrimaryKey var id: Long?
)