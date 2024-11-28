#!/bin/bash

if [ -e /data/workspace/myshixun/step31/error.txt ]; then
    cat /data/workspace/myshixun/step31/error.txt
else
    cat /data/workspace/myshixun/step31/result.txt
fi
