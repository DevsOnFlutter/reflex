
import 'dart:async';

import 'package:flutter/services.dart';

class Reflex {
  static const MethodChannel _channel = MethodChannel('reflex');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
