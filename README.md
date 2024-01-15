# VelocityMail
[![pipeline status](https://gitlab.lukasslaby.cz/majn/VelocityMail/badges/main/pipeline.svg)](https://gitlab.lukasslaby.cz/majn/VelocityMail/-/commits/main)
![](https://dcbadge.vercel.app/api/shield/305386943137579008)


# About
VelocityMail is a simple mail plugin for Velocity (https://papermc.io/software/velocity).

- Send mail to players which are offline
- MySQL data storage
- Interactive chat
- Players see new mails when they log in
- Supports tab-completion for player names
# Commands 
- `/mail view (/mail list)` <br> Shows new mails.

- `/mail <recipient> <message>` <br> Send a mail with content `<message>` to `<recipient>`. Note that you can only send messages to players that have played on the server at least once after the plugin has been installed.

- `/mailadmin reload` <br> Reload the config and language file.<br> 
Permission: `mail.mailadmin`


# Compiling
Compilation requires JDK and Maven.   
To compile the plugin, run `mvn package` from the terminal.   
Once the plugin compiles, grab the jar from `/target/` folder.   

# Contributing
If this plugin has helped you in any way, and you would like to return something back to make the plugin even better, there is a lot of ways to contribute:

* Open a **pull request** containing a new feature or a bug fix, which you believe many users will benefit from.
Make detailed high-quality bug reports. The difference between a bug getting fixed in 1 week vs 1 hour is in quality of the report (typically providing correct steps to reproduce that actually work).




