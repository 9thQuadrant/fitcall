# Mock Call Reminder

A Cordova application that delivers reminders as mock incoming calls. When triggered, the app presents a full-screen incoming call UI (similar to WhatsApp). On answer, a robotic voice reads the reminder text aloud via TTS.

---

## Motivation

Standard alarms and notifications are easy to dismiss or ignore. A full-screen incoming call UI demands attention and forces a deliberate action (answer or decline), making it harder to unconsciously swipe away a reminder.

---

## Architecture

```
┌─────────────────────────────────┐
│         Cordova Web Layer        │
│  - Reminder config UI            │
│  - Schedule management           │
│  - Call screen UI (HTML/CSS/JS)  │
└────────────┬────────────────────┘
             │ Cordova Plugin Bridge
┌────────────▼────────────────────┐
│         Java Plugin Layer        │
│  - AlarmManager scheduling       │
│  - Full-screen intent / overlay  │
│  - TTS (TextToSpeech API)        │
│  - Wake lock                     │
└─────────────────────────────────┘
```

---

## Features

- Schedule recurring reminders at fixed intervals (e.g., every 2 hours)
- Full-screen mock incoming call UI — renders over lock screen
- Answer triggers TTS playback of the reminder message
- Decline dismisses without playback
- Custom reminder text per schedule entry
- Survives device sleep via `AlarmManager` + wake lock

---

## Tech Stack

| Layer | Technology |
|---|---|
| App framework | Apache Cordova |
| Web UI | HTML / CSS / JavaScript |
| Native plugin | Java (Android) |
| Scheduling | Android `AlarmManager` (exact alarms) |
| Call UI overlay | `FLAG_SHOW_WHEN_LOCKED` + `SYSTEM_ALERT_WINDOW` |
| Text-to-Speech | Android `TextToSpeech` API |
| Wake lock | `PowerManager.WakeLock` |

---

## How It Works

1. User sets a reminder — text and interval (e.g., "Drink water", every 2 hours)
2. Cordova JS calls the native plugin via `cordova.exec()`
3. Java plugin registers an exact alarm with `AlarmManager`
4. On trigger, a `BroadcastReceiver` fires and launches the call screen `Activity`
5. Call screen renders over the lock screen with Answer / Decline buttons
6. On Answer — `TextToSpeech` speaks the reminder message
7. On Decline — activity finishes, alarm is not rescheduled until the next interval

---

## Project Structure

```
mock-call-reminder/
├── www/
│   ├── index.html          # Reminder config UI
│   ├── call-screen.html    # Incoming call UI
│   ├── css/
│   │   └── call.css
│   └── js/
│       ├── app.js          # Schedule management
│       └── bridge.js       # cordova.exec() wrappers
├── plugins/
│   └── cordova-plugin-mock-call/
│       ├── plugin.xml
│       ├── www/
│       │   └── MockCall.js         # JS interface
│       └── src/android/
│           ├── MockCallPlugin.java      # Cordova plugin entry
│           ├── AlarmReceiver.java       # BroadcastReceiver
│           ├── CallScreenActivity.java  # Full-screen call UI
│           └── TTSManager.java          # TextToSpeech wrapper
├── config.xml
└── package.json
```

---

## Plugin API

```javascript
// Schedule a recurring reminder
MockCall.schedule({
  message: "Reminder to drink water",
  intervalHours: 2
}, onSuccess, onError);

// Cancel all scheduled reminders
MockCall.cancelAll(onSuccess, onError);
```

---

## Android Permissions

Declared in `plugin.xml`:

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />
```

> **Android 12+**: `SCHEDULE_EXACT_ALARM` requires explicit user grant via system settings. The app should deep-link the user to `ACTION_REQUEST_SCHEDULE_EXACT_ALARM` on first launch.

---

## Known Constraints

- **Android only** — iOS does not permit call screen overlays or background exact scheduling without VoIP entitlements (CallKit), which requires Apple developer approval.
- **Android 10+ overlay restrictions** — `SYSTEM_ALERT_WINDOW` behavior changed. `CallScreenActivity` must use `FLAG_SHOW_WHEN_LOCKED` + `FLAG_TURN_SCREEN_ON` window flags, not a floating overlay.
- **Doze mode** — `AlarmManager.setExactAndAllowWhileIdle()` required for alarms to fire in Doze. Still subject to alignment windows on some OEMs (Xiaomi, Huawei).
- **OEM battery optimization** — Users on aggressive OEM ROMs (MIUI, EMUI) must whitelist the app manually. Worth surfacing in onboarding.

---

## Setup

```bash
# Install Cordova
npm install -g cordova

# Create project
cordova create mock-call-reminder com.example.mockcall MockCallReminder
cd mock-call-reminder

# Add Android platform
cordova platform add android

# Add the plugin (local)
cordova plugin add ../cordova-plugin-mock-call

# Build
cordova build android

# Run on device
cordova run android --device
```

---

## Development Notes

- Built and tested circa 2018–2019 on Cordova 8.x, Android API 21–28
- `AlarmManager` exact alarm behavior has changed significantly across API levels — see Android docs for per-version handling
- If reviving this project today, evaluate **Capacitor** as a drop-in Cordova successor with better Android 12+ support
- For a purely native path, **Kotlin + WorkManager + Notification (full-screen intent)** is now the canonical approach for this use case

---

## License

MIT
