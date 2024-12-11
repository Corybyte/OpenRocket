##!/bin/bash

#if [ -e /data/workspace/myshixun/finSetCP/error.txt ]; then
#    cat /data/workspace/myshixun/finSetCP/error.txt
#else
#    cat /data/workspace/myshixun/finSetCP/result.txt
#fi

#!/bin/bash
if [ -e /data/workspace/myshixun/finSetCP/error.txt ]; then
    cat /data/workspace/myshixun/finSetCP/error.txt
else
    cat /data/workspace/myshixun/finSetCP/result.txt
fi

python3 /data/workspace/myshixun/finSetCP/pltImage.py
