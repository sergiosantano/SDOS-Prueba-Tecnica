package es.sdos.android.project.data.local.games

import androidx.room.*
import es.sdos.android.project.data.local.games.dbo.GameDbo
import es.sdos.android.project.data.local.games.dbo.RoundDbo

@Dao
abstract class GamesDao {

    @Query("SELECT * FROM table_games WHERE id = :gameId")
    abstract suspend fun getGame(gameId: Long): GameDbo?

    @Query("SELECT * FROM table_games")
    abstract suspend fun getGames(): List<GameDbo>

    @Query("SELECT * FROM table_rounds where id = :gameId")
    abstract suspend fun getRounds(gameId: Long): List<RoundDbo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveGame(gameDbo: GameDbo) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveRound(roundDbo: RoundDbo) : Long

    @Query("DELETE FROM table_games WHERE id = :gameId")
    abstract suspend fun deleteGame(gameId: Long)

    @Query("DELETE FROM table_rounds WHERE id = :gameId")
    abstract suspend fun deleteRounds(gameId: Long)

}