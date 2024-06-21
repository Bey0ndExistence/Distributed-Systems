java -jar spring-cloud-skipper-server-2.3.2.RELEASE.jar
java -jar spring-cloud-dataflow-server-2.4.2.RELEASE.jar
java -jar spring-cloud-dataflow-shell-2.4.2.RELEASE.jar

app register --name time-source --type source --uri file:////media/enaki/Data/TuIasi/An%203/Semestrul%202/SD/Laboratoare/Lab_9/Tema_9/DataFlow%20Source/target/DataFlowSource-1.0-SNAPSHOT.jar
app register --name time-processor --type processor --uri file:////media/enaki/Data/TuIasi/An%203/Semestrul%202/SD/Laboratoare/Lab_9/Tema_9/DataFlow%20Processor/target/DataFlowProcessor-1.0-SNAPSHOT.jar
app register --name logging-sink --type sink --uri file:////media/enaki/Data/TuIasi/An%203/Semestrul%202/SD/Laboratoare/Lab_9/Tema_9/DataFlow%20Sink/target/DataFlowSink-1.0-SNAPSHOT.jar

stream create --name time-to-log --definition 'time-source | time-processor | logging-sink'

stream undeploy --name time-to-log
stream deploy --name time-to-log

app unregister --name time-source --type source
app unregister --name time-processor --type processor
app unregister --name logging-sink --type sink


