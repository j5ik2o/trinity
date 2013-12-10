# TODO: サーバー構成が決定したら見直し
# 各変数はフルパスで記載すること
JAVA_HOME=/Library/Java/JavaVirtualMachines/current/Contents/Home
JAVA_OPTS="-Xms1024m -Xmx2048m -XX:MaxPermSize=512m"
CLASS_PATH="/Users/junichi.kato/HomeProject/trinity/trinity-daemon-test/target/scala-2.10/trinity-daemon-test-assembly-1.0.0-SNAPSHOT.jar"
PID_FILE=/tmp/trinity.pid
STDOUT_FILE=/tmp/trinity_stdout.txt
STDERR_FILE=/tmp/trinity_stderr.txt
APPLICATION_CONF_FILE=/Users/junichi.kato/HomeProject/trinity/trinity-daemon-test/conf/application.conf
LOGBACK_CONF_FILE=/Users/junichi.kato/HomeProject/trinity/trinity-daemon-test/conf/logback.xml
