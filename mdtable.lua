--[[
  Generates a pretty Markdown table for the benchmark results
]]

local json = require "dkjson"

local function getBenchmarks(path)
  -- JMH emits raw JSON arrays, which isn't tolerated by most parsers. A
  -- workaround is to wrap the content in an object.
  return assert(json.decode("{\"value\":" .. assert(io.open(path)):read("*a") .. "}")).value
end

local frameworks = {
  { name = "retinazer"  , package = "com.github.antag99.benchmarks.retinazer" },
  { name = "artemis-odb", package = "com.github.antag99.benchmarks.artemis" },
  { name = "ashley"     , package = "com.github.antag99.benchmarks.ashley" },
}

local configurations = {
  { name = "plain"        , value = getBenchmarks "retinazer-benchmarks/target/results-plain.json" },
  { name = "plain (fast)" , value = getBenchmarks "retinazer-benchmarks/target/results-plain-fast.json" },
  { name = "pooled"       , value = getBenchmarks "retinazer-benchmarks/target/results-pooled.json" },
  { name = "pooled (fast)", value = getBenchmarks "retinazer-benchmarks/target/results-pooled-fast.json" },
  { name = "packed"       , value = getBenchmarks "retinazer-benchmarks/target/results-packed.json" },
  { name = "packed (fast)", value = getBenchmarks "retinazer-benchmarks/target/results-packed-fast.json" },
}

-- benchmarks to generate tables for; will search results for matching parameters
local benchmarks = {
  { name = "iteration (small)", class = "IterationBenchmark", method = "benchmarkIteration", params = { entityCount = 32768 } },
  { name = "iteration (huge)" , class = "IterationBenchmark", method = "benchmarkIteration", params = { entityCount = 131072 } },
  { name = "retrieval (small)", class = "RetrievalBenchmark", method = "benchmarkRetrieval", params = { entityCount = 32768 } },
  { name = "retrieval (huge)" , class = "RetrievalBenchmark", method = "benchmarkRetrieval", params = { entityCount = 131072 } },
}

for i = 1, #benchmarks do
  io.write("## " .. benchmarks[i].name .. " ##\n\n")

  -- | ECS | foo | bar |
  io.write("| ECS ")
  for ii = 1, #configurations do
    io.write("| ", configurations[ii].name, " ")
  end
  io.write("|\n")

  -- |-----|-----|-----|
  io.write("|-----")
  for ii = 1, #configurations do
    io.write("|-", configurations[ii].name:gsub(".", "-"), "-")
  end
  io.write("|\n")

  local benchmark = benchmarks[i].class .. "." .. benchmarks[i].method

  for ii = 1, #frameworks do
    -- | bad | 123 | 456 |
    io.write("| ")
    io.write(frameworks[ii].name, " ")

    local package = frameworks[ii].package
    for iii = 1, #configurations do
      local value = configurations[iii].value
      local match = false
      for iiii = 1, #value do
        if value[iiii].benchmark == (package .. "." .. benchmark) then
          match = true
          for k, v in pairs(benchmarks[i].params) do
            if tonumber(value[iiii].params[k]) ~= tonumber(benchmarks[i].params[k]) then
              match = false
            end
          end
          if match then
            io.write("| ", value[iiii].primaryMetric.score, " ")
            break
          end
        end
      end

      if not match then
        io.write("| N/A ")
      end
    end

    io.write("|\n")
  end
  io.write("\n\n")
end
