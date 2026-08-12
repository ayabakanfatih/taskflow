#!/usr/bin/env bash
kill -9 $(lsof -t -i :8080) 2>/dev/null
sleep 1
docker start taskflow-postgres >/dev/null 2>&1
source ./.env.local
./mvnw spring-boot:run
