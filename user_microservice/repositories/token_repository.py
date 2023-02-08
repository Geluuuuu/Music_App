from http.client import HTTPException
from pickle import TRUE
from xmlrpc.client import boolean
from base.sql_base import Session
from models.token_orm import Token
from rest_framework import status


def add_token(session, token, timestamp):
    token = Token (token, True, timestamp)
    try:
        session.add(token)
        session.commit()
    except Exception as exc:
        print(f"Failed to add user - {exc}")
    return token


def get_token (session, token_id):
    tokens = session.query(Token).all()
    token =  list(filter (lambda x : x.token == token_id, tokens))

    if (len(token) == 0):
        raise HTTPException()

    return token[0]

def invalidate_token (session, token):
    tokens = session.query(Token).all()
    token =  list(filter (lambda x : x.token == token, tokens))

    if (len(token) == 0):
        raise HTTPException(
        status_code=status.HTTP_400_BAD_REQUEST,
        detail="Token not exists",
    )

    new_token : Token = token[0]
    new_token.valid = False

    try:
        session.add (new_token)
        session.commit()
    except Exception as exc:
        print(f"Failed to add user - {exc}")
    
    return new_token
