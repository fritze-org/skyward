package dev.fritze.skyward.core.format

import kotlin.test.Test
import kotlin.test.assertEquals

class AboutLinksTest {

    @Test
    fun releaseBuildLinksToItsOwnTag() {
        assertEquals("$SOURCE_REPOSITORY_URL/tree/v0.1.41", sourceCodeUrl("0.1.41"))
        assertEquals("$SOURCE_REPOSITORY_URL/tree/v1.0.0", sourceCodeUrl("1.0.0"))
    }

    @Test
    fun buildPastATagFallsBackToTheRepositoryRoot() {
        // No tag holds this build's source, so linking v0.1.41 would point at
        // the wrong tree — the root is the honest answer.
        assertEquals(SOURCE_REPOSITORY_URL, sourceCodeUrl("0.1.41-3-gabc1234"))
    }

    @Test
    fun untaggedAndMalformedVersionsFallBackToTheRepositoryRoot() {
        assertEquals(SOURCE_REPOSITORY_URL, sourceCodeUrl("0.0.0-dev"))
        assertEquals(SOURCE_REPOSITORY_URL, sourceCodeUrl("0.1.0-rc.1"))
        assertEquals(SOURCE_REPOSITORY_URL, sourceCodeUrl("v0.1.41"))
        assertEquals(SOURCE_REPOSITORY_URL, sourceCodeUrl("0.01.41"))
        assertEquals(SOURCE_REPOSITORY_URL, sourceCodeUrl(""))
    }
}
