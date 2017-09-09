#!/bin/bash
# © All rights reserved. ECOLE POLYTECHNIQUE FEDERALE DE LAUSANNE,
# Switzerland, Laboratory of Experimental Biophysics, 2017
# See the LICENSE.txt file for more details.
#
# Tests to ensure that each script runs without errors.
#
# Note that this script does not check the results of the scripts,
# but rather that they run to completion.
#
# Inputs
# ------
# 1. The directory containing the SASS scripts
# 2. The version of SASS to use (for example, enter 0.2.1 at the
#    command line). The appropriate .jar file will be downloaded.
#
# Kyle M. Douglass, 2017
#

test_dir=$(readlink -f $1)
sass=$(readlink -f $2)

# Print information about the test
echo "Testing execution of scripts in directory: $test_dir"
echo "SASS: $sass"

java -version

# Download desired version of SASS to a temporary directory
temp_dir=$(mktemp -d)

# Change to the temporary directory to run tests there
cd $temp_dir

for file in $test_dir/*
do

    if echo $file | grep -q ".bsh"
    then
	echo "Running script: $file"
	java -jar $sass -s $file

	# Check whether the script ran successfully exit if not.
	if [ $? -eq 0 ]
	then
	    echo "Script ran sucessfully."
	else
	    echo "The script failed." >&2
	    rm -rf $temp_dir
	    exit 1
	fi
    fi

done

# Clean up
rm -rf $temp_dir

exit 0
