import 'package:reflex/src/helper/exception/reflex_exception.dart';

class ReflexUtils {
  ReflexUtils._();

  static void checkConflictingPackageNames(
    List<String> packageNameList,
    List<String> packageNameExceptionList,
  ) {
    for (String packageName in packageNameList) {
      if (packageNameExceptionList.contains(packageName)) {
        throw ReflexException(
            "Found $packageName both in packageName and packageNameExceptionList!");
      }
    }
  }
}
