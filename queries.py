def get_user_by_email(email):
    query = "SELECT * FROM users WHERE email = ?"
    return execute_safe_query(query, [email])
