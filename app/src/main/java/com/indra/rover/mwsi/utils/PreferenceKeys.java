package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceKeys {

	private static PreferenceKeys instance;
	 String pref_name ="mwsi";
	SharedPreferences settings ;
	public PreferenceKeys(Context context) {
		//Context context =	MyApp.getContext();
		settings = context.getSharedPreferences(pref_name, 0);
	}
	
	public static PreferenceKeys getInstance(Context context) {
		if(instance == null){
		   instance = new PreferenceKeys(context);
		}
		return instance;
	}
	
	public void destroyKeys(){
		SharedPreferences.Editor  prefEditor = settings.edit();
		prefEditor.clear();
		prefEditor.apply();
	}
	
	
	public void setData(String key, String value){
		if(settings !=null){
			SharedPreferences.Editor  prefEditor = settings.edit();
			prefEditor.putString(key, value);
			prefEditor.apply();
		}
	}
	
	public void setData(String key, boolean value){
		if(settings !=null){
			SharedPreferences.Editor  prefEditor = settings.edit();
			prefEditor.putBoolean(key, value);
			prefEditor.apply();
		}
	}
	
	public void setData(String key, int value){
		if(settings !=null){
			SharedPreferences.Editor  prefEditor = settings.edit();
			prefEditor.putInt(key, value);
			prefEditor.apply();
		}
	}

    public void setData(String key, long value){
        if(settings !=null){
            SharedPreferences.Editor  prefEditor = settings.edit();
            prefEditor.putLong(key, value);
            prefEditor.apply();
        }
    }

	public String getData(String key){
		String value="";
		if(settings != null){
			value = settings.getString(key, "");
		}
		return value;
	}
	
	public boolean getBooleanData(String key){
		boolean value=false;
		if(settings != null){
			value = settings.getBoolean(key, value);
		}
		return value;
	}
	
	
	public String getData(String key,String defaultValue){
		String value=defaultValue;
		if(settings != null){
			value = settings.getString(key, defaultValue);
		}
		return value;
	}
	
	public boolean getData(String key, boolean defaultValue){
		boolean value=defaultValue;
		if(settings != null){
			value = settings.getBoolean(key, defaultValue);
		}
		return value;
	}
	
	
	public int getData(String key, int defaultValue){
		int value=defaultValue;
		if(settings != null){
			value = settings.getInt(key, defaultValue);
		}
		return value;
	}


    public long getData(String key,long defaultValue){
        long value = defaultValue;
        if(settings!=null){
            value  = settings.getLong(key,defaultValue);
        }
        return value;
    }
	
	
	public int getIntData(String key){
		int defValue =0;
		if(settings !=null){
			defValue = settings.getInt(key, defValue);
		}
		return defValue;
	}
	
}
