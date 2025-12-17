package com.hypermarket.modules.components;

public abstract class ViewController {

    public Runnable onLogout;

    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    protected abstract void setUpNavigation();

    protected abstract void showDashboard();

}
