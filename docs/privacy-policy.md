# Skyward privacy policy

Skyward reminds you about sky events — eclipses, aurora, meteor showers,
comets and more — from where you are or where you'd travel. It is a
local-only app: there is no Skyward account, no Skyward server, no analytics
or telemetry, and no advertising. Skyward does not collect, sell or share
personal data, and the developers receive nothing from your device.

This policy covers both the Android app (in every store it is distributed
through) and the Linux desktop app.

## What stays on your device

Your saved locations, reminder rules, settings, notification history, and
the event data Skyward downloads or computes are stored only on your device.
Eclipses, meteor showers, moon events, conjunctions and sky positions are
computed on-device without any network access.

If you export your settings, Skyward writes them to a file at a place you
choose. What happens to that file afterwards — copying it to another device,
a sync tool, or a backup — is up to you; Skyward does not upload it.

## Location

Location access is optional; you can always enter a location by hand
instead. If you grant it, Skyward reads your approximate (coarse) location
only to save it as one of your locations, and uses your saved locations to
work out what is visible from where you are.

Your saved locations stay on your device, with one exception: an
approximate region covering them — never your exact coordinates — may be
included in requests to NASA EONET to narrow the natural events it returns.
No other data provider receives anything derived from your locations.

## Network requests

Skyward's only network traffic is direct HTTPS requests from your device to
these public data providers:

- **NOAA Space Weather Prediction Center** (`services.swpc.noaa.gov`) — aurora
  forecasts. These requests carry no location.
- **NASA EONET** (`eonet.gsfc.nasa.gov`) — natural events such as volcanoes
  and wildfires, narrowed as described above.
- **NASA/JPL Small-Body Database** (`ssd-api.jpl.nasa.gov`) — comet orbits.
  These requests carry no location.

As with any HTTPS request, these providers can see your IP address and the
app's user-agent string, which identifies the app as Skyward but not you.
Their own privacy policies govern what they do with it.

When you choose to open a link — an event's page, a map of a viewing spot,
this policy or the source code — it opens in your browser or maps app, which
then talks to that website under its own privacy policy.

## Permissions (Android)

- **Internet** — for the data providers above.
- **Approximate location** — optional, as described above.
- **Notifications** — to show the reminders you set up.
- **Alarms and reminders** (exact alarms) — so reminders fire at the minute
  you asked for. It gives the app no access to your data.
- **Run at startup** — to restore scheduled reminders after a reboot.

## Children

Skyward is not directed at children and, collecting no personal data, holds
none about anyone of any age.

## Changes and contact

Changes to this policy are made in the open, in this file's history in the
Skyward source repository: <https://github.com/skywardapp/skyward>. Questions
can be raised in its issue tracker.
