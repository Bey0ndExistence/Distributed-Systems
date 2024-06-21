import json
from sqlalchemy import Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base

from sqlalchemy import create_engine

Base = declarative_base()


class Expense(Base):
    __tablename__ = 'expenditure'

    id = Column(Integer, primary_key=True)
    name = Column(String(250), nullable=False)
    expense = Column(Integer, default=0, nullable=False)

    @property
    def serialize(self):
        return {
            'id': self.id,
            'name': self.name,
            'expense': self.expense
        }

    def __init__(self, j=None, name=None, expense=0):
        if name:
            self.name = name
            self.expense = expense
        elif j:
            self.__dict__ = json.loads(j)



engine = create_engine('sqlite:///../family.db')
Base.metadata.create_all(engine)    #create expenditure table