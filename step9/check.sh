#!/bin/bash

if [ -e /data/workspace/myshixun/step9/error.txt ]; then
    cat /data/workspace/myshixun/step9/error.txt
else
    cat /data/workspace/myshixun/step9/result.txt
fi
