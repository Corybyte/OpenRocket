#!/bin/bash

if [ -e /data/workspace/myshixun/railButtonCP/error.txt ]; then
    cat /data/workspace/myshixun/railButtonCP/error.txt
else
    cat /data/workspace/myshixun/railButtonCP/result.txt
fi
