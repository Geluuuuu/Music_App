from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

Base = declarative_base()
engine = create_engine('postgresql://postgres:pass@localhost:10000/user_db')
Session = sessionmaker(bind=engine)
session = Session()