#!/bin/bash

if [ -e /data/workspace/myshixun/step20/error.txt ]; then
    cat /data/workspace/myshixun/step20/error.txt
else
    cat /data/workspace/myshixun/step20/result.txt
fi
