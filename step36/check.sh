#!/bin/bash

if [ -e /data/workspace/myshixun/step36/error.txt ]; then
    cat /data/workspace/myshixun/step36/error.txt
else
    cat /data/workspace/myshixun/step36/result.txt
fi
