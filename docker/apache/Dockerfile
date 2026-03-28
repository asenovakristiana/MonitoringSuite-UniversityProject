FROM httpd:2.4

RUN apt-get update && apt-get install -y curl

RUN sed -i 's/#LoadModule status_module/LoadModule status_module/' /usr/local/apache2/conf/httpd.conf

RUN echo "<Location /server-status>" >> /usr/local/apache2/conf/httpd.conf && \
    echo "    SetHandler server-status" >> /usr/local/apache2/conf/httpd.conf && \
    echo "    Require all granted" >> /usr/local/apache2/conf/httpd.conf && \
    echo "</Location>" >> /usr/local/apache2/conf/httpd.conf

COPY httpd.conf /usr/local/apache2/conf/httpd.conf
COPY index.html /usr/local/apache2/htdocs/index.html
