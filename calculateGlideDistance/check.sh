#!/bin/bash

ERROR_FILE="/data/workspace/myshixun/calculateGlideDistance/error.txt"
RESULT_FILE="/data/workspace/myshixun/calculateGlideDistance/result.txt"

if [ -e "$ERROR_FILE" ]; then
    cat "$ERROR_FILE"
    rm "$ERROR_FILE"
elif [ -e "$RESULT_FILE" ]; then
    cat "$RESULT_FILE"
    rm "$RESULT_FILE"
else
    echo "计算中，请稍后再试"
fi
