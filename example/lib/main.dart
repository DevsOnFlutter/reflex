import 'dart:async';

import 'package:flutter/material.dart';
import 'package:reflex/reflex.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({
    Key? key,
  }) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Reflex? _reflex;
  StreamSubscription<NotificationEvent>? _subscription;
  final List<NotificationEvent> _log = [];
  bool started = false;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    startListening();
  }

  void onData(NotificationEvent event) {
    setState(() {
      _log.add(event);
    });
    debugPrint(event.toString());
  }

  void startListening() {
    _reflex = Reflex();
    try {
      _subscription = _reflex!.notificationStream!.listen(onData);
      setState(() {
        started = true;
      });
    } on ReflexException catch (exception) {
      debugPrint(exception.toString());
    }
  }

  void stopListening() {
    _subscription?.cancel();
    setState(() => started = false);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Reflex Example app'),
        ),
        body: Center(
          child: ListView.builder(
            itemCount: _log.length,
            itemBuilder: (BuildContext context, int idx) {
              final entry = _log[idx];
              return ListTile(
                title: Text(entry.title ?? ""),
                subtitle: Text(entry.message ?? ""),
                trailing: Text(entry.packageName.toString().split('.').last),
              );
            },
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: started ? stopListening : startListening,
          tooltip: 'Start/Stop listening',
          child:
              started ? const Icon(Icons.stop) : const Icon(Icons.play_arrow),
        ),
      ),
    );
  }
}
