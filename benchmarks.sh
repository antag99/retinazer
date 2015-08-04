if [ "$1" = "--debug" ]; then
    PARAMS="-i 1 -wi 1 -r 1 -p entityCount=4096"
else
    echo "Running all benchmarks; this will take some time. Use the --debug switch for testing."
fi

mvn clean
mkdir retinazer-benchmarks/target
# Run all benchmarks, write results in json format to results.json,
# and write the full log to results.log.
mvn install -DskipTests=true | \
  tee retinazer-benchmarks/target/build-plain.log
java -jar retinazer-benchmarks/target/microbenchmarks.jar .* \
  -rf json -rff retinazer-benchmarks/target/results-plain.json $PARAMS | \
  tee retinazer-benchmarks/target/results-plain.log

# Run artemis-odb benchmarks with various weaving configurations
mvn install -DskipTests=true -Dartemis.optimizeSystems=true | \
  tee retinazer-benchmarks/target/build-plain-fast.log
java -jar retinazer-benchmarks/target/microbenchmarks.jar .*\\.artemis\\..* \
  -rf json -rff retinazer-benchmarks/target/results-plain-fast.json $PARAMS | \
  tee retinazer-benchmarks/target/results-plain-fast.log

mvn install -DskipTests=true -Dartemis.optimizeSystems=false -Dartemis.enablePooledWeaving=true | \
  tee retinazer-benchmarks/target/build-pooled.log
java -jar retinazer-benchmarks/target/microbenchmarks.jar .*\\.artemis\\..* \
  -rf json -rff retinazer-benchmarks/target/results-pooled.json $PARAMS | \
  tee retinazer-benchmarks/target/results-pooled.log

mvn install -DskipTests=true -Dartemis.optimizeSystems=false -Dartemis.enablePackedWeaving=true | \
  tee retinazer-benchmarks/target/build-packed.log
java -jar retinazer-benchmarks/target/microbenchmarks.jar .*\\.artemis\\..* \
  -rf json -rff retinazer-benchmarks/target/results-packed.json $PARAMS | \
  tee retinazer-benchmarks/target/results-packed.log

mvn install -DskipTests=true -Dartemis.optimizeSystems=true -Dartemis.enablePooledWeaving=true | \
  tee retinazer-benchmarks/target/build-pooled-fast.log
java -jar retinazer-benchmarks/target/microbenchmarks.jar .*\\.artemis\\..* \
  -rf json -rff retinazer-benchmarks/target/results-pooled-fast.json $PARAMS | \
  tee retinazer-benchmarks/target/results-pooled-fast.log

mvn install -DskipTests=true -Dartemis.optimizeSystems=true -Dartemis.enablePackedWeaving=true | \
  tee retinazer-benchmarks/target/build-packed-fast.log
java -jar retinazer-benchmarks/target/microbenchmarks.jar .*\\.artemis\\..* \
  -rf json -rff retinazer-benchmarks/target/results-packed-fast.json $PARAMS | \
  tee retinazer-benchmarks/target/results-packed-fast.log
