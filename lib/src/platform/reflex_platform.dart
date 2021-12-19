/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:reflex/src/handler/reflex_handler.dart';
import 'package:reflex/src/helper/events/notification_event.dart';
import 'package:reflex/src/helper/helper.dart';

abstract class ReflexPlatform extends PlatformInterface {
  ReflexPlatform() : super(token: _token);

  static final Object _token = Object();

  static final ReflexPlatform _instance = ReflexHandler();

  /// returns the instance of the [ReflexHandler].
  static ReflexPlatform get instance => _instance;

  void init({
    required bool debug,
    List<String>? packageNameList,
    List<String>? packageNameExceptionList,
    AutoReply? autoReply,
  }) {
    throw UnimplementedError('init has not been implemented for reflex.');
  }

  Stream<NotificationEvent>? get notificationStream {
    throw UnimplementedError('notificationStream has not been implemented');
  }

  Future<bool> get isPermissionGranted async {
    throw UnimplementedError('isPermissionGranted has not been implemented!');
  }
}
