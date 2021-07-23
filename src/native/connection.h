#ifndef GNLCLIENT_CONNECTION_H
#define GNLCLIENT_CONNECTION_H

#define GNLHeader "../../../get_next_line/get_next_line.h"

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

#endif