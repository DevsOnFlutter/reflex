/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

import 'package:reflex/src/helper/helper.dart';

class ReflexEvent {
  ReflexEvent({
    this.type,
    this.packageName,
    this.title,
    this.message,
    this.timeStamp,
  });

  /// Event type Notification/Reply
  ReflexEventType? type;

  /// The package name of the app that sent the notification.
  String? packageName;

  /// Notification Title
  String? title;

  /// Notification Message
  String? message;

  /// The time stamp of the notification.
  DateTime? timeStamp;

  factory ReflexEvent.fromMap(Map<dynamic, dynamic> map) {
    DateTime time = DateTime.now();
    String? name = map['packageName'];
    String? message = map['message'];
    String? title = map['title'];

    ReflexEventType? type;

    if (map['type'] == 'notification') {
      type = ReflexEventType.notification;
    } else if (map['type'] == 'reply') {
      type = ReflexEventType.reply;
    }

    return ReflexEvent(
      type: type,
      packageName: name,
      title: title,
      message: message,
      timeStamp: time,
    );
  }

  static ReflexEvent getReflexEventFromMap(dynamic data) {
    return ReflexEvent.fromMap(data);
  }

  @override
  String toString() {
    return '''$runtimeType
    type: $type,
    Package Name: $packageName, 
    Title: $title, 
    Message: $message, 
    Timestamp: $timeStamp''';
  }
}
