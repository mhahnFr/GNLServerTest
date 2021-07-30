#ifndef GNLCLIENT_CONNECTION_H
#define GNLCLIENT_CONNECTION_H

#include <stdbool.h>

/*
 * The Connection object. It includes the address to which to connect
 * as well as the file descriptor of the connection.
 */
struct GNLClient_Connection;

/*
 * Allocates and returns a new GNLClient_Connection using the provided address
 * and port. Return either the new object or NULL if the allocation fails.
 */
struct GNLClient_Connection*  GNLClient_Connection_New(const char*, int);
/*
 * Deletes the given object. Closes the established connection if present.
 */
void                          GNLClient_Connection_Delete(struct GNLClient_Connection*);
/*
 * Reads a line from the stream. Returns NULL when nothing can be read.
 */
char*                         GNLClient_Connection_ReadLine(struct GNLClient_Connection*);
/*
 * Writes the given text to the stream.
 */
void                          GNLClient_Connection_WriteLine(struct GNLClient_Connection*, const char*);
/*
 * Reads all the text sent by the server using the get_next_line function. It
 * compares the read text to the original test files.
 */
void                          GNLClient_Connection_Test(struct GNLClient_Connection*);
/*
 * Constructs the path to the test files. Returns the created path. The parameter
 * is used to store a pointer to the digit in the path, so that it can be easily
 * incremented.
 */
char*                         GNLClient_Connection_CreatePath(char**);
/*
 * Compares the sent file read with the get_next_line function to the original
 * text read from the given file using the system functions. It prints then the
 * result of the test.
 */
void                          GNLClient_Connection_TestNo(struct GNLClient_Connection*, char*, int);
/*
 * Prints the given result of the given test number.
 */
void                          GNLClient_Connection_PrintResult(int, bool);
/*
 * Appends a newline character to the given line if needed.
 */
void                          GNLClient_Connection_EditSystemLine(char**);
#endif