@echo off
setlocal ENABLEEXTENSIONS EnableDelayedExpansion

@rem ds content directory
set ds_content_dir=c:\directory_of_contents\

@rem ds content upload directory location
set ds_upload_dir=%ds_content_dir%uploads

@rem ds content download directory location
set ds_download_dir=%ds_content_dir%downloads

@rem ds content db dump directory location
set ds_dbdump=%ds_content_dir%dbdump
@rem rover database dump  file name
set rover_db_dump=DBDUMP

@rem rover android installer file directory location
set rover_android_apk_location=%ds_content_dir%
@rem rover android installer file name
set rover_android_apk=app.apk


@rem rover android app package name
set rover_package=com.indra.rover.mwsi

@rem rover android app start activity name
set rover_activity=.ui.activities.LockedAppActivity


@rem variable that checks if there is a connected device
set is_device=0

@rem variable that checks if the rover application is installed on the device
set is_installed=1

@rem rover directory
set rover_app_dir=/sdcard/%rover_package%


@rem rover download directory. where DS will pull files inside the android directory
set rover_app_d_dir=%rover_app_dir%/downloads/

set actiontype=download
set statustype=started

        call :dir_loc
        call :start_app
		set actiontype=pulldb
		set statustype=started
		call :send_broadmsg
		IF not exist %ds_dbdump% (mkdir %ds_dbdump%)
		For /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c-%%a-%%b)
        For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime=%%a%%b)
        @rem remove trailing spaces
        For /f "tokens=* delims= " %%a in ("!mytime!") do (set mytime=%%a)


            adb pull %rover_app_dir%/dbdump/	%ds_dbdump%\!mydate!_!mytime!
			set statustype=ended
			call :send_broadmsg
			echo Completed


goto :skip

@rem force the device to start the application
:start_app
	set cmd_r='adb shell am start -n %rover_package%/%rover_activity% --activity-clear-task --activity-clear-top --activity-single-top'
	for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
		for /f "tokens=1" %%d in ("!output!") do (
				set app_status=%%d
				if /I "!app_status!"=="Error:" (
						set is_installed=0
					)
			)
	)
exit /B 0
@rem send broadcast  message to a device
:send_broadmsg
	set cmd_r= 'adb shell am broadcast -a com.indra.rover.mwsi.ROVER_MESSAGE   --es action %actiontype% --es status %statustype% --es file %file%'
	for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
exit /B 0

:dir_loc
 set cmd_r='adb shell  "echo $EXTERNAL_STORAGE"'

 for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
    set rover_app_dir=%output%/%rover_package%

exit /B 0
:skip
endlocal
