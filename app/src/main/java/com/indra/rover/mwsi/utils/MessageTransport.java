package com.indra.rover.mwsi.utils;

import android.os.Bundle;

/**
 * Created by Indra on 9/7/2016.
 */
public class MessageTransport {

    String action;
    String message;
    Bundle bundle;

    public MessageTransport(String action,Bundle bundle){
        this.action = action;
        this.bundle = bundle;
    }

    public MessageTransport(String action, String message){
        this.action = action;
        this.message = message;
    }

}
