#!/bin/bash

if [ -e /data/workspace/myshixun/shockCordCG/error.txt ]; then
    cat /data/workspace/myshixun/shockCordCG/error.txt
else
    cat /data/workspace/myshixun/shockCordCG/result.txt
fi
