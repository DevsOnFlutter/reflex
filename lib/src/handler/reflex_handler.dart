/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'dart:io';

import 'package:flutter/services.dart';
import 'package:reflex/src/helper/events/notification_event.dart';
import 'package:reflex/src/helper/exception/reflex_exception.dart';
import 'package:reflex/src/helper/helper.dart';
import 'package:reflex/src/helper/utils/reflex_utils.dart';
import 'package:reflex/src/platform/reflex_platform.dart';

class ReflexHandler extends ReflexPlatform {
  // static const MethodChannel _methodChannel = MethodChannel('reflex');

  final EventChannel _eventChannel = const EventChannel('reflex_event_channel');

  // MethodChannel get methodChannel => _methodChannel;
  EventChannel get eventChannel => _eventChannel;

  Stream<NotificationEvent>? _notificationStream;

  late final List<dynamic> arguments;

  @override
  void init({
    required bool debug,
    List<String>? packageNameList,
    List<String>? packageNameExceptionList,
    AutoReply? autoReply,
  }) {
    // throw exception if package
    if (packageNameList != null && packageNameExceptionList != null) {
      ReflexUtils.checkConflictingPackageNames(
          packageNameList, packageNameExceptionList);
    }

    Map<String, dynamic> map = {
      "debug": debug,
      "packageNameList": packageNameList,
      "packageNameExceptionList": packageNameExceptionList,
      "autoReply": autoReply?.deserialize(),
    };

    arguments = [map];
  }

  @override
  Stream<NotificationEvent>? get notificationStream {
    if (Platform.isAndroid) {
      _notificationStream ??= _eventChannel
          .receiveBroadcastStream(arguments)
          .map<NotificationEvent>(
            (event) => NotificationEvent.getNotificationEventFromMap(event),
          );
      return _notificationStream;
    }
    throw ReflexException('notificationStream is only supported on Android!');
  }
}
