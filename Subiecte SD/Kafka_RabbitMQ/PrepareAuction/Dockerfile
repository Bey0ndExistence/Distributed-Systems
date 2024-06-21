FROM python:3.9.2-alpine
RUN pip install kafka-python
WORKDIR /app
ADD PrepareAuction.py app.py
CMD ["python", "app.py"]