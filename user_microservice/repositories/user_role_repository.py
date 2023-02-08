from base.sql_base import Session
from models.role_orm import Role
from models.user_orm import User

def create_user_role(session, user : User, role : Role):
    user.roles.append (role)
    session.add(user)
    session.commit()