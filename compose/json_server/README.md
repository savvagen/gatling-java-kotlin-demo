## Run Json Server in Docker container


### 1. Build and run your own json-server microservice

1. Build json-server image
``` 
cd json_server

docker build -t my/json-server:latest -f Dockerfile .

```
2. Run json-server
``` 

docker run -d --name json-server -p 3000:3000 my/json-server:latest

## OR USE docker-compose
cd json_server
docker-compose up -d
```
Json server will be running at: http://localhost:3000


### 2. Using existing image:

```
docker run -d -p 80:80 -v /json_server/db.json:/data/db.json clue/json-server

```

### 3. Other json-server images

https://hub.docker.com/r/williamyeh/json-server/
https://hub.docker.com/r/vimagick/json-server
https://hub.docker.com/r/franciscocpg/json-server/