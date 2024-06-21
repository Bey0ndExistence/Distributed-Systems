FROM python:3.9.2-alpine
RUN pip install pika retry
WORKDIR /app
ADD Stats.py app.py
ADD mq_communication.py mq_communication.py
CMD ["python", "app.py"]