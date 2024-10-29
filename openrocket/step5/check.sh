#!/bin/bash

if [ -e /data/workspace/myshixun/step5/error.txt ]; then
    cat /data/workspace/myshixun/step5/error.txt
else
    cat /data/workspace/myshixun/step5/result.txt
fi
