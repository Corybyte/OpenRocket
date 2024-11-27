#!/bin/bash

if [ -e /data/workspace/myshixun/step4/error.txt ]; then
    cat /data/workspace/myshixun/step4/error.txt
else
    cat /data/workspace/myshixun/step4/result.txt
fi
