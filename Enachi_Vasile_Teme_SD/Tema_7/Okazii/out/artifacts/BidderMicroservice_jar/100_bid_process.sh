#!/bin/bash

for i in {1..100}
do
	echo "Starting $i bid"
	java -jar BidderMicroservice.jar &
	pids[${i}]=$!
done

# wait for all pids
echo "Now we wait"
for pid in ${pids[*]}; do
    wait $pid
done




