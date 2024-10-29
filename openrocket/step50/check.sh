#!/bin/bash

if [ -e /data/workspace/myshixun/step50/error.txt ]; then
    cat /data/workspace/myshixun/step50/error.txt
else
    cat /data/workspace/myshixun/step50/result.txt
fi
