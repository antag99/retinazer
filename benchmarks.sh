if [ "$1" = "--debug" ]; then
    PARAMS="-i 1 -wi 1 -r 1 -p entityCount=4096"
elif [ "$1" == "--dev" ]; then
    mvn install -DskipTests=true
    java -jar retinazer-benchmarks/target/microbenchmarks.jar .*\\.retinazer\\..* \
      -rf json -rff retinazer-benchmarks/target/results-dev.json $PARAMS | \
      tee retinazer-benchmarks/target/results-dev.log
    exit 0
else
    echo "Running all benchmarks; this will take some time. Use the --debug switch for testing."
fi

start_time=`date +%s`
mvn clean
mkdir retinazer-benchmarks/target
# Run all benchmarks, write results in json format to results.json,
# and write the full log to results.log.
mvn install -DskipTests=true \
  -Dmaven.javadoc.skip=true \
  -Dartemis.optimizeEntitySystems=true \
  -Dartemis.enablePooledWeaving=true \
  -Dartemis.enablePackedWeaving=true | \
  tee retinazer-benchmarks/target/build.log
java -jar retinazer-benchmarks/target/microbenchmarks.jar .* \
  -rf json -rff retinazer-benchmarks/target/results.json $PARAMS | \
  tee retinazer-benchmarks/target/results.log

end_time=`date +%s`
total_time=$((end_time-start_time))

echo "Done! Took $total_time seconds."
