/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

class ReflexException implements Exception {
  String message;

  ReflexException(this.message);

  @override
  String toString() {
    return "ReflexException: $message";
  }
}

class ReflexNullException implements Exception {
  String message;

  ReflexNullException(this.message);

  @override
  String toString() {
    return "ReflexNullException: $message";
  }
}
