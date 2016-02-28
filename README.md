Proximity Display
=================

Using Raspberry Pi, analog infra-red distance sensor and character LCD display with Scala application to access calibrated digital input from sensor.
See more details on project page.


**Project home:**
http://explorer.devscala.com

**Github sources:**
https://github.com/lanceon/raspberry-pi-explorer

&copy; Alexey Zavalin

-----

#### Building from source and running Proximity Display v1.1

Pi console:

    mkdir proximity
    cd proximity


sbt:

    project lcd
    publishLocal
    project distance
    publishLocal
    project proximity-display
    df

Pi console:

    ./proximity.sh 

Output:

    Starting proximity.jar...
    
    Proximity Display parameters: delayBetweenBursts = 1000, delayBetweenReadings = 50, burstSize = 10, displayThrottle = 500, pins = Map(rs -> 1, en -> 4, d4 -> 5, d5 -> 6, d6 -> 7, d7 -> 2)
    Start with --help for details
    Press Enter to exit
          
    [INFO] [2016-02-28 04:09:46,495] [main] c.d.e.ProximityApp$: ADC is ready
    [INFO] [2016-02-28 04:09:47,339] [main] c.d.e.ProximityApp$: LCD is ready
    [INFO] [2016-02-28 04:09:48,499] [main] c.d.e.ProximityApp$: Sensor object is ready
    [INFO] [2016-02-28 04:09:48,518] [main] c.d.e.ProximityDisplay: ProximityDisplay: started
    [INFO] [2016-02-28 04:09:51,300] [RxComputationThreadPool-1] c.d.e.ProximityDisplay: Updating LCD: 11 cm  

