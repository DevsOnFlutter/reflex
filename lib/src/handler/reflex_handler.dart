/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'dart:io';

import 'package:flutter/services.dart';
import 'package:reflex/src/helper/events/reflex_event.dart';
import 'package:reflex/src/helper/exception/reflex_exception.dart';
import 'package:reflex/src/helper/helper.dart';
import 'package:reflex/src/helper/utils/reflex_utils.dart';
// import 'package:reflex/src/platform/reflex_platform.dart';

class ReflexHandler {
  static const MethodChannel _methodChannel =
      MethodChannel('reflex_method_channel');

  final EventChannel _eventChannel = const EventChannel('reflex_event_channel');

  MethodChannel get methodChannel => _methodChannel;
  EventChannel get eventChannel => _eventChannel;

  Stream<ReflexEvent>? _notificationStream;

  late final List<dynamic> arguments;

  void init({
    required bool debug,
    List<String>? packageNameList,
    List<String>? packageNameExceptionList,
    AutoReply? autoReply,
  }) {
    // Check conflicting package name list and exception list
    if (packageNameList != null && packageNameExceptionList != null) {
      String? packageName = ReflexUtils.checkConflictingPackageNames(
          packageNameList, packageNameExceptionList);

      if (packageName != null) {
        throw ReflexException(
            "Found $packageName both in packageNameList and packageNameExceptionList of Reflex!");
      }
    }

    if (autoReply != null && autoReply.packageNameList != null) {
      // Check if package in autoReply.packageNameList is present in packageNameList
      if (packageNameList != null) {
        String? packageName = ReflexUtils.checkPackagePresence(
            packageNameList, autoReply.packageNameList ?? []);

        if (packageName != null) {
          throw ReflexException(
              "You are not listening for $packageName, but you are using it for AutoReply!");
        }
      }

      // Check conflicting auto reply packageNameList and packageNameExceptionList
      if (packageNameList == null && packageNameExceptionList != null) {
        String? packageName = ReflexUtils.checkConflictingPackageNames(
            packageNameExceptionList, autoReply.packageNameList ?? []);

        if (packageName != null) {
          throw ReflexException(
              "Found $packageName both in packageNameExceptionList and autoReply.packageNameList!");
        }
      }
    }

    Map<String, dynamic> map = {
      "debug": debug,
      "packageNameList": packageNameList,
      "packageNameExceptionList": packageNameExceptionList,
      "autoReply": autoReply?.deserialize(),
    };

    arguments = [map];
  }

  Stream<ReflexEvent>? get notificationStream {
    if (Platform.isAndroid) {
      _notificationStream ??=
          _eventChannel.receiveBroadcastStream(arguments).map<ReflexEvent>(
                (event) => ReflexEvent.getReflexEventFromMap(event),
              );
      return _notificationStream;
    }
    throw ReflexException('notificationStream is only supported on Android!');
  }

  Future<bool> get isPermissionGranted async {
    bool? result =
        (await _methodChannel.invokeMethod<bool>('isPermissionGranted')) ??
            false;
    return result;
  }

  Future<void> requestPermission() async {
    await _methodChannel.invokeMethod('requestPermission');
  }
}
