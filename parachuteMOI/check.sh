#!/bin/bash

if [ -e /data/workspace/myshixun/parachuteMOI/error.txt ]; then
    cat /data/workspace/myshixun/parachuteMOI/error.txt
else
    cat /data/workspace/myshixun/parachuteMOI/result.txt
fi
