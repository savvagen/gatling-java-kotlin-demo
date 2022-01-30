FROM node:12

COPY db.json /tmp/json_server/db.json
COPY index.js /tmp/json_server/index.js
COPY routes.json /tmp/json_server/routes.json
COPY middlewares/hello.js /tmp/json_server/hello.js
COPY middlewares/date.js /tmp/json_server/date.js
COPY middlewares/auth.js /tmp/json_server/auth.js
COPY middlewares/log.js /tmp/json_server/log.js
COPY middlewares/token.js /tmp/json_server/token.js

RUN npm install -g json-server

EXPOSE 3000

CMD json-server --watch /tmp/json_server/index.js \
    --routes /tmp/json_server/routes.json \
    --middlewares /tmp/json_server/hello.js /tmp/json_server/date.js /tmp/json_server/auth.js /tmp/json_server/log.js /tmp/json_server/token.js \
    --port 3000 --host 0.0.0.0