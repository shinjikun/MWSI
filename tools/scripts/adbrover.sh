#!/usr/bin/env bash


#start the application
 adb shell am start -n com.indra.rover.mwsi/.ui.activities.LockedAppActivity --activity-clear-task --activity-clear-top --activity-single-top



#end the application
adb shell am force-stop com.indra.rover.mwsi

#check version
adb shell dumpsys package com.indra.rover.mwsi |grep versionCode


#send broadcast
adb shell am broadcast -a com.indra.rover.mwsi.ROVER_MESSAGE  -p com.indra.rover.mwsi --es action upload --es status started --es file BNFOMULTBOOK.txt


#install apk
adb install rover.apk

#push files
adb push <file to push> /sdcard/com.indra.rover.mwsi/upload/



##pull files
adb shell am start -n com.indra.rover.mwsi/.ui.activities.LockedAppActivity --activity-clear-task --activity-clear-top --activity-single-top
adb shell am broadcast -a com.indra.rover.mwsi.ROVER_MESSAGE  -p com.indra.rover.mwsi --es action pulldb --es status started
adb pull  /sdcard/com.indra.rover.mwsi/dbdump ./dbdump
adb shell am broadcast -a com.indra.rover.mwsi.ROVER_MESSAGE  -p com.indra.rover.mwsi --es action pulldb --es status ended
