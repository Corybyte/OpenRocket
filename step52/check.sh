#!/bin/bash

if [ -e /data/workspace/myshixun/step52/error.txt ]; then
    cat /data/workspace/myshixun/step52/error.txt
else
    cat /data/workspace/myshixun/step52/result.txt
fi
