package es.sdos.android.project.home.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import es.sdos.android.project.data.model.game.GameBo
import es.sdos.android.project.data.repository.games.GamesRepository
import es.sdos.android.project.data.repository.util.AsyncResult
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AddShotUseCase @Inject constructor(private val gamesRepository: GamesRepository) {

    suspend operator fun invoke(gameId: Long, shotScore: Int): LiveData<AsyncResult<GameBo?>> {
        return gamesRepository.addShot(gameId, shotScore).flow().asLiveData(coroutineContext)
    }

}
