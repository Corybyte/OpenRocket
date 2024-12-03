#!/bin/bash

if [ -e /data/workspace/myshixun/step56/error.txt ]; then
    cat /data/workspace/myshixun/step56/error.txt
else
    cat /data/workspace/myshixun/step56/result.txt
fi

mv /data/workspace/myshixun/step56/aoa.png /data/workspace/myshixun/step56/image
mv /data/workspace/myshixun/step56/mach.png /data/workspace/myshixun/step56/image