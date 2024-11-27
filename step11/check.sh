#!/bin/bash

if [ -e /data/workspace/myshixun/step11/error.txt ]; then
    cat /data/workspace/myshixun/step11/error.txt
else
    cat /data/workspace/myshixun/step11/result.txt
fi
