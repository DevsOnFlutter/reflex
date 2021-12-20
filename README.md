# Reflex

Flutter plugin for notification read & reply.

<img src="https://i.imgur.com/kz6uoXm.png" title="Flutter_Shortcuts"/>

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

## Quick Start

### Step 1: Include plugin to your project

```yml
dependencies:
  reflex: <latest version>
```

Run pub get and get packages.

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

### Step 3

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
