#!/bin/bash

if [ -e /data/workspace/myshixun/innerComponentCG/error.txt ]; then
    cat /data/workspace/myshixun/innerComponentCG/error.txt
else
    cat /data/workspace/myshixun/innerComponentCG/result.txt
fi
