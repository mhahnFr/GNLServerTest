#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <unistd.h>

#include "connection.h"

/*
 * Prints an info banner of this program.
 */
void GNLClient_Banner()
{
	printf("\n");
	printf("    GNLClient v0.1\n");
	printf("    by (mhahn, enijakow)@student.42heilbronn.de\n");
	printf("\n");
}

/*
 * Interpretes the arguments. Returns wether the conversion was successful.
 * Arguments: the argument count, the array with the arguments, a pointer
 * to the address to be filled, a pointer to the port number to be filled.
 */
bool GNLClient_ParseArgs(int argc, char** argv, const char** address, int* port)
{
	if (argc != 3) return false;

	*address = argv[1];
	*port    = atoi(argv[2]);

	if (*port == -1) return false;

	return true;
}

/*
 * Creates a new connection to the specified address using the specified
 * port.
 */
void GNLClient_Run(const char* address, int port)
{
	struct GNLClient_Connection*  connection;

	connection = GNLClient_Connection_New(address, port);
	if (connection == NULL) {
		printf("Could not connect to %s:%d\n", address, port);
		return;
	}
	GNLClient_Connection_Test(connection);
	GNLClient_Connection_Delete(connection);
}

int main(int argc, char** argv)
{
	const char*  address;
	int          port;

	GNLClient_Banner();

	if (GNLClient_ParseArgs(argc, argv, &address, &port)) {
		GNLClient_Run(address, port);
	} else {
		printf("usage: %s <address> <port>\n", argv[0]);
	}

	return 0;
}