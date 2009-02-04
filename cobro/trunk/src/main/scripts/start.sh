JAVA_HOME=/usr/local/jdk1.5.0_04

CLASSPATH=$CLASSPATH:`find . -maxdepth 3 -name '*.jar' -printf '%p:'`

java -ea -classpath "$CLASSPATH" \
	-Xms64m -Xmx1000m -XX:+UseParallelGC
	org.styskin.ca.CriteriaAnalysis