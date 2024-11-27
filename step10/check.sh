#!/bin/bash

if [ -e /data/workspace/myshixun/step10/error.txt ]; then
    cat /data/workspace/myshixun/step10/error.txt
else
    cat /data/workspace/myshixun/step10/result.txt
fi
