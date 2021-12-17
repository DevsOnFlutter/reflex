#import "ReflexPlugin.h"
#if __has_include(<reflex/reflex-Swift.h>)
#import <reflex/reflex-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "reflex-Swift.h"
#endif

@implementation ReflexPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftReflexPlugin registerWithRegistrar:registrar];
}
@end
