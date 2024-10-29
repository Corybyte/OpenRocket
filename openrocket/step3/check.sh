#!/bin/bash

if [ -e /data/workspace/myshixun/step3/error.txt ]; then
    cat /data/workspace/myshixun/step3/error.txt
else
    cat /data/workspace/myshixun/step3/result.txt
fi
