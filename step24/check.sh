#!/bin/bash

if [ -e /data/workspace/myshixun/step24/error.txt ]; then
    cat /data/workspace/myshixun/step24/error.txt
else
    cat /data/workspace/myshixun/step24/result.txt
fi
