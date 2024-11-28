#!/bin/bash

if [ -e /data/workspace/myshixun/step2/error.txt ]; then
    cat /data/workspace/myshixun/step2/error.txt
else
    cat /data/workspace/myshixun/step2/result.txt
fi
