#!/bin/bash

if [ -e /data/workspace/myshixun/step30/error.txt ]; then
    cat /data/workspace/myshixun/step30/error.txt
else
    cat /data/workspace/myshixun/step30/result.txt
fi
