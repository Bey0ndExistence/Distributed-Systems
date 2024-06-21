import os
import sys
from PyQt5.QtWidgets import QWidget, QApplication, QFileDialog, QMessageBox
from PyQt5 import QtCore
from PyQt5.uic import loadUi
from mq_communication import RabbitMq


def debug_trace(ui=None):
    from pdb import set_trace
    QtCore.pyqtRemoveInputHook()
    set_trace()
    # QtCore.pyqtRestoreInputHook()


class LibraryApp(QWidget):
    ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

    def __init__(self):
        super(LibraryApp, self).__init__()
        ui_path = os.path.join(self.ROOT_DIR, 'exemplul_2.ui')
        loadUi(ui_path, self)
        self.search_btn.clicked.connect(self.search)
        self.add_btn.clicked.connect(self.add)
        self.save_as_file_btn.clicked.connect(self.save_as_file)
        self.rabbit_mq = RabbitMq(self)

    def set_response(self, response):
        self.result.setText(response)

    def send_request(self, request):
        self.rabbit_mq.send_message(message=request)
        self.rabbit_mq.receive_message()

    def add(self):
        request = None
        author = self.input_author.text()
        title = self.input_title.text()
        editor = self.input_publisher.text()
        text = self.input_text.text()
        if author and title and editor and text:
            request = 'add:' + 'author={}'.format(author) + '&title={}'.format(title) + '&editor={}'.format(
                editor) + '&text={}'.format(text)
        self.send_request(request)

    def search(self):
        search_string = self.search_bar.text()
        request = None
        if search_string:
            if self.json_rb.isChecked():
                request = 'sprint:json&'
            elif self.html_rb.isChecked():
                request = 'sprint:html&'
            else:
                request = 'sprint:raw&'
            if self.author_rb.isChecked():
                request = request + 'author={}'.format(search_string)
            elif self.title_rb.isChecked():
                request = request + 'title={}'.format(search_string)
            else:
                request = request + 'publisher={}'.format(search_string)

        elif not search_string:
            if self.json_rb.isChecked():
                request = 'print:json'
            elif self.html_rb.isChecked():
                request = 'print:html'
            else:
                request = 'print:raw'
        else:
            if self.author_rb.isChecked():
                request = 'find:author={}'.format(search_string)
            elif self.title_rb.isChecked():
                request = 'find:title={}'.format(search_string)
            else:
                request = 'find:publisher={}'.format(search_string)
        self.send_request(request)

    def save_as_file(self):
        options = QFileDialog.Options()
        options |= QFileDialog.DontUseNativeDialog
        selected_format = ''
        if self.json_rb.isChecked():
            selected_format = "JSON Files (*.json)"
        elif self.html_rb.isChecked():
            selected_format = "HTML Files (*.html)"
        else:
            selected_format = "Text Files (*.txt)"

        file_path, _ = QFileDialog.getSaveFileName(self,
                                                   'Salvare fisier',
                                                   filter=selected_format,
                                                   options=options)
        if file_path:
            file_path = str(file_path)
            selected_extension = ''
            if self.json_rb.isChecked():
                selected_extension = '.json'
            elif self.html_rb.isChecked():
                selected_extension = '.html'
            else:
                selected_extension = '.txt'

            # Append the selected extension if necessary
            if not file_path.endswith(('.json', '.html', '.txt')):
                file_path += selected_extension

            try:
                with open(file_path, 'w') as fp:
                    if file_path.endswith(".html"):
                        fp.write(self.result.toHtml())
                    else:
                        fp.write(self.result.toPlainText())
            except Exception as e:
                print(e)
                QMessageBox.warning(self, 'Exemplul 2',
                                    'Nu s-a putut salva fisierul')


if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = LibraryApp()
    window.show()
    sys.exit(app.exec_())
