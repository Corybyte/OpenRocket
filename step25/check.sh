#!/bin/bash

if [ -e /data/workspace/myshixun/step25/error.txt ]; then
    cat /data/workspace/myshixun/step25/error.txt
else
    cat /data/workspace/myshixun/step25/result.txt
fi
