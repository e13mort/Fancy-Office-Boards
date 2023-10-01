# syntax=docker/dockerfile:1
FROM amazoncorretto:11-alpine3.18

COPY ./build/install/backend/ /dashboard/
WORKDIR /dashboard/
ENTRYPOINT ["sh", "./bin/backend"]