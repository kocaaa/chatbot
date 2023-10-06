FROM python:3.8-slim
WORKDIR /server
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . /server
RUN echo "#!/bin/bash" >> run.sh
RUN echo "python3 docker-train.py" >> run.sh
RUN echo "python3 server.py" >> run.sh
RUN chmod +x run.sh
EXPOSE 10046
CMD ["./run.sh"]