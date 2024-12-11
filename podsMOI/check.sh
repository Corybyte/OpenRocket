#!/bin/bash

if [ -e /data/workspace/myshixun/podsMOI/error.txt ]; then
    cat /data/workspace/myshixun/podsMOI/error.txt
else
    cat /data/workspace/myshixun/podsMOI/result.txt
fi
