#!/bin/bash

if [ -e /data/workspace/myshixun/bodyTubeCG/error.txt ]; then
    cat /data/workspace/myshixun/bodyTubeCG/error.txt
else
    cat /data/workspace/myshixun/bodyTubeCG/result.txt
fi
