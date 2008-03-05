#!/bin/sh
SERVANT=vkontakte
log_rc=$SERVANT.log

. $SERVANT.conf

$exec >> $log_rc 2>&1 &

echo $! > $SERVANT.pid 
 