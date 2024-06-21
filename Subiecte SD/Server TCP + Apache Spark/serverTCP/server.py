import socket
import json
import requests
import time

API_KEY = "cpnvvg1r01qru1ca8nvgcpnvvg1r01qru1ca8o00"
SERVER_PORT = 12345
SERVER_IP = "localhost"

stock_symbol_json = requests.get(f'https://finnhub.io/api/v1/stock/symbol?exchange=US&token={API_KEY}').json()
stock_symbols = []

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((SERVER_IP, SERVER_PORT))
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.listen()
conn, addr = s.accept()
if conn is not None:
    print(f"{addr} s-a conectat")
    for stock in stock_symbol_json:
        stock_symbols.append(stock["symbol"])

    for stock_symbol in stock_symbols:
        news_json = requests.get(f"https://finnhub.io/api/v1/company-news?symbol={stock_symbol}&from=2024-06-16&to=2024-06-17&token={API_KEY}").json()
        for news in news_json:
            print("am trimis data catre server")
            print(news)
            conn.sendall(f"{json.dumps(news)}\n".encode("ASCII"))
            time.sleep(3)
        if len(news_json) == 0:
            time.sleep(1)
