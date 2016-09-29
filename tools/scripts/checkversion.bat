@echo off
@rem rover android app package name
set rover_package=com.indra.rover.mwsi
setlocal ENABLEEXTENSIONS EnableDelayedExpansion
 set cmd_r='adb shell  "dumpsys package %rover_package% |grep versionCode"'
 for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
		for /f "tokens=1" %%d in ("!output!") do (
				set app_status=%%d
			)
		echo !app_status!

endlocal