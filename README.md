# Sky Tapper (Android)

A lightweight one-touch arcade game built with Kotlin + Jetpack Compose.

## Gameplay
- A comet falls through one of 3 lanes.
- Tap the correct lane before it reaches the bottom.
- Wrong taps or missed comets cost lives.
- The run ends after 3 misses.

## Build
1. Open in Android Studio (Ladybug or newer recommended).
2. Let Gradle sync.
3. Run on an emulator or device with Android 7.0+.

## Google Play Store readiness checklist
Before publishing, complete these production steps:
- Replace launcher icons and screenshots.
- Add Privacy Policy URL in Play Console.
- Add app signing key + configure Play App Signing.
- Build release bundle: `./gradlew bundleRelease`.
- Fill Store Listing text, category, content rating, and target audience.
- Test internal track before production rollout.

## Suggested next features
- Add sound effects and haptic feedback.
- Add rewarded ad to continue after one miss.
- Persist `bestScore` with DataStore.
- Add achievements/leaderboard via Play Games Services.
