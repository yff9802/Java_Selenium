package com.yff.utils;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * @author YFF
 * @date 2020/4/4
 */
public class MyRemoteWebDriver extends RemoteWebDriver implements HasTouchScreen {
    public TouchScreen touchScreen;

    public MyRemoteWebDriver(URL remoteAddress, Capabilities capabilities){
        super(remoteAddress, capabilities);
        this.touchScreen = new RemoteTouchScreen(this.getExecuteMethod());
    }

    @Override
    public TouchScreen getTouch() {
        return this.touchScreen;
    }
}
