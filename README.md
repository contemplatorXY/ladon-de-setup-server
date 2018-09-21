# Ladon Data Center Edition Setup Server
The Setup Server helps to configure Ladon Data Center Edition in more complex setups.

Edit the ladon.properties file to fit your cluster setup and start the config server.

Important: use the external IPs of your nodes as they have to talk to each other.
For a simple single Node Setup you don't need this, just add -Dlocalsetup at startup of your ladon DE instance.


If you don't want to build it from sources have a look at the dist folder.

cd dist

java -jar ladon-config-server.jar


### License
Copyright (C) 2018 Mind Consulting

Free for private use, easy commercial licensing available

<a href="http://mind-consulting.de/"><img src="http://mind-consulting.de/img/logo_no_bg.png"  height="100" width="250" ></a>

