package net.zomis.scorers

import org.junit.jupiter.api.Test

class ScorersTest {

    val strings: Array<String> = arrayOf("x", "something", "CAPITAL LETTERS ONLY", "aaaaa")
    val scorers = Scorers(this::fieldsToScore, this::canScoreField)

    private fun fieldsToScore(param: Int): Iterable<String> {
        return strings.toList()
    }

    private fun canScoreField(param: ScoreParams<Int>, field: String): Boolean {
        return true
    }

    @Test
    fun oneScorer() {
        scorers.scorer("length") { it.length.toDouble() }

        NamedScorer<Int, String>("length") { p, f -> f.length.toDouble() }
    }

    @Test
    fun predicateScorer() {
        scorers.predicateScorer("only capital letters") { it.toUpperCase() == it }
    }

    fun scorerWithDependency() {
        scorers.config().withProvider(Analyze::class) { Analyze() }.withScorer("requires analyze") { p, _ -> p.require(Analyze::class).value() }
    }

    class Analyze {
        fun value(): Double {
            return 4.0
        }
    }

    fun scorerWithPostScoring() {
        val simpleScorer = NamedScorer<Int, String>("created scorer") { p, f -> f.count { it == ' ' }.toDouble() }
//        scorers.config().withScorer(simpleScorer).withPost(ScorePostProcessor("adjust even")) {
//            Post()
//        }
    }

    @Test
    fun ranking() {
        val scores = scorers.config()
            .withScorer { it.length.toDouble() }
            .withScorer(scorers.predicateScorer("predicate") { it.toUpperCase() == it }.weight(10.0))
            .producer(42, this.strings.toList())
            .score()
        println(scores)
        val best = scores.best()
        assert(best.contains("CAPITAL LETTERS ONLY"))
    }

}