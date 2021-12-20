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
  StreamSubscription<ReflexEvent>? _subscription;
  final List<ReflexEvent> _notificationLogs = [];
  final List<ReflexEvent> _autoReplyLogs = [];
  bool isListening = false;

  Reflex reflex = Reflex(
    debug: true,
    packageNameList: ["com.whatsapp", "com.tyup"],
    packageNameExceptionList: ["com.android.systemui"],
    autoReply: AutoReply(
      packageNameList: ["com.whatsapp"],
      message: "[Reflex] This is an automated reply.",
    ),
  );

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    startListening();
  }

  void onData(ReflexEvent event) {
    setState(() {
      if (event.type == ReflexEventType.notification) {
        _notificationLogs.add(event);
      } else if (event.type == ReflexEventType.reply) {
        _autoReplyLogs.add(event);
      }
    });
    debugPrint(event.toString());
  }

  void startListening() {
    try {
      _subscription = reflex.notificationStream!.listen(onData);
      setState(() {
        isListening = true;
      });
    } on ReflexException catch (exception) {
      debugPrint(exception.toString());
    }
  }

  void stopListening() {
    _subscription?.cancel();
    setState(() => isListening = false);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Reflex Example app'),
        ),
        body: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              notificationListener(),
              permissions(),
              autoReply(),
            ],
          ),
        ),
      ),
    );
  }

  Widget permissions() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        ElevatedButton(
          child: const Text("See Permission"),
          onPressed: () async {
            bool isPermissionGranted = await Reflex.isPermissionGranted;
            debugPrint("Notification Permission: $isPermissionGranted");
          },
        ),
        ElevatedButton(
          child: const Text("Request Permission"),
          onPressed: () async {
            await Reflex.requestPermission();
          },
        ),
      ],
    );
  }

  Widget notificationListener() {
    return SizedBox(
      height: 400,
      child: Column(
        children: [
          SizedBox(
            height: 300,
            child: ListView.builder(
              reverse: true,
              itemCount: _notificationLogs.length,
              itemBuilder: (BuildContext context, int index) {
                final ReflexEvent element = _notificationLogs[index];
                return ListTile(
                  title: Text(element.title ?? ""),
                  subtitle: Text(element.message ?? ""),
                  trailing: Text(
                    element.packageName.toString().split('.').last,
                  ),
                );
              },
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton.icon(
                icon: isListening
                    ? const Icon(Icons.stop)
                    : const Icon(Icons.play_arrow),
                label: const Text("Reflex Notification Listener"),
                onPressed: () {
                  if (isListening) {
                    stopListening();
                  } else {
                    startListening();
                  }
                },
              ),
              if (_notificationLogs.isNotEmpty)
                ElevatedButton.icon(
                  style: ElevatedButton.styleFrom(
                    primary: Colors.red,
                  ),
                  icon: const Icon(Icons.clear),
                  label: const Text(
                    "Clear List",
                    style: TextStyle(
                      color: Colors.white,
                    ),
                  ),
                  onPressed: () {
                    setState(() {
                      _notificationLogs.clear();
                    });
                  },
                ),
            ],
          ),
        ],
      ),
    );
  }

  Widget autoReply() {
    return SizedBox(
      height: 400,
      child: Column(
        children: [
          SizedBox(
            height: 300,
            child: ListView.builder(
              itemCount: _autoReplyLogs.length,
              itemBuilder: (BuildContext context, int index) {
                final ReflexEvent element = _autoReplyLogs[index];
                return ListTile(
                  title: Text("AutoReply to: ${element.title}"),
                  subtitle: Text(element.message ?? ""),
                  trailing: Text(
                    element.packageName.toString().split('.').last,
                  ),
                );
              },
            ),
          ),
          if (_autoReplyLogs.isNotEmpty)
            ElevatedButton.icon(
              style: ElevatedButton.styleFrom(
                primary: Colors.red,
              ),
              icon: const Icon(Icons.clear),
              label: const Text(
                "Clear List",
                style: TextStyle(
                  color: Colors.white,
                ),
              ),
              onPressed: () {
                setState(() {
                  _autoReplyLogs.clear();
                });
              },
            ),
        ],
      ),
    );
  }
}
