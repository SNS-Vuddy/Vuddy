
FROM nginx:alpine

ARG name

COPY nginx-conf/default.conf /etc/nginx/conf.d/default.conf

CMD [ "/bin/sh" , "-c" , "envsubst '$SERVER_HOSTNAME' < /etc/nginx/conf.d/default.conf > /etc/nginx/conf.d/default.conf && exec nginx -g 'daemon off;'" ]
