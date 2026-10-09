package dev.fritze.skyward.core.sources

import dev.fritze.skyward.core.model.Certainty
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.days

/**
 * JVM-only supplement to the shared SourceRunnerTest (issue #131): that suite
 * proves the run switches into an *injected* `computeContext`; this proves
 * the production default does what §4.3 needs it to, which takes a real
 * thread to compare against.
 */
class SourceRunnerThreadTest {

    private val now = RUNNER_TEST_NOW

    /**
     * §4.3: a run's astronomy and its re-plan stay off the caller's thread.
     * Pull-to-refresh calls [SourceRunner.runDue] straight from a
     * view-model's main-thread scope; when the run executed there, the
     * Upcoming screen froze for the length of it.
     */
    @Test
    fun runDueDoesNotRunSourcesOrTheReplanOnTheCallersThread() = runTest {
        val caller = Thread.currentThread()
        var sourceThread: Thread? = null
        var replanThread: Thread? = null
        val fx = SourceRunnerFixture()
        val source = FakeSource("test-source", onRefresh = { sourceThread = Thread.currentThread() })
        source.nextResult = RefreshResult(
            listOf(runnerTestOcc("se:1", now + 1.days, Certainty.CERTAIN)),
            emptyMap(), null, SourceDiagnostics(ok = true),
        )
        val runner = fx.runner(listOf(source)) { replanThread = Thread.currentThread() }

        runner.runDue(now, force = setOf("test-source"))

        assertNotEquals(caller, assertNotNull(sourceThread))
        assertNotEquals(caller, assertNotNull(replanThread), "a newly-seen occurrence is material, so the re-plan ran")
    }
}
