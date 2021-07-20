#ifndef GNL_SERVER_TEST_H
#define GNL_SERVER_TEST_H
/*
 * Tries to connect to the server on the given port. Returns the file
 * descriptor for the connect stream, or -1 in case of error. Connects
 * to the given address.
 */
char establishConnection(int port, char *addr);
#endif