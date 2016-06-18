#!/bin/bash
git clone git@github.com:rdelao/sentiment-web.git &&
docker build -t sentimentweb:latest ./sentiment-web
rm -rf sentiment-web
