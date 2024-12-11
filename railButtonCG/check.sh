#!/bin/bash

if [ -e /data/workspace/myshixun/railButtonCG/error.txt ]; then
    cat /data/workspace/myshixun/railButtonCG/error.txt
else
    cat /data/workspace/myshixun/railButtonCG/result.txt
fi
