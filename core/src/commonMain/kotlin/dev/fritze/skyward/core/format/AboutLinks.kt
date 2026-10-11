package dev.fritze.skyward.core.format

/**
 * §13.1(a) / §15.4: the hosted privacy policy, at a stable public non-PDF URL
 * so the same link can sit in Play Console and in both About screens. The page
 * is `docs/privacy-policy.md`, published by GitHub Pages from `main`'s `docs/`
 * folder (RELEASE.md, Play Console step 5).
 */
const val PRIVACY_POLICY_URL = "https://skywardapp.github.io/skyward/privacy-policy.html"

/** The public repository root — what [sourceCodeUrl] falls back to for a build that is not a release. */
const val SOURCE_REPOSITORY_URL = "https://github.com/skywardapp/skyward"

/**
 * §16's EONET row: credit alone is not enough, About must also surface EONET's
 * own "general information purposes" disclaimer (§7.7 carries the same caveat
 * into how its events are presented).
 */
const val EONET_DISCLAIMER =
    "NASA EONET event positions and dates are approximations, provided for general information purposes."

// A bare MAJOR.MINOR.PATCH, leading zeros rejected — the root build.gradle.kts's
// releaseTagPattern without its "v", since versionName drops that prefix.
private val releaseVersionPattern = Regex("""^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)$""")

/**
 * §13.1(b) / §16 / GPL §6(d): a shipped build links to the source tree at the
 * tag it was built from, not to whatever `main` holds today.
 *
 * Keyed on the versionName the About screen already displays rather than on a
 * separate build-time tag field. §15.4's derivation makes the bare `X.Y.Z`
 * form mean exactly "HEAD is release tag vX.Y.Z"; a build past the tag renders
 * as `X.Y.Z-<n>-g<sha>` and an untagged one as `0.0.0-dev`, and both fall back
 * to the repository root, since no tag holds their source. Reading the shown
 * version also covers the `-PskywardVersionName` override (a tarball or pinned
 * F-Droid recipe with no git history), where a separately derived tag would be
 * empty while the screen claims a release.
 */
fun sourceCodeUrl(versionName: String): String =
    if (releaseVersionPattern.matches(versionName)) {
        "$SOURCE_REPOSITORY_URL/tree/v$versionName"
    } else {
        SOURCE_REPOSITORY_URL
    }
