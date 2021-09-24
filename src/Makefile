# --- Variables --- #

# The path to your get_next_line. The default path is the parent directory of
# this tester; you can simply edit it if needed.
GET_NEXT_LINE_PATH = ../../

# The name of the Java executable.
JAVA_EXE	=	server.jar

# The name of the C executable.
C_EXE		=	client.exe

# The path to the Java manifest.
JAVA_MF		=	de/heilbronn42/mhahn/manifest.mf

# The buffer size to be used by the get_next_line.
BUFFER_SIZE	=	42

# The port number to be used to connect to the server.
PORT_NO		=	42

# The IP address to connect to. The server should be running on that machine.
IP_ADDRESS	=	127.0.0.1

# The source files of the get_next_line project.
GNL_FILES	=	$(GET_NEXT_LINE_PATH)/get_next_line.c \
				$(GET_NEXT_LINE_PATH)/get_next_line_utils.c

# The Java source files. The star before the dot is needed to also include
# class files of inner classes.
JAVA_SRC	=	de/heilbronn42/mhahn/Connection.java \
				de/heilbronn42/mhahn/Server.java \
				de/heilbronn42/mhahn/ServerFrame.java

# The Java class files for inner classes of the class ServerFrame.
JAVA_IOBJ	=	de/heilbronn42/mhahn/ServerFrame\$$1.class \
				de/heilbronn42/mhahn/ServerFrame\$$2.class

# The source file that has some inner classes.
JAVA_ISRC	=	de/heilbronn42/mhahn/ServerFrame.java

# The C source files.
C_SRC	=		native/connection_test.c \
				native/connection.c \
				native/stringJoiner.c \
				native/main.c

# A macro to generate the java class files.
JAVA_OBJ	=	$(patsubst %.java,%.class,$(JAVA_SRC))

# A macro to generate the object files.
C_OBJ		=	$(patsubst %.c,%.o,$(C_SRC))

# A macro to generate the object files for the GNL source files.
GNL_OBJ		=	$(patsubst %.c,%.o,$(GNL_FILES))

# The flags to be used for the compilation of the C files.
CFLAGS		=	-Wall -Werror -Wextra


# --- Rules --- #

# Checks for updates, and runs the tests.
.phony: test
test: update runTest

# Checks for updates, and runs the test without starting the server.
.phony: noServer
noServer: update runTestNoJava

# Checks for updates, and runs the test.
.phony: all
all: update runTest

# Checks wether there is a JRE installed.
.phony: checkJava
checkJava:
	$(if $(command -v java),$(error No JRE installed, please install either a JRE or a JDK (min. v: 8)),)

# Checks wether there is a JDK installed.
.phony: checkJavac
checkJavac:
	$(if $(command -v javac),$(error No JDK installed, if you wish to compile the server, please install a JDK (min. v: 8)),)

# Runs the test. Does not compile anything.
.phony: runTest
runTest: runServerIfNotRunning runTestNoJava

# Only starts the server if it is not already running. Also checks
# wether a JRE is present. Waits a second for Java to start up.
.phony: runServerIfNotRunning
runServerIfNotRunning: checkJava $(JAVA_EXE)
ifeq ($(shell ps | grep $(JAVA_EXE) | wc -l | xargs),2)
	nohup java -jar $(JAVA_EXE) $(PORT_NO) &
	sleep 1
endif

# Runs the test without starting the server.
.phony: runTestNoJava
runTestNoJava: $(C_EXE)
	./$(C_EXE) $(IP_ADDRESS) $(PORT_NO)

# Compiles each C file individually.
%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $< -D BUFFER_SIZE=$(BUFFER_SIZE) -I $(GET_NEXT_LINE_PATH)

# Compiles each Java file individually.
%.class: %.java
	javac $<

# Compiles the part written in Java of the project.
$(JAVA_EXE): $(JAVA_OBJ)
	jar -cfm $(JAVA_EXE) $(JAVA_MF) $(JAVA_IOBJ) $(JAVA_OBJ)

# Compiles the part written in C of the project.
$(C_EXE): $(C_OBJ) $(GNL_OBJ)
	$(CC) $(CFLAGS) -o $(C_EXE) $(C_OBJ) $(GNL_OBJ)

# Checks wether to perform an update.
.phony: update
update:
	- ./update.sh

# Remove all temporary files.
.phony: clean
clean:
	- $(RM) $(JAVA_OBJ) $(C_OBJ) $(GNL_OBJ) $(JAVA_IOBJ) *~ nohup.out

# Removes everything created by this Makefile. Stops the server if it is
# running.
.phony: fclean
fclean: clean
	- kill $(shell ps | grep $(JAVA_EXE) | head -n 1 | awk '{print $$1}')
	- $(RM) $(JAVA_EXE) $(C_EXE)

# Cleans and recompiles everything.
.phony: re
re: fclean all

