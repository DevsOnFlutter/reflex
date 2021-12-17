/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'dart:async';

import 'package:reflex/src/helper/events/notification_event.dart';
import 'package:reflex/src/helper/helper.dart';
import 'package:reflex/src/platform/reflex_platform.dart';

export 'package:reflex/src/helper/helper.dart';

class Reflex {
  /// [Reflex] Constructor
  Reflex({
    this.debug = false,
    this.autoReply,
  }) {
    init();
  }

  final bool debug;
  final AutoReply? autoReply;
  late final ReflexPlatform reflexPlatform;

  // Initialize [ReflexPlatform] with [debug] and [autoReply]
  void init() {
    reflexPlatform = ReflexPlatform.instance;
    reflexPlatform.init(debug: debug, autoReply: autoReply);
  }

  /// [notificationStream] returns stream of notifications.
  Stream<NotificationEvent>? get notificationStream {
    return reflexPlatform.notificationStream;
  }
}
