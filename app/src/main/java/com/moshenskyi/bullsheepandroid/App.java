package com.moshenskyi.bullsheepandroid;

import android.app.Application;

public class App extends Application {

    private static App context;

    public App() {
        context = this;
    }

    public static App getContext() {
        return context;
    }
}
