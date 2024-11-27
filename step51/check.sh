#!/bin/bash

if [ -e /data/workspace/myshixun/step51/error.txt ]; then
    cat /data/workspace/myshixun/step51/error.txt
else
    cat /data/workspace/myshixun/step51/result.txt
fi
