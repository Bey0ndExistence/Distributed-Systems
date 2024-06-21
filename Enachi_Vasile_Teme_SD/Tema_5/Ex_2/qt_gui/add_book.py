# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'add_book.ui'
#
# Created by: PyQt5 UI code generator 5.12.3
#
# WARNING! All changes made in this file will be lost!


from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QMessageBox


class Ui_AddBookWindow(object):

    def setupUi(self, AddBookWindow):
        AddBookWindow.setObjectName("AddBookWindow")
        AddBookWindow.resize(829, 340)
        self.centralwidget = QtWidgets.QWidget(AddBookWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.lineEditAutor = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEditAutor.setGeometry(QtCore.QRect(40, 20, 113, 25))
        self.lineEditAutor.setObjectName("lineEditAutor")
        self.lineEditTitlu = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEditTitlu.setGeometry(QtCore.QRect(40, 50, 113, 25))
        self.lineEditTitlu.setObjectName("lineEditTitlu")
        self.lineEditEditura = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEditEditura.setGeometry(QtCore.QRect(40, 80, 113, 25))
        self.lineEditEditura.setObjectName("lineEditEditura")
        self.textEditContent = QtWidgets.QTextEdit(self.centralwidget)
        self.textEditContent.setGeometry(QtCore.QRect(40, 110, 381, 161))
        self.textEditContent.setObjectName("textEditContent")
        self.pushButtonAddBook = QtWidgets.QPushButton(self.centralwidget)
        self.pushButtonAddBook.setGeometry(QtCore.QRect(590, 10, 211, 71))
        self.pushButtonAddBook.setObjectName("pushButtonAddBook")
        self.pushButtonAddBook.clicked.connect(self.add_book)
        self.pushButtonExit = QtWidgets.QPushButton(self.centralwidget)
        self.pushButtonExit.setGeometry(QtCore.QRect(590, 130, 211, 71))
        self.pushButtonExit.setObjectName("pushButtonExit")
        self.pushButtonExit.clicked.connect(QtCore.QCoreApplication.instance().quit)
        AddBookWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(AddBookWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 829, 22))
        self.menubar.setObjectName("menubar")
        AddBookWindow.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(AddBookWindow)
        self.statusbar.setObjectName("statusbar")
        AddBookWindow.setStatusBar(self.statusbar)

        self.retranslateUi(AddBookWindow)
        QtCore.QMetaObject.connectSlotsByName(AddBookWindow)

    def retranslateUi(self, AddBookWindow):
        _translate = QtCore.QCoreApplication.translate
        AddBookWindow.setWindowTitle(_translate("AddBookWindow", "Add Book"))
        self.lineEditAutor.setPlaceholderText(_translate("AddBookWindow", "Autor"))
        self.lineEditTitlu.setPlaceholderText(_translate("AddBookWindow", "Titlu"))
        self.lineEditEditura.setPlaceholderText(_translate("AddBookWindow", "Editura"))
        self.pushButtonAddBook.setText(_translate("AddBookWindow", "Adauga"))
        self.pushButtonExit.setText(_translate("AddBookWindow", "Exit"))

    def setParentWindow(self, parent):
        self.parentWindow = parent

    def add_book(self):
        autor = self.lineEditAutor.text()
        title = self.lineEditTitlu.text()
        editura = self.lineEditEditura.text()
        content = self.textEditContent.toPlainText()
        request = None

        if not autor or not title or not editura or not content:
            msg = QMessageBox()
            msg.setText("Invalid input")
            msg.setWindowTitle("Spatii goale")
            msg.exec_()
            return

        request = "add:{}={}={}={}".format(autor, title, editura, content)
        print(request)
        if self.parentWindow:
            self.parentWindow.send_request(request)


if __name__ == "__main__":
    import sys

    app = QtWidgets.QApplication(sys.argv)
    AddBookWindow = QtWidgets.QMainWindow()
    ui = Ui_AddBookWindow()
    ui.setupUi(AddBookWindow)
    AddBookWindow.show()
    sys.exit(app.exec_())
