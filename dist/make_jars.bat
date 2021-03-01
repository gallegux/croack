cd ..\bin

jar cvMf ..\dist\jars\croack-core.jar croack/*.class croack/*.cfg croack/ed croack/gui croack/noto croack/util

cd ..\src

jar cvMf ..\dist\jars\croack-rsrc.jar croack/recursos

cd ..\dist

java -cp japon.jar gallegux.japon.utils.MakeJaponFile croack-japon.txt D:\ffgr33\owncloud\jars\croack.japon jars

copy jars\croack-core.jar D:\ffgr33\owncloud\jars
copy jars\croack-rsrc.jar D:\ffgr33\owncloud\jars

