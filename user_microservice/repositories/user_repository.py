from models.user_orm import User
from base.sql_base import Session


def get_users(session):
    users = session.query(User).all()
    return users

def get_user (session, user_id):
    user = session.query(User).all()
    return filter (lambda x : x.id == user_id, user)


def create_user(session, username, password, user_role):
    user = User(username, password)
    user.roles.append (user_role)
    session.add(user)
    session.commit()
    return user

def log_in (session, username, password):
    user = session.query(User).all()
    users = list(filter (lambda x : (x.username == username and x.password == password), user))
    if (len(users) == 0):
        return None
    return users[0]

def change_password (session, user : User, password):
    user.password = password
    try:
        session.add (user)
        session.commit()
    except Exception as exc:
        print(f"Failed to add user - {exc}")
    
    return user