package es.sdos.android.project.data.remote.games

import es.sdos.android.project.data.model.game.GameBo
import es.sdos.android.project.data.model.game.RoundBo
import es.sdos.android.project.data.model.game.addShot
import es.sdos.android.project.data.model.game.isComplete
import es.sdos.android.project.data.remote.games.dto.GameDto
import java.util.Date

fun GameDto.toBo(): GameBo {
    val rounds = calculateRounds(id, shoots.split(",").map {
        it.trim()
        it.toIntOrNull() ?: 0
    })
    return GameBo(
        id,
        Date(),
        rounds,
        rounds.sumBy { it.score ?: 0 },
        rounds.isComplete()
    )
}

private fun calculateRounds(gameId: Long, shots: List<Int>): List<RoundBo> {
    var result = listOf<RoundBo>()
    for (shot in shots) {
        result = result.addShot(gameId, shot)
    }
    return result
}
