
FROM nginx

COPY nginx-conf/default.conf /etc/nginx/conf.d/mysite.template

CMD [ "/bin/sh" , "-c" , "envsubst '$SERVER_HOSTNAME' < /etc/nginx/conf.d/mysite.template > /etc/nginx/conf.d/default.conf && exec nginx -g 'daemon off;'" ]
