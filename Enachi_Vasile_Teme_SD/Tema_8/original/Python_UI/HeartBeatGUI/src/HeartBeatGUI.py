from tkinter import *
from tkinter import ttk
import threading
import socket
from threading import Timer

HOST = "localhost"
DUMMY_PORT = 2000

global flag


def stop_waiting():
    global flag
    flag = False


def resolve_question(question_text):
    global flag
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    try:
        sock.connect((HOST, DUMMY_PORT))
        sock.send(bytes(question_text + "\n", "utf-8"))
        t = threading.Timer(5.0, stop_waiting)
        t.start()
        flag = True
        while flag:
            data = sock.recv(1024)
            response_text = str(data, "utf-8")
            response_widget.insert(END, response_text)
    except ConnectionError:
        response_text = "Eroare de conectare la serviciul dummy"
        response_widget.insert(END, response_text)
    except Exception:
        pass


def ask_question():
    question_text = question.get()
    threading.Thread(target=resolve_question, args=(question_text,)).start()

if __name__ == '__main__':
    root = Tk()
    root.title("HeartBeat")

    root.columnconfigure(0, weight=1)
    root.rowconfigure(0, weight=1)

    content = ttk.Frame(root)

    response_widget = Text(content, height=10, width=50)

    question_label = ttk.Label(content, text="Dummy Ask:")

    question = ttk.Combobox(content, width=50)
    question['values'] = ("Putem lua pauza?", "Tineti post?", "Tema asta v-a provocat depresie?")
    question.current(0)

    ask = ttk.Button(content, text="Intreaba", command=ask_question)  # la apasare, se apeleaza functia ask_question
    exitbtn = ttk.Button(content, text="Iesi", command=root.destroy)  # la apasare, se iese din aplicatie

    content.grid(column=0, row=0)
    response_widget.grid(column=0, row=0, columnspan=3, rowspan=4)
    question_label.grid(column=3, row=0, columnspan=2)
    question.grid(column=3, row=1, columnspan=2)
    ask.grid(column=3, row=3)
    exitbtn.grid(column=4, row=3)

    root.mainloop()
