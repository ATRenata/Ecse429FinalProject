#!/bin/bash

#copy MutationTesting
cp -rf MathDummy.java SUT.java
sed -i -e 's/MathDummy/SUT/g' SUT.java
