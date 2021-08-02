# --- Variables --- #

# The path to your get_next_line. Please provide it as 
# absolute path and without trailing slash. Note: does
# not work properly with relative pathes!
GET_NEXT_LINE_PATH = /Users/mhahn/Documents/gnljzhoumax

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
GNL_FILES	=	$(GET_NEXT_LINE_PATH)/get_next_line.c #\
				$(GET_NEXT_LINE_PATH)/get_next_line_utils.c # <-- Include this line for later use!

# The Java source files.
JAVA_SRC	=	de/heilbronn42/mhahn/Connection.java \
				de/heilbronn42/mhahn/Server.java \
				de/heilbronn42/mhahn/ServerFrame.java

# The C source files.
C_SRC	=		native/connection_test.c \
				native/connection.c \
				native/ft_strjoin.c \
				native/main.c \
				../../Libft/libft.a # <-- Remove this for later use, debug only!!!

# The Java .class files. The star before the dot is needed to also 
# include class files of inner classes.
JAVA_OBJ	=	de/heilbronn42/mhahn/Connection*.class \
				de/heilbronn42/mhahn/Server*.class \
				de/heilbronn42/mhahn/ServerFrame*.class

# The C object files.
C_OBJ		=	native/connection_test.o \
				native/connection.o \
				native/ft_strjoin.o \
				native/main.o


# --- Rules --- #

# Checks for updates, recompiles the client and runs the tests.
test: update compC runTest

# Checks for updates, compiles everything and runs the test.
all: update compile runTest

# Checks wether there is a JRE installed.
.phony: checkJava
checkJava:
	$(if $(command -v java),$(error No JRE installed, please install either a JRE or a JDK),)

# Checks wether there is a JDK installed.
.phony: checkJavac
checkJavac:
	$(if $(command -v javac),$(error No JDK installed, if you wish to compile the server, please install a JDK (min. v: 8)),)

# Runs the test.  Does not compile anything.
runTest: runServerIfNotRunning runTestNoJava

# Only starts the server if it is not already running. Also checks
# wether a JRE is present. Waits a second for Java to start up.
runServerIfNotRunning: checkJava
ifeq ($(shell ps | grep $(JAVA_EXE) | wc -l | xargs),2)
	nohup java -jar $(JAVA_EXE) $(PORT_NO) &
	sleep 1
endif

# Runs the test without starting the server.
runTestNoJava:
	./$(C_EXE) $(IP_ADDRESS) $(PORT_NO)

# Compiles the C Client and the Java Server.
compile: compJava compC

# Compiles the part written in Java of the project.
compJava: checkJavac
	javac $(JAVA_SRC)
	jar -cfm $(JAVA_EXE) $(JAVA_MF) $(JAVA_OBJ)

# Compiles the part written in C of the project.
compC:
	$(CC) $(CFLAGS) $(C_SRC) $(GNL_FILES) -o $(C_EXE) -D BUFFER_SIZE=$(BUFFER_SIZE) -D GNLHeader=\"$(GET_NEXT_LINE_PATH)/get_next_line.h\"

# Checks wether to perform an update. !!!TODO!!!
update:

# Remove all temporary files.
clean:
	$(RM) $(JAVA_OBJ) $(C_OBJ) *~ nohup.out

# Removes everything created by this Makefile.
fclean: clean
	$(RM) $(JAVA_EXE) $(C_EXE)

# Cleans and recompiles everything.
re: fclean all
