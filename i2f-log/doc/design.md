# log framework design
## supports
- multicast log output(register/unregister)
    - dynamic add/remove log output writer
- custom define output
    - to db,to es,to file,to remote...
- dynamic log config
    - log level control,open/close control
- async queue log output
    - use queue and thread pool async write pool
- log level output control
    - control every package/class/method log out level
- annotation auto log
    - simple use log fast record logs
- spring auto-configuration
    - add enable to auto-config
- lest rely on
    - lest rely on other component/framework
    - by extend api support
- more custom api
    - keep almost everywhere could be custom define/control

