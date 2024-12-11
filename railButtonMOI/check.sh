#!/bin/bash

if [ -e /data/workspace/myshixun/railButtonMOI/error.txt ]; then
    cat /data/workspace/myshixun/railButtonMOI/error.txt
else
    cat /data/workspace/myshixun/railButtonMOI/result.txt
fi
