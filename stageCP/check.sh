#!/bin/bash

if [ -e /data/workspace/myshixun/stageCP/error.txt ]; then
    cat /data/workspace/myshixun/stageCP/error.txt
else
    cat /data/workspace/myshixun/stageCP/result.txt
fi
