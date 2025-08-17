import sqlite3

def execute_safe_query(query, params):
    conn = sqlite3.connect('my.db')
    cursor = conn.cursor()
    cursor.execute(query, params)  # Safe: parameterized query
    return cursor.fetchall()
