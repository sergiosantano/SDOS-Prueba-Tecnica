package es.sdos.android.project.data.model.game

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RoundBoExtensionTest {

    @Test
    fun `when no rounds add one shot`() {
        // Given
        val rounds = listOf<RoundBo>()

        // When
        val result = addShot(rounds, 5)

        // Then
        assertThat(result.size, `is`(1))
        assertThat(result.last().roundNum, `is`(1))
        checkAllScores(result.last(), 5)
    }

    @Test
    fun `when last round is not complete adding one standard shot completes it`() {
        // Given
        val rounds = listOf(RoundBo(1, 1, 1, 5, null, null, null))

        // When
        val result = addShot(rounds, 3)

        // Then
        assertThat(result.size, `is`(1))
        assertThat(result.last().roundNum, `is`(1))
        checkAllScores(result.last(), 5, 3, finalScore = 8)
    }

    @Test
    fun `when last round is complete adding one standard shot creates new round`() {
        // Given
        val rounds = listOf(RoundBo(1, 1, 1, 5, 3, null, null))

        // When
        val result = addShot(rounds, 7)

        // Then
        assertThat(result.size, `is`(2))
        assertThat(result.last().roundNum, `is`(2))
        checkAllScores(result.last(), 7)
    }

    @Test
    fun `when ninth round is complete adding one standard shot creates new incomplete round`() {
        // Given
        val rounds = mutableListOf<RoundBo>().apply {
            for (i in 1..9) {
                this.add(RoundBo(i.toLong(), 1, i, 5, 3, null, 8))
            }
        }.toList()

        // When
        val result = addShot(rounds, 5)

        // Then
        assertThat(result.size, `is`(10))
        assertThat(result.last().roundNum, `is`(10))
        checkAllScores(result.last(), 5)
    }

    @Test
    fun `when tenth round is incomplete adding one standard shot completes the game`() {
        // Given
        val rounds = mutableListOf<RoundBo>().apply {
            for (i in 1..9) {
                this.add(RoundBo(i.toLong(), 1, i, 5, 3, null, 8))
            }
        }.toList()

        // When
        val result = addShot(rounds, 5).addShot(1, 3)

        // Then
        assertThat(result.size, `is`(10))
        assertThat(result.last().roundNum, `is`(10))
        checkAllScores(result.last(), 5, 3, finalScore = 80)
        assert(result.isComplete())
    }

    @Test
    fun `when adding a strike in standard round it is complete and score is calculated with two following`() {
        // Given
        val rounds = listOf<RoundBo>()

        // When
        val roundsAfterStrike = addShot(rounds, 10)
        val roundsAfterStrikeAndTwoMoreShots = addShot(roundsAfterStrike, 3).addShot(1, 3)

        // Then
        checkAllScores(roundsAfterStrike.last(), 10)
        checkAllScores(roundsAfterStrikeAndTwoMoreShots[0], 10, finalScore = 16)
        checkAllScores(roundsAfterStrikeAndTwoMoreShots[1], 3, 3, finalScore = 22)
    }

    @Test
    fun `when all rounds are strike final game score is 300`() {
        // Given
        var rounds = listOf<RoundBo>()
        for (i in 1..9) {
            rounds = addShot(rounds, 10)
        }

        // When
        val result = addShot(rounds, 10)
            .addShot(1, 10)
            .addShot(1, 10)

        // Then
        assertThat(result.size, `is`(10))
        assert(result.isComplete())
        checkAllScores(result[8], 10, finalScore = 270)
        checkAllScores(result[9], 10, 10, 10, 300)


    }

    @Test
    fun `when spare in the tenth round allow third shot`() {
        // Given
        var rounds = listOf<RoundBo>()
        for (i in 1..9) {
            rounds = addShot(rounds, 5).addShot(1, 5)
        }

        // When
        val result = addShot(rounds, 5)
            .addShot(1, 5)
            .addShot(1, 5)

        // Then
        assertThat(result.size, `is`(10))
        assert(result.isComplete())
        checkAllScores(result[8], 5, secondShot = 5, finalScore = 135)
        checkAllScores(result[9], 5, 5, 5, 150)
    }


    @Test
    fun `valid scores test`() {
        // Given
        val emptyRounds = listOf<RoundBo>()
        val incompleteRound = listOf(RoundBo(1, 1, 1, 5, null, null, null))

        // Then
        assert(!emptyRounds.invalidScore(10))
        assert(!emptyRounds.invalidScore(0))
        assert(!emptyRounds.invalidScore(4))
        assert(!incompleteRound.invalidScore(5))
        assert(!incompleteRound.invalidScore(0))
        assert(!incompleteRound.invalidScore(3))
    }

    @Test
    fun `invalid scores test`() {
        // Given
        val emptyRounds = listOf<RoundBo>()
        val incompleteRound = listOf(RoundBo(1, 1, 1, 5, null, null, null))

        // Then
        assert(emptyRounds.invalidScore(-1))
        assert(emptyRounds.invalidScore(11))
        assert(incompleteRound.invalidScore(11))
        assert(incompleteRound.invalidScore(6))
    }

    /*
     * --------------------------------------------------------------------
     *                          UTIL METHODS
     * --------------------------------------------------------------------
     */

    private fun addShot(rounds: List<RoundBo>, score: Int) = rounds.addShot(1, score)

    private fun checkAllScores(round: RoundBo, firstShot: Int, secondShot: Int? = null, thirdShot: Int? = null, finalScore: Int? = null) {
        assertThat(round.firstShot, `is`(firstShot))
        assertThat(round.secondShot, `is`(secondShot))
        assertThat(round.thirdShot, `is`(thirdShot))
        assertThat(round.score, `is`(finalScore))
    }

}