/*

               Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex.notification.model;

public class App {
    private final String name;
    private final String packageName;

    public App(String name, String packageName)
    {
        this.name = name;
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }
}
