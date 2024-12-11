#!/bin/bash

if [ -e /data/workspace/myshixun/wholeCP/error.txt ]; then
    cat /data/workspace/myshixun/wholeCP/error.txt
else
    cat /data/workspace/myshixun/wholeCP/result.txt
fi

mv /data/workspace/myshixun/wholeCP/aoa.png /data/workspace/myshixun/wholeCP/image
mv /data/workspace/myshixun/wholeCP/mach.png /data/workspace/myshixun/wholeCP/image