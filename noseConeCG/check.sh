#!/bin/bash

if [ -e /data/workspace/myshixun/noseConeCG/error.txt ]; then
    cat /data/workspace/myshixun/noseConeCG/error.txt
else
    cat /data/workspace/myshixun/noseConeCG/result.txt
fi
