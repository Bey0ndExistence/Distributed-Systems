from flask import request, Flask, render_template, redirect, url_for

from data.database_setup import Expense
from services.services import ExpenseServiceImpl


app = Flask(__name__)
expenditureService = ExpenseServiceImpl()

#----------------RESTFUL--------------------
@app.route('/expenditure', methods=['GET', 'POST'])
def expenditure_function():
    if request.method == 'GET':
        return expenditureService.get_expenditure()
    elif request.method == 'POST':
        name = request.args.get('name', '')
        expense = request.args.get('expense', 0)
        return expenditureService.add_new_member(name, expense)


@app.route('/expenditure/<int:id>', methods=['GET', 'PUT', 'DELETE'])
def expense_function_id(id):
    if request.method == 'GET':
        return expenditureService.get_expense(id)
    elif request.method == 'PUT':
        name = request.args.get('name', '')
        expense = request.args.get('expense', 0)
        return expenditureService.update_member(id, name, expense)
    elif request.method == 'DELETE':
        return expenditureService.delete_member(id)


#-------------------interface-----------------
@app.route('/')
def show_expenditure():
    js = expenditureService.get_expenditure().get_json()
    members = [Expense(str(element).replace("\'", "\"")) for element in js['expenditure']]
    return render_template('home.html', members=members)


@app.route('/expenditure/<int:member_id>/delete/', methods=['GET', 'POST'])
def delete_expense(member_id):
    if request.method == 'POST':
        expenditureService.delete_member(member_id)
        return redirect(url_for('show_expenditure'))
    else:
        js = expenditureService.get_expense(member_id).get_json()
        member = str(js['expense']).replace("\'", "\"")
        return render_template('delete_member.html', member=Expense(member))


@app.route('/expenditure/add/', methods=['GET', 'POST'])
def add_expense():
    if request.method == 'POST':
        member = Expense(name=request.form['name'], expense=request.form['expense'])
        expenditureService.add_new_member(member.name, member.expense)
        return redirect(url_for('show_expenditure'))
    else:
        return render_template('add_member.html')


@app.route('/expenditure/<int:member_id>/edit/', methods=['GET', 'POST'])
def update_expense(member_id):
    js = expenditureService.get_expense(member_id).get_json()
    member = Expense(str(js['expense']).replace("\'", "\""))
    if request.method == 'POST':
        name = request.form['name'] if request.form['name'] else member.name
        expense = request.form['expense'] if request.form['expense'] else member.expense
        expenditureService.update_member(member_id, name, expense)
        return redirect(url_for('show_expenditure'))
    else:
        return render_template('edit_member.html', member=member)


if __name__ == '__main__':
    app.debug = True
    app.run(host='127.0.0.1', port=8080)