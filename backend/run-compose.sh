#!/bin/bash

#
#  Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
#  GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
#

export DASHBOARDS_ADMIN_USERNAME=admin
export DASHBOARDS_ADMIN_PASSWORD=password
export DASHBOARDS_IMAGE_NAME=fancy-dashboards
export DASHBOARDS_PORT=8080
export DASHBOARDS_HOST_WORK_DIR=./dashboards-data

# build:
# docker build -t fancy-dashboards .
docker-compose -f compose.yaml up