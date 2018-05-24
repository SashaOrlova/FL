#!/bin/bash

rootdir=$(pwd)
cd $rootdir/FL_Parser
./start.sh compile
./start.sh run prog
