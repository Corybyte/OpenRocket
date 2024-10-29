#!/bin/bash

if [ -e /data/workspace/myshixun/step15/error.txt ]; then
    cat /data/workspace/myshixun/step15/error.txt
else
    cat /data/workspace/myshixun/step15/result.txt
fi
