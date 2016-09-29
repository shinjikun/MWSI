:: Name:     adbrover.bat
:: Purpose:  Rover-Android Utility script
::           A script that will install  apk, upload  and download file to  a connected
::           android device via usb
:: Author:   leonardo.ilagan@gmail.com
:: Revision: September 2016 - initial version


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
set file=.

@rem List of COMMANDS ARE LISTED BELOW
@rem  install - to install the apk use the 'install' command
@rem download - to pull data files from the connected device
@rem upload - to push files from pc to device
@rem updatedb - to update the db of the rover app
@rem checkversion check version number of the app installed inside the device
@rem checkname  check version name installed inside the device
@rem checkdevice  check for connected device on DS thru USB
@rem checkapp check app if already installed inside the device
@rem pull app's db
SET cmd_arg=%1
SET param2=%2


if /I "%cmd_arg%"=="pulldb" (
	call :check_devices
	if  "!is_device!"=="1" (
			call :pulldb
		) else (
		echo No connected device
		)
	goto :skip
		)



if /I "%cmd_arg%"=="checkdevice" (
	call :check_devices
	if  "!is_device!"=="1" (
			echo Connected
		) else (
		echo No connected device
		)
	goto :skip

)

if /I "%cmd_arg%"=="checkapp" (
	call :check_devices
	if  "!is_device!"=="1" (
			call :check_app
			if  "!is_installed!"=="1" (
            			echo Installed
            			)else (
            			echo Rover App not installed
            	)
		) else (
		echo No connected device
		)
	goto :skip

)


if /I "%cmd_arg%"=="install" (
	call :check_devices
	if  "!is_device!"=="1" (
			call :check_app
            if  "!is_installed!"=="1" (
                   call :end_app
                   call :install_app
            )else (
                call :install_app
            )
		) else (
		echo No connected device
		)
	goto :skip

)

if /I "%cmd_arg%"=="checkversion" (
	call :check_devices
	if  "!is_device!"=="1" (
	       call :check_app
	        if  "!is_installed!"=="1" (
	            call :checkversion
	        )else (
	            echo Rover App not Installed
	        )


		) else (
		echo No connected device
		)
	goto :skip

)

if /I "%cmd_arg%"=="checkname" (
call :check_devices
	if  "!is_device!"=="1" (
	       call :check_app
	        if  "!is_installed!"=="1" (
	            call :checkname
	        )else (
	            echo Rover App not Installed
	        )


		) else (
		echo No connected device
		)
	goto :skip

)

if /I "%cmd_arg%"=="download" (
	call :check_devices
	if  "!is_device!"=="1" (
			call :pull_files_block
		) else (
		echo No connected device
		)
	goto :skip
		)
if /I "%cmd_arg%"=="upload" (
	call :check_devices
	if  "!is_device!"=="1" (
			call :push_files_block
		) else (
		echo No connected device
		)
	goto :skip
		)


if /I "%cmd_arg%"=="updatedb" (
	call :check_devices
	if  "!is_device!"=="1" (
			call :send_broadmsg
			call :update_db
		) else (
		echo No connected device
		)
	goto :skip
		)

echo No command found
goto :skip

:checkversion

 set cmd_r='adb shell  "dumpsys package %rover_package% |grep versionCode"'
 for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
		for /f "tokens=1" %%d in ("!output!") do (
				set app_status=%%d
			)
		echo !app_status!

exit /B 0

:checkname

 set cmd_r='adb shell  "dumpsys package %rover_package% |grep versionName"'
 for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
		for /f "tokens=1" %%d in ("!output!") do (
				set app_status=%%d
			)
		echo !app_status!

exit /B 0

:pulldb

	call :start_app
	if  "!is_installed!"=="1" (
			set actiontype=pulldb
			set statustype=started
			call :send_broadmsg
		IF not exist %ds_dbdump% (mkdir %ds_dbdump%)
		For /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c-%%a-%%b)
        For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime=%%a%%b)
        @rem remove trailing spaces
        for /f "tokens=* delims= " %%a in ("!mytime!") do (set mytime=%%a)


            adb pull %rover_app_dir%/dbdump/	%ds_dbdump%\!mydate!_!mytime!
			set statustype=ended
			call :send_broadmsg
			echo Completed
			)else (
			echo Rover App not installed
	)

exit /B 0

:pull_files_block

	call :start_app
	if  "!is_installed!"=="1" (
			set actiontype=download
			set statustype=started
			call :send_broadmsg
			IF not exist %ds_download_dir% (mkdir %ds_download_dir%)
		    For /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c-%%a-%%b)
            For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime=%%a%%b)
            @rem remove trailing spaces
            For /f "tokens=* delims= " %%a in ("!mytime!") do (set mytime=%%a)

            adb pull %rover_app_dir%/downloads/	%ds_download_dir%\!mydate!_!mytime!
			set statustype=ended
			call :send_broadmsg
			echo Completed
			)else (
			echo Rover App not installed
	)

exit /B 0

:push_files_block
	call :start_app
	if  "!is_installed!"=="1" (
			set actiontype=upload
			set statustype=started
			call :send_broadmsg


			call :push_file

			set statustype=ended
			call :send_broadmsg
			echo Completed
			)else (
			echo Rover App not installed
	)
exit /B 0

:update_db
	call :start_app
	if  "!is_installed!"=="1" (
    			set actiontype=updatedb
    			set statustype=started
    			call :send_broadmsg


    			call :push_db

    			set statustype=ended
    			call :send_broadmsg
    			echo Completed
    			)else (
    			echo Rover App not installed
    	)
exit /B 0

:push_file
	@rem is param2 is empty then pull all files inside the upload folder
	if  "%param2%"=="" (
		adb push %ds_upload_dir% %rover_app_dir%
	)else (
			@rem param2 is a directory
		if exist %param2%\* (
		adb push %param2% %rover_app_dir%/

		 ) else (
		 	@rem param2 is a file
		 	adb push %param2% %rover_app_dir%/uploads/
			)

	)
exit /B 0

:push_db
	@rem is param2 is empty then pull all files inside the upload folder
	if  "%param2%"=="" (
		adb push %ds_content_dir%db %rover_app_dir%
	)else (
			@rem param2 is a directory
		if exist %param2%\* (
		adb push %param2% %rover_app_dir%/

		 ) else (
		 	@rem param2 is a file
		 	adb push %param2% %rover_app_dir%/db/
			)

	)
exit /B 0

:check_app
 set cmd_r='adb shell  "pm list package |grep %rover_package%"'

 for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)

        if  "%output%"=="package:com.indra.rover.mwsi" (
                    set is_installed=1
          	)else (
          	     set is_installed=0
          	)

exit /B 0

:install_app
    if  "%param2%"=="" (
   			adb install -r %rover_android_apk_location%%rover_android_apk%
   	)else (
   	    	adb install  -r %param2%
   	)

exit /B 0

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

@rem force the device to end/finish the application
:end_app

	set cmd_r='adb shell am force-stop %rover_package%'

	for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
exit /B 0

:check_devices

	for /f "tokens=*" %%f in ('adb devices') do (
		set devicestr=%%f
		for /f "tokens=1,2" %%d in ("!devicestr!") do (
			set deviceid=%%d
			set keyword=%%e
			set is_device=0
				if /I "!keyword!"=="device" (
					set is_device=1
					 call :dir_loc
				)

			)
		)
exit /B 0

:dir_loc
 set cmd_r='adb shell  "echo $EXTERNAL_STORAGE"'

 for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
    set rover_app_dir=%output%/%rover_package%

exit /B 0

@rem send broadcast  message to a device
:send_broadmsg
	set cmd_r= 'adb shell am broadcast -a com.indra.rover.mwsi.ROVER_MESSAGE   --es action %actiontype% --es status %statustype% --es file %file%'
	for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
exit /B 0



:skip
endlocal