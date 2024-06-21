from flask import Flask, render_template, request, redirect, jsonify
import requests

app = Flask(__name__)


@app.route('/')
def login_page():
    return render_template('index.html')


@app.route('/register')
def register_page():
    return render_template('register.html')


@app.route('/send', methods=['POST'])
def register():
    data = {
        'username': request.form['username'],
        'password': request.form['password'],
        'firstname': request.form['firstname'],
        'lastname': request.form['lastname']
    }

    req = requests.post('http://localhost:8080/user', json=data)

    return redirect('/')


@app.route('/auth', methods=['POST'])
def login():
    data = {
        'username': request.form['username'],
        'password': request.form['password']
    }

    req = requests.post('http://localhost:8080/login', json=data)

    auth = req.json()
    userid = auth.get("id")
    if userid:
        return redirect(f'/expenses/{userid}')

    return redirect('/')


@app.route('/expenses/<int:userid>', methods=['GET'])
def expenses_page(userid):
    user = requests.get(f'http://localhost:8080/user/{userid}')
    expenses = requests.get(f'http://localhost:8080/expenses/{userid}')
    return render_template("expenses.html", user=user.json(), expenses=expenses.json(), userid=userid)


@app.route('/addpage/<int:userid>', methods=['GET'])
def add_page(userid):
    return render_template("add.html", userid=userid)


@app.route('/add/<int:userid>', methods=['POST'])
def add_expense(userid):
    rez = {
        'expensesName': request.form['expense'],
        'value': request.form['value'],
    }

    req = requests.post(f'http://localhost:8080/expenses/{userid}', json=rez)

    return redirect(f'/expenses/{userid}')


@app.route('/editpage/<int:userid>/<string:name>/<int:value>', methods=['GET'])
def edit_page(userid, name, value):
    return render_template("edit.html", userid=userid, name=name, value=value, oldname=name)


@app.route('/edit/<int:userid>/<string:name>', methods=['POST'])
def edit(userid, name):
    rez = {
        'expensesName': request.form['expense'],
        'value': request.form['value'],
    }
    req = requests.patch(f'http://localhost:8080/expenses/{userid}/{name}', json=rez)
    return redirect(f'/expenses/{userid}')


@app.route('/delete/<int:userid>/<string:name>')
def delete(userid, name):
    req = requests.delete(f'http://localhost:8080/expenses/{userid}/{name}')
    return redirect(f'/expenses/{userid}')


if __name__ == '__main__':
    app.run()
