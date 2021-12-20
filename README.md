# Reflex

Flutter plugin for notification read & reply.

<img src="https://i.imgur.com/Y8kadag.png" title="Flutter_Shortcuts"/>

![GitHub](https://img.shields.io/github/license/DevsOnFlutter/reflex?style=plastic) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/DevsOnFlutter/reflex?style=plastic) ![GitHub top language](https://img.shields.io/github/languages/top/DevsOnFlutter/reflex?style=plastic) ![GitHub language count](https://img.shields.io/github/languages/count/DevsOnFlutter/reflex?style=plastic) ![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/DevsOnFlutter/reflex?style=plastic) ![GitHub issues](https://img.shields.io/github/issues/DevsOnFlutter/reflex?style=plastic) ![GitHub Repo stars](https://img.shields.io/github/stars/DevsOnFlutter/reflex?style=social) ![GitHub forks](https://img.shields.io/github/forks/DevsOnFlutter/reflex?style=social)

## Compatibility

✅ &nbsp; Android </br>
❌ &nbsp; iOS (active issue: [iOS support for reflex](https://github.com/DevsOnFlutter/reflex/issues/1))

## Show some :heart: and :star: the repo

## Why use Reflex?

Reflex Plugin is known for:

| Reflex |
| :--------------------------------- |
| Fast, performant & compatible |
| Free & Open-source |
| Production ready |
| Make App Reactive |

## Features

All the features listed below can be performed at the runtime.

✅ &nbsp; Get Notification Stream </br>
✅ &nbsp; Read Notification </br>
✅ &nbsp; Reply From Notification </br>
✅ &nbsp; Auto Reply </br>

## Demo

|<img height=500 src="https://i.imgur.com/pG2qr2J.gif"/>|
|---|

## Quick Start

### Step 1: Include plugin to your project

```yml
dependencies:
  reflex: <latest version>
```

Run `pub get` and get packages.

### Step 2: Add Service in `AndroidManifest.xml`

Add the following service inside the `application` tag of `AndroidManifest.xml`.

```xml
    ...
    <service
        android:label="notifications"
        android:name="com.devsonflutter.reflex.notification.NotificationListener"
        android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
        <intent-filter>
            <action android:name="android.service.notification.NotificationListenerService" />
        </intent-filter>
    </service>
</application>
```

**Android 12+ Compatibility**
Add `android:exported="true"` field in the service tag to make it compatible with Android 12+.

```xml
    ...
    <service
        android:label="notifications"
        android:name="com.devsonflutter.reflex.notification.NotificationListener"
        android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE"
        android:exported="true">
        <intent-filter>
            <action android:name="android.service.notification.NotificationListenerService" />
        </intent-filter>
    </service>
</application>
```

### Step 3: Instantiate Reflex

Reflex must only be instantiated only once.

```dart
Reflex reflex = Reflex();
```


## Example

Go to example section in pub.dev to see the full example code.

In GitHub, head over to `example/lib/main.dart` to see the full example code.

### Import

This single import is enough for using reflex.

```dart
import 'package:reflex/reflex.dart';
```

### Listen & Reply

```dart
StreamSubscription<ReflexEvent>? _subscription;
final List<ReflexEvent> _notificationLogs = [];
final List<ReflexEvent> _autoReplyLogs = [];
bool isListening = false;

Reflex reflex = Reflex(
  debug: true,
  packageNameList: ["com.whatsapp", "com.tyup"],
  packageNameExceptionList: ["com.android.systemui"],
  autoReply: AutoReply(
    packageNameList: ["com.whatsapp"],
    message: "[Reflex] This is an automated reply.",
  ),
);

@override
void initState() {
  super.initState();
  initPlatformState();
}

Future<void> initPlatformState() async {
  startListening();
}

void onData(ReflexEvent event) {
  setState(() {
    if (event.type == ReflexEventType.notification) {
      _notificationLogs.add(event);
    } else if (event.type == ReflexEventType.reply) {
      _autoReplyLogs.add(event);
    }
  });
  debugPrint(event.toString());
}

void startListening() {
  try {
    _subscription = reflex.notificationStream!.listen(onData);
    setState(() {
      isListening = true;
    });
  } on ReflexException catch (exception) {
    debugPrint(exception.toString());
  }
}

void stopListening() {
  _subscription?.cancel();
  setState(() => isListening = false);
}
```

## Quick Guide

A quick guide to **Flutter Reflex plugin**!

### Debugging

Debugging allows you to debug the plugin's functionality, logs are shown in the console.

By default debug logging is enabled. You can configure it using the debug field in the Reflex class.


```dart
Reflex reflex = Reflex(
  debug: false,
);
```

### Listen notification permission

See if listening notification permission has been granted or not.

```dart
bool isPermissionGranted = await Reflex.isPermissionGranted;
```

### Request notification listening permission

Use the function to grant notification listening permission.

```dart
await Reflex.requestPermission();
```

### Notification Stream

Use the reflex object to get a notification stream to listen to notifications in your flutter application.

```dart
StreamSubscription<ReflexEvent>? _subscription;
_subscription = reflex.notificationStream!.listen((event) {
  // Application Logic
});
```

The stream is subscribed for `ReflexEvent` whenever a notification is received.

#### Reflex Event

The incoming reflex event contains:

* **type**: `ReflexEventType.notification` whenever a notification is received to flutter application, and `ReflexEventType.reply` whenever an automated reply is sent.

* **packageName**: Application's package name from which notifications are received and reply are sent.

* **title**: Notification title

* **message**: Message contained in the notification and while sending reply.
  
* **timestamp**: Timestamp of the notification received and reply sent.

### Listen notification from specific apps

Specify list of package names to listen to notifications from those applications.

If `packageNameList: null` plugin will listen to notifications from all packages.

```dart
Reflex reflex = Reflex(
  debug: true,
  packageNameList: ["com.whatsapp", "com.facebook"],
);
```

### Avoid notification from specific apps

Specify package name exception list to avoid listening notifications from those applications.

If `packageNameExceptionList: null`, the plugin will listen to notifications for `packageNameList` if not null.

```dart
Reflex reflex = Reflex(
  debug: true,
  packageNameExceptionList: ["com.whatsapp"],
);
```

### Auto Reply

Send an automated reply while listening notification.

```dart
AutoReply autoReply = AutoReply(
  message: "[Reflex] This is an automated reply.",
),
```

#### Auto reply specific apps

Specify `packageNameList` in AutoReply to reply to specific applications.

```dart
AutoReply autoReply = AutoReply(
  packageNameList: ["com.whatsapp"],
  message: "[Reflex] This is an automated reply.",
),
```

The `AutoReply` object is used by the Reflex's `autoReply` field to automatically reply to applications.

```dart
Reflex reflex = Reflex(
  debug: true,
  packageNameList: ["com.whatsapp", "com.tyup"],
  packageNameExceptionList: ["com.miui.securitycenter"],
  autoReply: AutoReply(
    packageNameList: ["com.whatsapp"],
    message: "[Reflex] This is an automated reply.",
  ),
);
```

If the `autoReply` field is `null` in `Reflex` class, Auto reply feature will be disabled.

## Ambiguity

A `ReflexException` will be thrown if,

* `Reflex`'s `packageNameList` and `packageNameExceptionList` contains any similar package name.

* any package name in `AutoReply`'s `packageNameList` is contained in `Reflex`'s `packageNameExceptionList`.

## Project Created & Maintained By

### Divyanshu Shekhar

<a href="https://twitter.com/dshekhar17"><img src="https://github.com/aritraroy/social-icons/blob/master/twitter-icon.png?raw=true" width="60"></a> <a href="https://in.linkedin.com/in/dshekhar17"><img src="https://github.com/aritraroy/social-icons/blob/master/linkedin-icon.png?raw=true" width="60"></a> <a href="https://instagram.com/dshekhar17"><img src="https://github.com/aritraroy/social-icons/blob/master/instagram-icon.png?raw=true" width="60"></a>

[![GitHub followers](https://img.shields.io/github/followers/divshekhar.svg?style=social&label=Follow)](https://github.com/divshekhar/)

### Subham Praharaj

<a href="https://twitter.com/SubhamPraharaj6"><img src="https://github.com/aritraroy/social-icons/blob/master/twitter-icon.png?raw=true" width="60"></a> <a href="https://www.linkedin.com/in/subham-praharaj-66b172179/"><img src="https://github.com/aritraroy/social-icons/blob/master/linkedin-icon.png?raw=true" width="60"></a> <a href="https://instagram.com/the_champ_subham_865"><img src="https://github.com/aritraroy/social-icons/blob/master/instagram-icon.png?raw=true" width="60"></a>

[![GitHub followers](https://img.shields.io/github/followers/skpraharaj.svg?style=social&label=Follow)](https://github.com/skpraharaj/)

## Contributions

Contributions are welcomed!

**If you feel that a hook is missing, feel free to open a pull-request.**

For a custom-hook to be merged, you will need to do the following:

* Describe the use-case.

* Open an issue explaining why we need this hook, how to use it, ...
  This is important as a hook will not get merged if the hook doens't appeal to
  a large number of people.

* If your hook is rejected, don't worry! A rejection doesn't mean that it won't
  be merged later in the future if more people shows an interest in it.
  In the mean-time, feel free to publish your hook as a package on [https://pub.dev](https://pub.dev).

* A hook will not be merged unles fully tested, to avoid breaking it inadvertendly in the future.
  
## Stargazers

[![Stargazers repo roster for @DevsOnFlutter/reflex](https://reporoster.com/stars/dark/DevsOnFlutter/reflex)](https://github.com/DevsOnFlutter/reflex/stargazers)

## Forkers

[![Forkers repo roster for @DevsOnFlutter/reflex](https://reporoster.com/forks/dark/DevsOnFlutter/reflex)](https://github.com/DevsOnFlutter/reflex/network/members)

## Copyright & License

Code and documentation Copyright (c) 2021 [Divyanshu Shekhar](https://hackthedeveloper.com). Code released under the [BSD 3-Clause License](./LICENSE).
