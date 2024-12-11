#!/bin/bash

if [ -e /data/workspace/myshixun/motorFunction/error.txt ]; then
    cat /data/workspace/myshixun/motorFunction/error.txt
else
    cat /data/workspace/myshixun/motorFunction/result.txt
fi
