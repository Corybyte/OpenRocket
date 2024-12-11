#!/bin/bash

if [ -e /data/workspace/myshixun/wholeCG/error.txt ]; then
    cat /data/workspace/myshixun/wholeCG/error.txt
else
    cat /data/workspace/myshixun/wholeCG/result.txt
fi
