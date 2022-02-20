
# Multi-Threaded Java Web Server

## Usage

If the [config.json](config.json) is in the same directory as the .jar file, then it will be automatically found. Otherwise provide a path parameter:

> java -jar multi-threaded-java-web-server-0.1.jar -f path/to/config.json

## Details

Implementation based on Java ServerSockets. 

  1. Server event loop within a dedicated thread receives new connections over the ServerSocket.
  2. The opened connection is sent off to a thread from the Thread Pool.
  3. HTTP Receiver Header is parsed.
  4. HTTP Response Header (+ Body) are built and sent.
  5. Connection is closed, the thread is done.
  6. Rinse & Repeat.

The server supports HTTP/1.1 GET messages. Everything else is declined with appropriate status codes. No range requests, no keep-alive. It serves text, images, audio and video.

The current bottleneck lies in the blocking operation of the ServerSocket and (Client)Socket. Using a Selector in combination with a ServerSocketChannel and implementing range requests as well as keep-alive could greatly increase the throughput of the server. However, this is non-trivial and takes more time and care to implement and test. The Logging is rudimentary and currently done programmatically but sufficiently functional for debugging purposes.

The [test website](http://65.21.145.57) used in the www/ folder is taken from [StartBootstrap](https://github.com/StartBootstrap/startbootstrap-landing-page/tree/master), with some inserted links in order to test that the server handles linking correctly.

There's a very small [loadTest.py](scripts/loadTest.py) script in the repository that makes use of [Locust](https://locust.io). To run it simply use

> locust -f scripts/loadTest.py

An example [Locust report](http://65.21.145.57/report) was made using the tool for the test website running on this server.

The [GitHub Actions CI/CD](.github/workflows/build-and-deploy.yml) does the following steps:

- Using the [DOCKERFILE](DOCKERFILE), the web server is built, tested and set up to run in a light-weight container. 
- The container is pushed to the [Docker Hub Registry](https://hub.docker.com/repository/docker/pragmaticfox/multi-threaded-java-web-server) (login required).
- Lastly, we SSH into the remote server to download the container from the registry and [run it](http://65.21.145.57).
