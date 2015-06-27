#!/bin/sh
"$@"

gfsh -e "connect --locator=192.168.99.100[10334]" -e "configure pdx --read-serialized=true --disk-store"

while true; do
  sleep 10
done
