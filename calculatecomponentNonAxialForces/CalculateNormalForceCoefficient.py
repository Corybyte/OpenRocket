def extract_cn_from_json(data):
    """
    提取 JSON 中的 cn 字段
    :param data: 传入的 JSON 数据
    :return: 如果存在 cn，返回 cn 的值；否则返回错误信息
    """
    if 'cn' in data:
        return data['cn']
    else:
        return None
