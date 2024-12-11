#!/bin/bash

if [ -e /data/workspace/myshixun/transitionCP/error.txt ]; then
    cat /data/workspace/myshixun/transitionCP/error.txt
else
    cat /data/workspace/myshixun/transitionCP/result.txt
fi
