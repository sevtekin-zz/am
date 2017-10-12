#!/bin/sh
gcsfuse --key-file=/app/amgcs.json am_snapshots /app/snapshots &&
nodejs ambackend.js
exit 0
