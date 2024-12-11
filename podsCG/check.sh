#!/bin/bash

if [ -e /data/workspace/myshixun/podsCG/error.txt ]; then
    cat /data/workspace/myshixun/podsCG/error.txt
else
    cat /data/workspace/myshixun/podsCG/result.txt
fi
