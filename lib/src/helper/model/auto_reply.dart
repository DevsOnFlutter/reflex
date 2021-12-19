/* 

                  Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

class AutoReply {
  AutoReply({
    this.packageNameList,
    required this.message,
  });

  /// Package Name List for [AutoReply]
  List<String>? packageNameList;

  /// Notification Message
  String message;

  factory AutoReply.fromMap(Map<dynamic, dynamic> map) {
    // Error Handling if map does not contain package name or message
    if (!map.containsKey('message')) {
      // TODO: Create own exception
      throw Exception('AutoReply do not contain message!');
    }

    List<String>? packageNameList =
        map.containsKey('packageNameList') ? map['packageNameList'] : null;
    String message = map['message'];

    return AutoReply(
      packageNameList: packageNameList,
      message: message,
    );
  }

  Map<String, dynamic> deserialize() {
    return <String, dynamic>{
      'packageNameList': packageNameList,
      'message': message,
    };
  }
}
