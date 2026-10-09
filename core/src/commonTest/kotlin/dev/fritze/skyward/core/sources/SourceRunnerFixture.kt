package dev.fritze.skyward.core.sources

import dev.fritze.skyward.core.model.Certainty
import dev.fritze.skyward.core.model.GeoPoint
import dev.fritze.skyward.core.model.Occurrence
import dev.fritze.skyward.core.model.Phenomenon
import dev.fritze.skyward.core.model.SolarEclipseKind
import dev.fritze.skyward.core.model.SolarEclipsePayload
import dev.fritze.skyward.core.model.TimeWindow
import dev.fritze.skyward.core.persistence.LocationRepo
import dev.fritze.skyward.core.persistence.OccurrenceRepo
import dev.fritze.skyward.core.persistence.RuleRepo
import dev.fritze.skyward.core.persistence.SettingsRepo
import dev.fritze.skyward.core.persistence.SkywardDatabase
import dev.fritze.skyward.core.persistence.SourceStateRepo
import dev.fritze.skyward.core.persistence.VisibilityCacheRepo
import dev.fritze.skyward.core.persistence.inMemorySkywardDatabase
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

/**
 * Shared scaffolding for [SourceRunner] orchestration tests (§6.2/§6.3): the
 * portable suite in `commonTest` and the JVM-only thread assertions in
 * `desktopTest` both build on it (issue #131).
 */
internal val RUNNER_TEST_NOW: Instant = Instant.parse("2026-01-01T00:00:00Z")

internal class FakeSource(
    override val id: String,
    override val phenomena: Set<Phenomenon> = setOf(Phenomenon.SOLAR_ECLIPSE),
    private val schedule: Schedule = Schedule.OnHorizonChange,
    override val kind: SourceKind = SourceKind.COMPUTED,
    /** Suspending, so a test can inspect the coroutine context the refresh ran in. */
    private val onRefresh: suspend (String) -> Unit = {},
) : EventSource {
    var nextResult: RefreshResult? = null
    var nextError: Exception? = null
    var callCount = 0

    override suspend fun refresh(req: RefreshRequest): RefreshResult {
        callCount++
        onRefresh(id)
        nextError?.let { throw it }
        return nextResult ?: RefreshResult(emptyList(), emptyMap(), null, SourceDiagnostics(ok = true))
    }

    override fun schedule(settings: SourceSettings) = schedule
}

internal fun runnerTestOcc(
    id: String,
    peakTime: Instant,
    certainty: Certainty,
    title: String = "t",
    fetchedAt: Instant = RUNNER_TEST_NOW,
) = Occurrence(
    id = id, phenomenon = Phenomenon.SOLAR_ECLIPSE, sourceId = "test-source", title = title,
    window = TimeWindow(peakTime - 1.hours, peakTime + 1.hours), peakTime = peakTime, certainty = certainty,
    payload = SolarEclipsePayload(SolarEclipseKind.TOTAL, GeoPoint(0.0, 0.0), peakTime, emptyList(), 1.0),
    fetchedAt = fetchedAt, expiresAt = null,
)

/** Real repos over a fresh in-memory database, plus a re-plan callback that counts its calls. */
internal class SourceRunnerFixture {
    val db: SkywardDatabase = inMemorySkywardDatabase()
    val occurrenceRepo = OccurrenceRepo(db)
    val sourceStateRepo = SourceStateRepo(db)
    val settingsRepo = SettingsRepo(db)
    val ruleRepo = RuleRepo(db)
    val locationRepo = LocationRepo(db)
    val visibilityCacheRepo = VisibilityCacheRepo(db)
    var replanCalls = 0
    var lastReplanNow: Instant? = null

    /** A runner on the production default `computeContext`, re-planning into [replanCalls]. */
    fun runner(vararg sources: EventSource) = runner(sources.toList()) { n -> replanCalls++; lastReplanNow = n }

    /** A runner with its re-plan callback and, optionally, its `computeContext` replaced. */
    fun runner(
        sources: List<EventSource>,
        computeContext: CoroutineContext? = null,
        onOccurrencesChanged: suspend (Instant) -> Unit,
    ): SourceRunner =
        // Two calls rather than a fallback value, so a null leaves SourceRunner's
        // own default in force instead of a copy of it that could drift.
        if (computeContext == null) {
            SourceRunner(
                sources, occurrenceRepo, sourceStateRepo, settingsRepo, ruleRepo, locationRepo, visibilityCacheRepo,
                onOccurrencesChanged,
            )
        } else {
            SourceRunner(
                sources, occurrenceRepo, sourceStateRepo, settingsRepo, ruleRepo, locationRepo, visibilityCacheRepo,
                onOccurrencesChanged, computeContext,
            )
        }
}
