package dev.fritze.skyward.core.persistence

/**
 * A fresh, schema-created, in-memory [SkywardDatabase] for `commonTest`, so a
 * test of code that takes the concrete SQLDelight repos (SourceRunner, §6.2)
 * can run on both targets rather than only in `desktopTest` (issue #131).
 *
 * Both actuals use SQLDelight's JDBC SQLite driver. §17 runs `commonTest` on
 * Android as *local unit tests* — `androidUnitTest`, on the host JVM against
 * `android.jar` stubs — where the framework SQLite that `AndroidSqliteDriver`
 * wraps does not exist; reaching it would take Robolectric for one fixture.
 * The repos only see SQLDelight's driver interface, so what the shared suite
 * exercises on Android is the Android *compilation* of `commonMain` against
 * the same SQL — which is the platform difference a unit test can observe.
 */
expect fun inMemorySkywardDatabase(): SkywardDatabase
