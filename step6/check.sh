#!/bin/bash

if [ -e /data/workspace/myshixun/step6/error.txt ]; then
    cat /data/workspace/myshixun/step6/error.txt
else
    cat /data/workspace/myshixun/step6/result.txt
fi
