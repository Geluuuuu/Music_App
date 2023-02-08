# python -m pip install mariadb sqlalchemy

from repositories.role_repository import get_roles
from repositories.user_repository import get_users
from base.sql_base import session

if __name__ == "__main__":
    print("\nUsers:")
    for user in get_users(session):
        print(f"{user.id} - {user.username} - {user.password} - roles: ", end="")
        for role in user.roles:
            print(f"{role.value} ", end="")
        print()

    print("\n\nRoles:")
    for role in get_roles(session):
        print(f"{role.value}")
