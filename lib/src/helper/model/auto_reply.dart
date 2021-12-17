/* 

                  Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

class AutoReply {
  AutoReply({
    required this.packageName,
    required this.message,
  });

  /// Package Name for [AutoReply]
  String packageName;

  /// Notification Message
  String message;

  factory AutoReply.fromMap(Map<dynamic, dynamic> map) {
    // Error Handling if map does not contain package name or message
    if (!map.containsKey('packageName') || !map.containsKey('message')) {
      throw Exception('Map does not contain package name or message');
    }

    String packageName = map['packageName'];
    String message = map['message'];

    return AutoReply(
      packageName: packageName,
      message: message,
    );
  }

  Map<String, dynamic> deserialize() {
    return <String, dynamic>{
      'packageName': packageName,
      'message': message,
    };
  }
}
