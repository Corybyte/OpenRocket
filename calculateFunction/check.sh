#!/bin/bash

if [ -e /data/workspace/myshixun/calculateFunction/error.txt ]; then
    cat /data/workspace/myshixun/calculateFunction/error.txt
else
    cat /data/workspace/myshixun/calculateFunction/result.txt
fi
