# Vars

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
# include Class files of inner classes.
JAVA_OBJ	=	de/heilbronn42/mhahn/Connection*.class \
				de/heilbronn42/mhahn/Server*.class \
				de/heilbronn42/mhahn/ServerFrame*.class

# The C object files.
C_OBJ		=	native/connection_test.o \
				native/connection.o \
				native/ft_strjoin.o \
				native/main.o

# Checks for updates after that compiles and runs the test.
all: update compile runTest

# Runs the test. Does not compile anything.
runTest:
	nohup java -jar $(JAVA_EXE) 42 &
	sleep 1
	./$(C_EXE) 127.0.0.1 42

# Compiles the C Client and the Java Server, if needed.
compile: compJava compC

# Compiles the part written in Java of the project.
compJava:
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
