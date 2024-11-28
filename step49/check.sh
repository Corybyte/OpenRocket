#!/bin/bash

if [ -e /data/workspace/myshixun/step49/error.txt ]; then
    cat /data/workspace/myshixun/step49/error.txt
else
    cat /data/workspace/myshixun/step49/result.txt
fi
