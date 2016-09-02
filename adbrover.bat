@rem Rover-Android Util Script
@rem A script that will install  apk, upload  and download file to  a connected  android device via usb 
@echo off
setlocal ENABLEEXTENSIONS EnableDelayedExpansion

@rem ds content directory
set ds_content_dir=c:\directory_of_contents\

@rem ds content upload directory location
set ds_upload_dir=%ds_content_dir%uploads

@rem ds content download directory location
set ds_download_dir=%ds_content_dir%downloads

@rem rover database dump  file name
set rover_db_dump=DBDUMP

@rem rover android installer file name
set rover_android_apk=android.apk


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

SET cmd_arg=%1
SET param2=%2

if /I "%cmd_arg%"=="install" (
	call :check_devices
	if  "!is_device!"=="1" (
			call :install_app
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

goto :skip

:pull_files_block

	call :start_app
	if  "!is_installed!"=="1" (
			set actiontype=download
			call :send_broadmsg
			call :send_broadmsg
			set statustype=ended
			call :send_broadmsg
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
			)else (
			echo Rover App not installed
	)
exit /B 0

:update_db
	call :start_app
	if  "!is_installed!"=="1" (
			call :send_broadmsg
			call :end_app
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
		if exist %param2%\* ( adb push %param2% %rover_app_dir%/

		 ) else (
		 	@rem param2 is a file
		 	adb push %param2% %rover_app_dir%/uploads/
			)

	)
exit /B 0



:install_app
	adb install %rover_android_apk%
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
				)

			)
		)
exit /B 0


@rem send broadcast  message to a device
:send_broadmsg
	set cmd_r= 'adb shell am broadcast -a com.indra.rover.mwsi.ROVER_MESSAGE  -p %rover_package% --es action %actiontype% --es status %statustype% --es file %file%'
	for /f "delims=" %%i in (%cmd_r%) do (
		set output=%%i
	)
exit /B 0


:skip
endlocal