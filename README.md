# multi-threaded-java-web-server


## Usage

Specify a config.json in the following format:

```
{
    "root": "absolute/path/to/root",
    "localIp": "localhost",
    "port": 8080,
    "queueSize": 250,
    "nbPoolThreads": 2
}
```

If the config.json is in the same directory as the .jar file, then it will be
automatically found. Otherwise provide a path parameter:

> java -jar multi-threaded-java-web-server-0.1.jar -f path/to/config.json


## Details

Using Javalin.

Test website used in the www/ folder is taken from
https://github.com/StartBootstrap/startbootstrap-landing-page/tree/master/dist.

Load test with https://locust.io/:

On one process:
locust -f loadTest.py --master

On several more processes:
locust -f loadTest.py --worker

Optionally add IP of master process if you're running distributed over several machines:
locust -f loadTest.py --worker --master-host=<insert ip address of master process>

For example; running with ~3000 requests per second distributed over 4 locust worker processes:

Type	Name	# Requests	# Fails	Median (ms)	90%ile (ms)	99%ile (ms)	Average (ms)	Min (ms)	Max (ms)	Average size (bytes)
GET     /       1148160     149851	200	        1600	    198000	    5693	        0	        833863      13469