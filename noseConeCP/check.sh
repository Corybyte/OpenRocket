#!/bin/bash
if [ -e /data/workspace/myshixun/noseConeCP/error.txt ]; then
    cat /data/workspace/myshixun/noseConeCP/error.txt
else
    cat /data/workspace/myshixun/noseConeCP/result.txt
fi

python3 /data/workspace/myshixun/noseConeCP/pltImage.py


# 拼接完整路径
#IMAGE_PATH="./data/workspace/myshixun/noseConeCP/image/mach.png"

# 检查文件是否存在
#if [ -f "$IMAGE_PATH" ]; then
#    echo "Opening image: $IMAGE_NAME"
#    xdg-open "$IMAGE_PATH" &
#fi
