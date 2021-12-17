/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

class NotificationEvent {
  NotificationEvent({
    this.packageName,
    this.title,
    this.message,
    this.timeStamp,
  });

  /// The package name of the app that sent the notification.
  String? packageName;

  /// Notification Title
  String? title;

  /// Notification Message
  String? message;

  /// The time stamp of the notification.
  DateTime? timeStamp;

  factory NotificationEvent.fromMap(Map<dynamic, dynamic> map) {
    DateTime time = DateTime.now();
    String? name = map['packageName'];
    String? message = map['message'];
    String? title = map['title'];

    return NotificationEvent(
      packageName: name,
      title: title,
      message: message,
      timeStamp: time,
    );
  }

  static NotificationEvent getNotificationEventFromMap(dynamic data) {
    return NotificationEvent.fromMap(data);
  }

  @override
  String toString() {
    return '''$runtimeType
    Package Name: $packageName, 
    Title: $title, 
    Message: $message, 
    Timestamp: $timeStamp''';
  }
}
