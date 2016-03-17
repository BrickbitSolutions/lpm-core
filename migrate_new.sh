#!/usr/bin/env bash

file_name=$1

if [ ${#file_name} -gt 0 ]; then
    timestamp=$(date "+%Y%m%d%H%M%S")
    touch "src/main/resources/db/migration/V"$timestamp"__"$file_name".sql"
else
    echo Specify a filename
fi