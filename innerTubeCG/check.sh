#!/bin/bash

if [ -e /data/workspace/myshixun/innerTubeCG/error.txt ]; then
    cat /data/workspace/myshixun/innerTubeCG/error.txt
else
    cat /data/workspace/myshixun/innerTubeCG/result.txt
fi
