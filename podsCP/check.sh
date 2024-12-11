#!/bin/bash

if [ -e /data/workspace/myshixun/podsCP/error.txt ]; then
    cat /data/workspace/myshixun/podsCP/error.txt
else
    cat /data/workspace/myshixun/podsCP/result.txt
fi
