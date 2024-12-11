#!/bin/bash

if [ -e /data/workspace/myshixun/parachuteCG/error.txt ]; then
    cat /data/workspace/myshixun/parachuteCG/error.txt
else
    cat /data/workspace/myshixun/parachuteCG/result.txt
fi
