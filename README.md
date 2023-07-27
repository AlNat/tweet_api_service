# tweet api service

Simple MVP for primitive twitter-alternative service
This is a BFF (backend for frontend), recives tweet and send it with Kafka to save-app


### HLD

Request --> REST --> API service ---> Kafka ---> Save-service -> Elastic

For save see https://github.com/AlNat/tweet_save_service


### Tests

Implements with Kafka in TestContainer and mocked consumer which receives messages for the real save app

Also, there are few mapping test
