FROM python:3.8-slim

WORKDIR /server
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . /server

RUN chmod +x startup.sh

EXPOSE 10046
CMD ["./startup.sh"]