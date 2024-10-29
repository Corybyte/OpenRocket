#!/bin/bash

if [ -e /data/workspace/myshixun/step37/error.txt ]; then
    cat /data/workspace/myshixun/step37/error.txt
else
    cat /data/workspace/myshixun/step37/result.txt
fi
