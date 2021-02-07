package es.sdos.android.project.home.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import es.sdos.android.project.common.ui.BaseViewModel
import es.sdos.android.project.common.util.lifecycle.MutableSourceLiveData
import es.sdos.android.project.data.model.game.GameBo
import es.sdos.android.project.data.repository.util.AppDispatchers
import es.sdos.android.project.data.repository.util.AsyncResult
import es.sdos.android.project.home.domain.AddShotUseCase
import es.sdos.android.project.home.domain.GetGameUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val getGameUseCase: GetGameUseCase,
    private val addShotUseCase: AddShotUseCase,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {

    private val gameLiveData = MutableSourceLiveData<AsyncResult<GameBo?>>()

    val shotInput = MutableLiveData<String>()
    val shotInputErrorMsg = MutableLiveData<String?>()

    private var gameId: Long? = null

    fun requestGame(gameId: Long) = viewModelScope.launch(dispatchers.io) {
        this@GameViewModel.gameId = gameId
        gameLiveData.changeSource(getGameUseCase(gameId))
    }

    fun getGameLiveData() = gameLiveData.liveData()

    fun addShot() {
        isValidShot()?.let { score ->
            gameId?.let { id ->
                viewModelScope.launch(dispatchers.io) {
                    gameLiveData.changeSource(addShotUseCase(id, score))

                }
            }
        }
    }

    private fun isValidShot(): Int? {
        val currentShotInput = shotInput.value
        return when {
            currentShotInput.isNullOrEmpty() -> setInputError("Obligatorio")
            currentShotInput.toIntOrNull() == null -> setInputError("Solo números")
            currentShotInput.toInt() > 10 -> setInputError("Valor ilegal")
            else -> currentShotInput.toInt()
        }
    }

    private fun setInputError(message: String): Int? {
        shotInputErrorMsg.value = message
        return null
    }
}