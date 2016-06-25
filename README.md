# bigDON
Creators: Ryan De La O & Tyler Lynch

# Running dockers

Install docker and docker-compose

## Development in IntelliJ

When developing in intellij, type:

    $ cd ./docker/development
    $ docker-compose up -d

and then change the DatabaseIp variable in the MongoConstants to "localhost"

and then change the RequestLocation IP address to "localhost" in FastSentimentSclassifier

## Production
type:
    $ sbt assembly
    $ sbt docker
    $ cd docker
    $ ./publish_sentiment_docker.sh
    $ cd production
    $ docker-compose up -d

it will output to `/srv/local/bigdon` by default but you may change
this in the docker-compose.yml file
