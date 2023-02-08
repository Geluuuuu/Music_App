from time import time
from xmlrpc.client import boolean
from sqlalchemy import Column, String, Boolean, Integer
from sqlalchemy.dialects.postgresql import UUID
from base.sql_base import Base
import uuid


class Token (Base):
    __tablename__ = 'token'

    id = Column(Integer, primary_key=True)
    token = Column (String)
    valid = Column (Boolean)
    timestamp = Column (String)

    def __init__ (self, token, valid, timestamp):
        self.token = token
        self.valid = valid
        self.timestamp = timestamp