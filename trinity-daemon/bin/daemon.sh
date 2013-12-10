#!/bin/sh

JSVC=./jsvc
COLOR_RED=$'\e[0;31m'
COLOR_GREEN=$'\e[0;32m'
COLOR_RESET=$'\e[0m'

THIS_DIR="$(dirname "${0}")"
DAEMON_CLASS_NAME=net.gree.cobit.message.application.api.external.ApiServerApplication

if [ ! -f ${THIS_DIR}/env.sh ]; then
  echo "[error] ${COLOR_RED}failed${COLOR_RESET}: env file is not exist"
  exit 1
fi

. ${THIS_DIR}/env.sh

function start() {
  echo "[info] starting jsvc ..."

  status
  if [ $? -eq 0 ]; then
    echo "[error] ${COLOR_RED}failed${COLOR_RESET}: jsvc has already started"
    exit 1
  fi

  $JSVC \
    -home ${JAVA_HOME} \
    -cp ${CLASS_PATH} \
    -pidfile ${PID_FILE} \
    -outfile ${STDOUT_FILE} \
    -errfile ${STDERR_FILE} \
    -Dconfig.file=${APPLICATION_CONF_FILE} \
    -Dlogback.configurationFile=${LOGBACK_CONF_FILE} \
    ${JAVA_OPTS} \
    ${DAEMON_CLASS_NAME} \
    `pwd`
  JSVC_STARTED=$?

  sleep 5
  status
  if [ ${JSVC_STARTED} -ne 0 -o $? -ne 0 ]; then
    echo "[error] ${COLOR_RED}failed${COLOR_RESET}: failed to start jsvc"
    exit 2
  fi

  echo "[info] ${COLOR_GREEN}success${COLOR_RESET}: jsvc has started"

  return 0
}

function stop() {
  echo "[info] stopping jsvc ..."

  jsvc \
    -stop \
    -pidfile ${PID_FILE} \
    ${DAEMON_CLASS_NAME}
  JSVC_STOPPED=$?

  sleep 5
  status
  if [ ${JSVC_STOPPED} -ne 0 -o $? -eq 0 ]; then
    echo "[error] ${COLOR_RED}failed${COLOR_RESET}: failed to stopped jsvc"
    exit 2
  fi

  echo "[info] ${COLOR_GREEN}success${COLOR_RESET}: jsvc has stopped"

  return 0
}

function status() {
  # TODO: pidfileにある番号のプロセスが起動しているか確認
  if [ -f ${PID_FILE} ]; then
    return 0
  else
    return 1
  fi
}

case "${1}" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "restart")
    stop
    start
    ;;
  "status")
    status
    if [ $? -eq 0 ]; then
      echo "[info] jsvc is running"
    else
      echo "[info] jsvc is stopped"
    fi
    ;;
  *)
    echo "[error] ${COLOR_RED}failed${COLOR_RESET}: usage $0 start/stop/restart/status"
    exit 1
    ;;
esac

exit 0
