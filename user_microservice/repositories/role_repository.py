from models.role_orm import Role
from base.sql_base import Session


def get_roles(session):
    roles = session.query(Role).all()
    return roles

def get_role_by_name(session, name):
    roles = session.query(Role).all()

    role = list(filter (lambda x : (x.value == name), roles))
    if (len(role) == 0):
        return None
    return role[0]

def create_role(session, name):
    role = Role (name)
    session.add(role)
    session.commit()
    return role