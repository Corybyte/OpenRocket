#!/bin/bash

if [ -e /data/workspace/myshixun/step22/error.txt ]; then
    cat /data/workspace/myshixun/step22/error.txt
else
    cat /data/workspace/myshixun/step22/result.txt
fi
