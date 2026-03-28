FROM nginx:1.25

RUN apt-get update && apt-get install -y curl

RUN echo "server {" > /etc/nginx/conf.d/status.conf && \
    echo "    listen 80;" >> /etc/nginx/conf.d/status.conf && \
    echo "    location /nginx_status {" >> /etc/nginx/conf.d/status.conf && \
    echo "        stub_status;" >> /etc/nginx/conf.d/status.conf && \
    echo "        allow all;" >> /etc/nginx/conf.d/status.conf && \
    echo "    }" >> /etc/nginx/conf.d/status.conf && \
    echo "}" >> /etc/nginx/conf.d/status.conf

COPY nginx.conf /etc/nginx/nginx.conf
COPY index.html /usr/share/nginx/html/index.html

