#!/bin/bash

if [ -e /data/workspace/myshixun/step56/error.txt ]; then
    cat /data/workspace/myshixun/step56/error.txt
else
    cat /data/workspace/myshixun/step56/result.txt
fi
