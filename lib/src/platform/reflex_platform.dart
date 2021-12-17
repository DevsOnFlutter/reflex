/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:reflex/src/handler/reflex_handler.dart';
import 'package:reflex/src/helper/events/notification_event.dart';

abstract class ReflexPlatform extends PlatformInterface {
  ReflexPlatform() : super(token: _token);

  static final Object _token = Object();

  static ReflexPlatform _instance = ReflexHandler();

  /// returns the instance of the [ReflexHandler].
  static ReflexPlatform get instance => _instance;

  static set instance(ReflexPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Stream<NotificationEvent>? get notificationStream {
    throw UnimplementedError('notificationStream has not been implemented');
  }
}
