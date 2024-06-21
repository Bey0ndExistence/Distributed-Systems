FROM python:3.11-alpine
RUN pip install kafka-python
WORKDIR /app
ADD BiddingProcessor.py app.py
CMD ["python", "app.py"]