from flask import jsonify
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from data.database_setup import Expense, Base

engine = create_engine('sqlite:///family.db?check_same_thread=False')
Base.metadata.bind = engine
DBSession = sessionmaker(bind=engine)
session = DBSession()


class ExpenseService:
    def get_expenditure(self):
        pass

    def get_expense(self, id):
        pass

    def add_new_member(self, name, expense=0):
        pass

    def update_member(self, id, name, expense=0):
        pass

    def delete_member(self, id):
        pass


class ExpenseServiceImpl(ExpenseService):
    def get_expenditure(self):
        expenditure = session.query(Expense).all()
        return jsonify(expenditure=[expense.serialize for expense in expenditure])

    def get_expense(self, id):
        expense = session.query(Expense).filter_by(id=id).one()
        return jsonify(expense=expense.serialize)

    def add_new_member(self, name, expense=0):
        added_member = Expense(name=name, expense=expense)
        session.add(added_member)
        session.commit()
        return jsonify(expense=added_member.serialize)

    def update_member(self, id, name, expense):
        #exemplu de interogare raw
        session.execute("UPDATE expenditure set name='{}', expense={} where id={}".format(name, expense, id))
        session.commit()
        return "Updated member expense with id {}".format(id)

    def delete_member(self, id):
        member_to_delete = session.query(Expense).filter_by(id=id).one()
        session.delete(member_to_delete)
        session.commit()
        return "Removed member expense with id {}".format(id)

