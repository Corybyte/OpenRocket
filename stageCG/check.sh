#!/bin/bash

if [ -e /data/workspace/myshixun/stageCG/error.txt ]; then
    cat /data/workspace/myshixun/stageCG/error.txt
else
    cat /data/workspace/myshixun/stageCG/result.txt
fi
