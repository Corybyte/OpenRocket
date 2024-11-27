#!/bin/bash

if [ -e /data/workspace/myshixun/step13/error.txt ]; then
    cat /data/workspace/myshixun/step13/error.txt
else
    cat /data/workspace/myshixun/step13/result.txt
fi
