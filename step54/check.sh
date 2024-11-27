#!/bin/bash

if [ -e /data/workspace/myshixun/step54/error.txt ]; then
    cat /data/workspace/myshixun/step54/error.txt
else
    cat /data/workspace/myshixun/step54/result.txt
fi
