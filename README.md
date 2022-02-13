
# Multi-Threaded Java Web Server

## Usage

Specify a [config.json](config.json) in the following format:

```
{
	"root": "www",
	"host": "0.0.0.0",
	"port": 3000,
	"nbPoolThreads": 250
}
```

If the config.json is in the same directory as the .jar file, then it will be automatically found.

Otherwise provide a path parameter:

> java -jar multi-threaded-java-web-server-0.1.jar -f path/to/config.json


## Details

This web server is built on top of [Javalin](https://javalin.io/), which is built on top of [Jetty](https://www.eclipse.org/jetty/). We wrap our own server class around, set up a [QueuedThreadPool](https://www.eclipse.org/jetty/javadoc/jetty-9/org/eclipse/jetty/util/thread/QueuedThreadPool.html), provide the config.json parameters and are good to go.

Since we're only interested in serving static files from a pre-specified root folder for the time being, we disallow any HTTP methods for the exception of GET and HEAD. These are then tested to verify correct behaviour and that sensible HTTP Error Codes are returned to the client.

The [test website](http://65.21.145.57/) used in the www/ folder is taken from [StartBootstrap](https://github.com/StartBootstrap/startbootstrap-landing-page/tree/master), with some inserted links in order to test that the server handles linking correctly.

There's a very small [loadTest.py](scripts/loadTest.py) script in the repository that makes use of [Locust](https://locust.io/). To run it simply use

> locust -f scripts/loadTest.py 

To run multi-threaded use the following, in different processes:

> locust -f scripts/loadTest.py --master

> locust -f scripts/loadTest.py --worker

An example [Locust report](http://65.21.145.57/report/) was made using the tool for the test website running on this server.

The [GitHub Actions CI/CD](.github/workflows/build-and-deploy.yml) does the following steps:

- Using the [DOCKERFILE](DOCKERFILE), the web server is built, tested and set up to run in a light-weight container. 
- The container is pushed to the [Docker Hub Registry](https://hub.docker.com/repository/docker/pragmaticfox/multi-threaded-java-web-server) (login required).
- Lastly, we SSH into the remote server to download the container from the registry and [run it](http://65.21.145.57/).
