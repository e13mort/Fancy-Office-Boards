# syntax=docker/dockerfile:1
# additional dockerfile to support building via gradle
FROM amazoncorretto:11-alpine3.18

COPY / /dashboard/
WORKDIR /dashboard/
ENTRYPOINT ["sh", "./bin/backend"]