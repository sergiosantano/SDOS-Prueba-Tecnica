package es.sdos.android.project.data.local.games

import es.sdos.android.project.data.local.games.dbo.GameDbo
import es.sdos.android.project.data.local.games.dbo.RoundDbo
import es.sdos.android.project.data.model.game.GameBo
import es.sdos.android.project.data.model.game.RoundBo

fun GameDbo.toBo() = GameBo(
    id,
    date,
    listOf(),
    totalScore,
    finished
)

fun GameBo.toDbo() = GameDbo(
    id,
    date,
    totalScore,
    finished
)

fun RoundDbo.toBo() = RoundBo(
    id,
    gameId,
    roundNum,
    firstShot,
    secondShot,
    thirdShot,
    score
)

public fun RoundBo.toDbo() = RoundDbo(
    id,
    gameId,
    roundNum,
    firstShot,
    secondShot,
    thirdShot,
    score
)
