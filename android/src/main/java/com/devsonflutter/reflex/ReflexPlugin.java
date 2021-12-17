/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;

/* ReflexPlugin */
public class ReflexPlugin implements FlutterPlugin {

  /* ------------- Native Variables -------------- */
  @SuppressLint("StaticFieldLeak")
  public static Context context = null;

  private BinaryMessenger binaryMessenger = null;

//  private MethodChannel methodChannel;
//  private MethodCallHandlerImplementation methodHandler;

  @SuppressLint("StaticFieldLeak")
  private static EventCallHandler eventHandler;

  private EventChannel eventChannel;
  /* ------------- Native Variables -------------- */

  /* ------------- Method Channel -------------- */
  private static final String CHANNEL_ID = "reflex_method_channel";

  public static String getChannelId() {
    return CHANNEL_ID;
  }
  /* ------------- Method Channel -------------- */

  /* ------------- Event Channel -------------- */
  private static final String STREAM_ID = "reflex_event_channel";

  public static String getStreamId() {
    return STREAM_ID;
  }
  /* ------------- Event Channel -------------- */

  /* ------------- Plugin Logging TAG -------------- */
  private static final String TAG = "[Reflex]";

  public static String getPluginTag() {
    return TAG;
  }
  /* ------------- Plugin Logging TAG -------------- */

  private void setupChannel(BinaryMessenger messenger, Context context) {
//    methodChannel = new MethodChannel(binaryMessenger, CHANNEL_ID);
    eventChannel = new EventChannel(binaryMessenger,STREAM_ID);

//    methodHandler = new MethodCallHandlerImplementation();
    eventHandler = new EventCallHandler(context);

//    methodChannel.setMethodCallHandler(methodHandler);
    eventChannel.setStreamHandler(eventHandler);
  }

  private void teardownChannel() {
//    methodChannel.setMethodCallHandler(null);
    eventChannel.setStreamHandler(null);
    binaryMessenger = null;
//    methodChannel = null;
//    methodHandler = null;
    eventChannel = null;
    eventHandler = null;
    context = null;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    binaryMessenger = flutterPluginBinding.getBinaryMessenger();
    context = flutterPluginBinding.getApplicationContext();
    setupChannel(binaryMessenger, context);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    teardownChannel();
  }
}
