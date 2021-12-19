import 'package:reflex/src/helper/exception/reflex_exception.dart';

class ReflexUtils {
  ReflexUtils._();

  static String? checkConflictingPackageNames(
    List<String> packageNameList,
    List<String> packageNameExceptionList,
  ) {
    for (String packageName in packageNameList) {
      if (packageNameExceptionList.contains(packageName)) {
        return packageName;
      }
    }
  }

  static String? checkPackagePresence(List<String> list1, List<String> list2) {
    // Chekcing if presence of list 2 in list 1
    for (String packageName in list2) {
      if (!list1.contains(packageName)) {
        return packageName;
        
      }
    }
  }
}
