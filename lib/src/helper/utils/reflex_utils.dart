/* 

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

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
