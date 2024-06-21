#!/bin/bash

# Directory containing subdirectories with JAR files
main_directory="/home/student/Documents/SD_Laborator_07/Okazii/out/artifacts/"

run_jars() {
    local directory="$1"

    jar_directories=(
        "MessageProcessorMicroservice_jar"
        "BiddingProcessorMicroservice_jar"
        "AuctioneerMicroservice_jar"
    )

    for jar_dir in "${jar_directories[@]}"; do
        if [[ -d "$directory/$jar_dir" ]]; then
            local jar_files=("$directory/$jar_dir"/*.jar)
            if [ ${#jar_files[@]} -gt 0 ]; then
                for jar_file in "${jar_files[@]}"; do
                    echo "Executing $jar_file..."
                    java -jar "$jar_file" &
                done
            else
                echo "No JAR files found in $directory/$jar_dir"
            fi
        else
            echo "Directory $directory/$jar_dir not found"
        fi
    done
    
    sleep 2
    
   for entry in "$directory"/BidderMicroservice_jar/*.jar; do
        if [[ -f "$entry" ]]; then
            for (( i=0; i<=20; i++ )); do
                echo "Executing $entry...$i"
                java -jar "$entry" &
            done
        else
            echo "No JAR files found in $entry"
        fi
    done

    
}

run_jars "$main_directory"

wait
