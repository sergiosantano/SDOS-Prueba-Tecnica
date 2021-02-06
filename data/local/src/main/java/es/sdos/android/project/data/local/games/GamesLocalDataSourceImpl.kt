package es.sdos.android.project.data.local.games

import es.sdos.android.project.data.datasource.games.GamesLocalDataSource
import es.sdos.android.project.data.model.game.GameBo
import es.sdos.android.project.data.model.game.GameFilter
import es.sdos.android.project.data.model.game.addShot
import es.sdos.android.project.data.model.game.isComplete
import java.util.Date

class GamesLocalDataSourceImpl(
    private val gamesDao: GamesDao
) : GamesLocalDataSource {

    override suspend fun getGame(gameId: Long): GameBo? {
        return gamesDao.getGame(gameId)?.toBo()?.copy(rounds = gamesDao.getRounds(gameId).map { it.toBo() })
    }

    override suspend fun getGames(filter: GameFilter?): List<GameBo> {
        val allGames = gamesDao.getGames().map { gameDbo ->
            gameDbo.toBo().copy(rounds = gamesDao.getRounds(gameDbo.id!!).map { it.toBo() })
        }

        return if (filter != null) {
            allGames.filter { if (filter == GameFilter.FINISHED) it.finished else !it.finished }
        } else {
            allGames
        }
    }

    override suspend fun saveGames(games: List<GameBo>) {
        games.forEach { game ->
            gamesDao.saveGame(game.toDbo())
            game.rounds.forEach { gamesDao.saveRound(it.toDbo()) }
        }
    }

    override suspend fun createGame(): GameBo {
        val game = GameBo(null, Date(), listOf(), 0, false)
        val gameId = gamesDao.saveGame(game.toDbo())
        return gamesDao.getGame(gameId)!!.toBo()
    }

    override suspend fun deleteGame(gameId: Long) {
        gamesDao.deleteGame(gameId)
    }

    override suspend fun addShot(gameId: Long, shotScore: Int): GameBo? {
        val newRounds = gamesDao.getRounds(gameId).map { it.toBo() }.addShot(gameId, shotScore)
        newRounds.forEach {
            gamesDao.saveRound(it.toDbo())
        }

        val game = gamesDao.getGame(gameId)
        if (game != null) {
            // Check if the game is finished and set final score
            if (newRounds.isComplete()) {
                game.finished = true
                game.totalScore = newRounds.last().score!!
            }
            // Update game in DB
            gamesDao.saveGame(game)
        }

        return getGame(gameId)
    }
}
