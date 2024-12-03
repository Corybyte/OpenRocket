##!/bin/bash

#if [ -e /data/workspace/myshixun/step8/error.txt ]; then
#    cat /data/workspace/myshixun/step8/error.txt
#else
#    cat /data/workspace/myshixun/step8/result.txt
#fi

#!/bin/bash
if [ -e /data/workspace/myshixun/step8/error.txt ]; then
    cat /data/workspace/myshixun/step8/error.txt
else
    cat /data/workspace/myshixun/step8/result.txt
fi

python3 /data/workspace/myshixun/step8/pltImage.py
