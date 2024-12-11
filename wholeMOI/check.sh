#!/bin/bash

if [ -e /data/workspace/myshixun/wholeMOI/error.txt ]; then
    cat /data/workspace/myshixun/wholeMOI/error.txt
else
    cat /data/workspace/myshixun/wholeMOI/result.txt
fi
