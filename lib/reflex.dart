/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'dart:async';

import 'package:reflex/src/platform/reflex_platform.dart';

export 'package:reflex/src/helper/helper.dart';

class Reflex {
  /// [notificationStream] returns stream of notifications.
  Stream<String> get notificationStream {
    return ReflexPlatform.instance.notificationStream;
  }
}
