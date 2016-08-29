@rem Rover-Android Util Script
@rem A script that will install  apk, upload  and download file to  a connected  android device via usb 

setlocal ENABLEEXTENSIONS
set rover_content_dir=c:\directory_of_contents\

@rem rover content upload directory location
set rover_content_u_dir=%rover_content_dir%upload\

@rem rover content download directory location
set rover_content_d_dir=%rover_content_dir%download

@rem rover database dump  file name
set rover_db_dump=DBDUMP

@rem rover android installer file name
set rover_android_apk=android.apk


@rem List of COMMANDS ARE LISTED BELOW

@rem  install - to install the apk use the 'install' command 

@rem download - to pull data files from the connected device

@rem upload - to push files from pc to device

@rem updatedb - to update the db of the rover app

SET cmd_arg =  $0


if /I "%cmd_arg%"=="install" (
	call :install
) else (
		if "%cmd_arg%"=="download" (
			call :pull_files
		)		
) else (
		if "%cmd_arg%"=="upload" (
			call :push_files
		)
) else (
		if "%cmd_arg%"=="updatedb" (
			call :updatedb
		)
) else (
	echo "Command not found"
)



:pull_files
	call :start_app
	call :end_app
exit /B 0

:push_files
	call :start_app
	call :end_app
exit /B 0

:update_db
	call :start_app
	call :end_app
exit /B 0


:install_app
	call :start_app
	call :end_app
exit /B 0

@rem force the device to start the application
:start_app
	adb shell am start -n com.indra.rover.mwsi/.ui.activities.SplashActivity
exit /B 0

@rem force the device to end/finish the application
:end_app
	adb shell am force-stop com.indra.rover.mwsi
exit /B 0

endlocal