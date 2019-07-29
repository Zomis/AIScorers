package net.zomis.scorers

import kotlin.reflect.KClass

class ScoreParams<P>(val param: P, private val suppliers: List<ScoreObjectProvider<P>>,
    private val objects: MutableMap<KClass<out Any>, Any>) {

    fun <T : Any> require(clazz: KClass<T>): T {
        val existing = objects[clazz] as T?
        if (existing != null) {
            return existing
        }

        val supplier = suppliers.find { it.clazz == clazz }?.supplier ?:
            throw NoSuchElementException("Object of type $clazz was not found and could not be provided with param $param")
        val suppliedObject = supplier(param) as T
        objects[clazz] = suppliedObject
        return suppliedObject
    }

}

class NamedScorer<P, F>(val name: String, val scoring: (ScoreParams<P>, F) -> Double) {

    fun weight(weight: Double): NamedScorer<P, F> {
        return NamedScorer("$name*$weight") { p, f -> scoring(p, f) * weight }
    }

    fun multiply(other: NamedScorer<P, F>): NamedScorer<P, F> {
        return NamedScorer("$name*${other.name}") { p, f -> scoring(p, f) * other.scoring(p, f) }
    }

}

class ScoreObjectProvider<P>(val clazz: KClass<out Any>, val supplier: (P) -> Any)
class ScorePostProcessor<P, F>(val name: String) { }

class Scorers<P, F>(val fieldsToScore: (P) -> Iterable<F>, val canScoreField: (ScoreParams<P>, F) -> Boolean) {

    fun config(): ScorersConfig<P, F> {
        return ScorersConfig(this, listOf(), listOf(), listOf())
    }

    fun scorer(name: String, scoring: (F) -> Double): NamedScorer<P, F> {
        return NamedScorer(name) { _, f -> scoring(f) }
    }

    fun predicateScorer(name: String, predicate: (F) -> Boolean): NamedScorer<P, F> {
        return NamedScorer(name) { _, f -> if (predicate(f)) 1.0 else 0.0 }
    }

}

class ScorersConfig<P, F>(val root: Scorers<P, F>,
    val providers: List<ScoreObjectProvider<P>>,
    val scorers: List<NamedScorer<P, F>>,
    val post: List<ScorePostProcessor<P, F>>) {

    fun withProvider(clazz: KClass<out Any>, supplier: (P) -> Any): ScorersConfig<P, F> {
        val provider = ScoreObjectProvider(clazz, supplier)
        return ScorersConfig(root, providers.plus(provider), scorers, post)
    }

    fun withScorer(scorer: NamedScorer<P, F>): ScorersConfig<P, F> {
        return ScorersConfig(root, providers, scorers.plus(scorer), post)
    }

    fun withScorer(name: String, scoring: (ScoreParams<P>, F) -> Double): ScorersConfig<P, F> {
        val scorer = NamedScorer(name, scoring)
        return ScorersConfig(root, providers, scorers.plus(scorer), post)
    }

    fun withScorerParams(scoring: (ScoreParams<P>, F) -> Double): ScorersConfig<P, F> {
        val scorer = NamedScorer("scorer#${scorers.size}", scoring)
        return ScorersConfig(root, providers, scorers.plus(scorer), post)
    }

    fun withScorer(scoring: (F) -> Double): ScorersConfig<P, F> {
        val scorer = NamedScorer<P, F>("scorer#${scorers.size}") { _, f -> scoring(f) }
        return ScorersConfig(root, providers, scorers.plus(scorer), post)
    }

    fun withPost(postProcessor: ScorePostProcessor<P, F>): ScorersConfig<P, F> {
        TODO("Post processors not supported yet")
    }

    fun producer(param: P, fields: Iterable<F>): ScoreProducer<P, F> {
        return ScoreProducer(this, param, fields)
    }

}

class ScoreProducer<P, F>(private val config: ScorersConfig<P, F>, private val param: P, private val fields: Iterable<F>) {

    fun score(): ScoreResults<P, F> {
        val scoreParams = ScoreParams(param, config.providers, mutableMapOf())
        val fieldResults = fields.associate { f -> f to config.scorers.associate { scorer -> scorer.name to scorer.scoring(scoreParams, f) } }

        val fieldScores = fieldResults.entries.associate { it.key to FieldScore(it.key, 0, it.value, it.value.values.sum()) }
        val sorted = fieldScores.entries.sortedByDescending { it.value.score }
        val ranks = mutableListOf<MutableList<FieldScore<F>>>(mutableListOf())

        var currentRankIndex = 0
        for (entry in sorted) {
            val currentRank = ranks[currentRankIndex]
            if (currentRank.isEmpty()) {
                currentRank.add(entry.value)
            } else if (entry.value.score < currentRank.first().score) {
                currentRankIndex++
                ranks.add(mutableListOf(entry.value))
            } else {
                currentRank.add(entry.value)
            }
        }

        return ScoreResults(param, fieldScores.entries.associate {
            entry -> entry.key to FieldScore(entry.key, 1 + ranks.indexOfFirst { it.contains(entry.value) }, entry.value.scores, entry.value.score)
        })
    }

}

data class ScoreResults<P, F>(val param: P, val fieldScores: Map<F, FieldScore<F>>) {

    fun best(): List<F> {
        return fieldScores.values.filter { it.rank == 1 }.map { it.field }.toList()
    }

}

data class FieldScore<F>(val field: F, val rank: Int, val scores: Map<String, Double>, val score: Double)
