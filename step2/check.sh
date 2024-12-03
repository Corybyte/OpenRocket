#!/bin/bash
if [ -e /data/workspace/myshixun/step2/error.txt ]; then
    cat /data/workspace/myshixun/step2/error.txt
else
    cat /data/workspace/myshixun/step2/result.txt
fi

python3 /data/workspace/myshixun/step2/pltImage.py


# 拼接完整路径
#IMAGE_PATH="./data/workspace/myshixun/step2/image/mach.png"

# 检查文件是否存在
#if [ -f "$IMAGE_PATH" ]; then
#    echo "Opening image: $IMAGE_NAME"
#    xdg-open "$IMAGE_PATH" &
#fi
