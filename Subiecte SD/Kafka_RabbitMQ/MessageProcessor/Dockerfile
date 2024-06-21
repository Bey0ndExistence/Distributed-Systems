FROM python:3.11-alpine
RUN pip install kafka-python pika retry
WORKDIR /app
ADD MessageProcessor.py app.py
ADD mq_communication.py mq_communication.py
CMD ["python", "app.py"]