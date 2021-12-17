import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:reflex/reflex.dart';

void main() {
  const MethodChannel channel = MethodChannel('reflex');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Reflex.platformVersion, '42');
  });
}
