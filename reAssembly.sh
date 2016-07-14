#!/bin/bash
sbt assembly
sbt docker
cd docker
./publish_sentiment_docker.sh
