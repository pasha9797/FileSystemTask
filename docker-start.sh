#!/bin/sh
docker run --rm -it -v $(pwd):/project -w /project maven mvn clean package && docker-compose up