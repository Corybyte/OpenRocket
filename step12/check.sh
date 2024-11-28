#!/bin/bash

if [ -e /data/workspace/myshixun/step12/error.txt ]; then
    cat /data/workspace/myshixun/step12/error.txt
else
    cat /data/workspace/myshixun/step12/result.txt
fi
