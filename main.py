from utils import sanitize_input

def handle_request(user_input):
    safe_input = sanitize_input(user_input)
    eval(safe_input)
