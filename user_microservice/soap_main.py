from http.client import HTTPException
from nis import cat
from spyne import Application
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
from spyne import rpc, ServiceBase, Integer, Iterable, String
from base.sql_base import Session
from dto.get_users_dto import GetUsersDto
from dto.user_dto import UserDto
from models.role_orm import Role
from models.token_orm import Token
from models.user_orm import User
from repositories.role_repository import create_role, get_role_by_name, get_roles
from repositories.token_repository import add_token, get_token, invalidate_token
from repositories.user_repository import change_password, create_user, get_user, get_users, log_in
from repositories.user_role_repository import create_user_role
from service.token_service import create_token, verify_token
import jwt
from base.sql_base import session
from datetime import datetime
from sqlalchemy.exc import IntegrityError

class RolesDbActions (ServiceBase):
    @rpc (String, _returns = String)
    def add_role (ctx, role):
        try:
            create_role(session, role)
            return "OK"
        except IntegrityError:
            session.rollback()
            return "BAD REQUEST"
        except Exception:
            return "INTERNAL SERVER ERROR"
        
        
    @rpc (_returns = String)
    def get_roles (ctx):
        return "{\"roles\" : " + str(list (map(lambda x : x.value, get_roles(session)))).replace("\'", "\"") + "}"

class UserRoleDbActions (ServiceBase):
    @rpc (Integer, String, _returns = String)
    def add_role_user (ctx, id_user, role_name):
        try:
            users = list(get_user(session, id_user))
            role = get_role_by_name(session, role_name)
            if (len(users) == 0 or role == None):
                return "BAD REQUEST"
            create_user_role (session, users[0], role)
        except Exception:
            return "INTERNAL SERVER ERROR"
        return "OK"

    
    @rpc (String, String, _returns = String)
    def check_permission (ctx, token, min_role):
        try:
            #verify token on db
            token_db : Token = get_token (session, token)
            if (token_db.valid == False):
                return "FORBIDDEN"

            #get data from token claims
            data_token = verify_token (token)
            user_role_name = data_token['role']

            #get roles
            user_role = get_role_by_name(session, user_role_name)
            min_role = get_role_by_name(session, min_role)

            if (min_role == None):
                return "BAD REQUEST"
            if (user_role == None):
                return "FORBIDDEN"

            #send response
            if (user_role.id <= min_role.id):
                return str(data_token['sub'])
            return "FORBIDDEN"
        except HTTPException:
            return "FORBIDDEN"
        except Exception:
            return "INTERNAL SERVER ERROR"
    

class UsersDbActions(ServiceBase):
    @rpc (Integer, _returns = String)
    def get_user_by_id (ctx, user_id):
        user = get_user(session, user_id)
        return list (user)[0].username
        

    @rpc (_returns = String)
    def get_users (ctx):
        return str(vars(GetUsersDto(list (map(lambda x : vars(UserDto (x.id, x.username, x.password, list(map(lambda y : y.value, x.roles)))), get_users(session)))))).replace("\'", "\"")


    @rpc (String, String, _returns = String)
    def add_user (ctx, username, password):
        try:
            role : Role = get_role_by_name (session, "user")
            create_user (session, username, password, role)
            return "OK"
        except IntegrityError:
            session.rollback()
            return "BAD REQUEST"
        except Exception:
            return "INTERNAL SERVER ERROR"

    
    @rpc(String, String, _returns = String)
    def log_in (ctx, username, password):
        try:
            user : User = log_in (session, username, password)
            if (user == None):
                return "FORBIDDEN"

            min_role : Role = min(user.roles, key=lambda role: role.id)

            token = create_token ('http://127.0.0.1:8000', user.id, min_role.value)

            # Getting the current date and time
            dt = datetime.now()
            add_token (session, token, str(dt))

            return token
        except Exception:
            return "INTERNAL SERVER ERROR"
    

    @rpc (String, _returns = String)
    def log_out (ctx, token):
        try:
            invalidate_token(session, token)
            return "OK"
        except HTTPException:
            return "FORBIDDEN"
        except Exception:
            return "INTERNAL SERVER ERROR"


    @rpc (String, String, _returns = String)
    def change_password (ctx, token, password):
        try:
            #get data from token claims
            data_token = verify_token (token)
            user_id = data_token['sub']
            user : User = get_user (session, user_id)
            change_password (session, user, password)

        except HTTPException:
            return "FORBIDDEN"
        except Exception:
            return "INTERNAL SERVER ERROR"


application = Application([UsersDbActions, RolesDbActions, UserRoleDbActions], 'services.user.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())

wsgi_application = WsgiApplication(application)
if __name__ == '__main__':
    import logging

    from wsgiref.simple_server import make_server
    
    logging.basicConfig(level=logging.INFO)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.INFO)

    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://127.0.0.1:8000/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()
