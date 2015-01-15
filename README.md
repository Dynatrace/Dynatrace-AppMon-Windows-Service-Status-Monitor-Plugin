## Windows Service Status Monitor Plugin

![images_community/download/attachments/73400859/icon.png](images_community/download/attachments/73400859/icon.png)

The plugin is querying the specified service on the given host(s) using the Windows SC command which communicates with the NT Service Controller and Services.  
If the service is started then the serviceStatus measure will record a "1". If the service is stopped it will return "0". In case of an access or other problem the plugin will fail to execute with an
error message indicating the problem.

Find further information in the [dynatrace community](https://community.compuwareapm.com/community/display/DL/Windows+Service+Status+Monitor+Plugin) 