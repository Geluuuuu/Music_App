# replace it with your 32 bit secret key
import datetime
from http.client import HTTPException
from rest_framework import status
import uuid
from jose import JWTError
import jwt


SECRET_KEY = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4"
 
# encryption algorithm
ALGORITHM = "HS256"


def create_token(url_server, user_id, role_id):
    expire = datetime.datetime.utcnow() + datetime.timedelta(minutes=60)

    data = {
        'iss': url_server,
        'sub': user_id,
        'jti': str(uuid.uuid1()),
        'role': role_id,
        'exp': expire
    }

    encoded_jwt = jwt.encode(data, SECRET_KEY, algorithm=ALGORITHM)

    return encoded_jwt

def verify_token(token):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        return payload
    except JWTError:
        raise HTTPException()
    except jwt.ExpiredSignatureError:
        raise HTTPException()