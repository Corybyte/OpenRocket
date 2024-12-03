#!/bin/bash

if [ -e /data/workspace/myshixun/calculatePoint/error.txt ]; then
    cat /data/workspace/myshixun/calculatePoint/error.txt
else
    cat /data/workspace/myshixun/calculatePoint/result.txt
fi
