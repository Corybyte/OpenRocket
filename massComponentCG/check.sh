#!/bin/bash

if [ -e /data/workspace/myshixun/massComponentCG/error.txt ]; then
    cat /data/workspace/myshixun/massComponentCG/error.txt
else
    cat /data/workspace/myshixun/massComponentCG/result.txt
fi
