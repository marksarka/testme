def sanitize_input(input_str):
    # sanitize the input before using
    return input_str.replace("__", "").replace("import", "")
