FROM nginx:stable-perl

RUN #rm /etc/nginx/conf.d/default.conf

#COPY config/default.conf /etc/nginx/conf.d/default.conf

RUN ls -l /etc/nginx/conf.d/

ENTRYPOINT ["nginx", "-g", "daemon off;"]