/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'dart:async';

import 'package:reflex/src/helper/events/reflex_event.dart';
import 'package:reflex/src/helper/helper.dart';
import 'package:reflex/src/platform/reflex_platform.dart';

export 'package:reflex/src/helper/helper.dart';

class Reflex {
  Reflex({
    this.debug = true,
    this.packageNameList,
    this.packageNameExceptionList,
    this.autoReply,
  }) {
    init();
  }

  /// [debug] logs on the terminal.
  /// debug is set to `true` by default.
  final bool debug;

  /// [autoReply] if not null, the plugin will send an automated reply to listening apps.
  final AutoReply? autoReply;

  /// [pacakgeNameList] list of package names to listen for notifications.
  final List<String>? packageNameList;

  /// [packageNameExceptionList] list of package names to avoid listening for notifications.
  final List<String>? packageNameExceptionList;

  static late final ReflexPlatform reflexPlatform;

  // Initialize [ReflexPlatform] with [debug] and [autoReply]
  void init() {
    reflexPlatform = ReflexPlatform.instance;
    reflexPlatform.init(
      debug: debug,
      packageNameList: packageNameList,
      packageNameExceptionList: packageNameExceptionList,
      autoReply: autoReply,
    );
  }

  /// [notificationStream] returns stream of notifications.
  Stream<ReflexEvent>? get notificationStream {
    return reflexPlatform.notificationStream;
  }

  static Future<bool> get isPermissionGranted async {
    return reflexPlatform.isPermissionGranted;
  }

  static Future<void> requestPermission() async {
    return reflexPlatform.requestPermission();
  }
}
