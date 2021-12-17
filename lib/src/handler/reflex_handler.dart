/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'dart:io';

import 'package:flutter/services.dart';
import 'package:reflex/src/helper/exception/reflex_exception.dart';
import 'package:reflex/src/platform/reflex_platform.dart';

class ReflexHandler extends ReflexPlatform {
  // static const MethodChannel _methodChannel = MethodChannel('reflex');

  final EventChannel _eventChannel = const EventChannel('reflex_event_channel');

  // MethodChannel get methodChannel => _methodChannel;
  EventChannel get eventChannel => _eventChannel;

  Stream<String>? _notificationStream;

  @override
  Stream<String> get notificationStream {
    if (Platform.isAndroid) {
      _notificationStream ??=
          _eventChannel.receiveBroadcastStream().map<String>((event) => event);
      return _notificationStream!;
    }
    throw ReflexException(
        'Reflex.notificationStream is only supported on Android.');
  }
}
