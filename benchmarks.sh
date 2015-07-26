mvn clean install
# Run all benchmarks, write results in json format to results.json,
# and write the full log to results.log.
java -jar retinazer-benchmarks/target/microbenchmarks.jar .* \
  -rf json -rff retinazer-benchmarks/target/results.json | \
  tee retinazer-benchmarks/target/results.log
