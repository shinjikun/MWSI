package com.indra.rover.mwsi.utils;

import android.os.Bundle;

/**
 * Created by Indra on 9/7/2016.
 */
public class MessageTransport {

    private String action;
    private String message;
    private Bundle bundle;

    public MessageTransport(String action,Bundle bundle){
        this.action = action;
        this.bundle = bundle;
    }

    public MessageTransport(String action, String message){
        this.action = action;
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
