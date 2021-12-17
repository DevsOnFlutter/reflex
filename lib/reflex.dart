import 'dart:async';

import 'package:reflex/src/platform/reflex_platform.dart';

export 'package:reflex/src/helper/helper.dart';

class Reflex {
  /// [notificationStream] returns stream of notifications.
  Stream<String> get notificationStream {
    return ReflexPlatform.instance.notificationStream;
  }
}
