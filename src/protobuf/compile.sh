#!/bin/bash

workdir=$(dirname $(pwd $0))


protoc --java_out=$workdir/main/java market.proto