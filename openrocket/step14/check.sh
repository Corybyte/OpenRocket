#!/bin/bash

if [ -e /data/workspace/myshixun/step14/error.txt ]; then
    cat /data/workspace/myshixun/step14/error.txt
else
    cat /data/workspace/myshixun/step14/result.txt
fi
