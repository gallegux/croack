cd ..\bin

jar cvMf ..\dist\jars\croack-rsrc.jar croack/recursos

cd ..\dist

java -cp japon.jar gallegux.japon.utils.MakeJaponFile croack-japon.txt D:\ffgr33\owncloud\jars\croack.japon jars

copy jars\croack-rsrc.jar D:\ffgr33\owncloud\jars
