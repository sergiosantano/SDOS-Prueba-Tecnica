package es.sdos.android.project.data.local.games

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import es.sdos.android.project.data.local.games.dbo.GameDbo
import es.sdos.android.project.data.local.games.dbo.RoundDbo

@Dao
abstract class GamesDao {

    @Query("SELECT * FROM gamedbo WHERE id = :gameId")
    abstract suspend fun getGame(gameId: Long): GameDbo?

    @Query("SELECT * FROM gamedbo")
    abstract suspend fun getGames(): List<GameDbo>

    @Query("SELECT * FROM rounddbo where id = :gameId")
    abstract suspend fun getRounds(gameId: Long): List<RoundDbo>

    @Insert
    abstract suspend fun saveGame(gameDbo: GameDbo) : Long

    @Insert
    abstract suspend fun saveRound(roundDbo: RoundDbo) : Long

    @Query("DELETE FROM gamedbo WHERE id = :gameId")
    abstract suspend fun deleteGame(gameId: Long)

    @Query("DELETE FROM rounddbo WHERE id = :gameId")
    abstract suspend fun deleteRounds(gameId: Long)

}