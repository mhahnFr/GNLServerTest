#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

#include "connection.h"


void GNLClient_Banner()
{
	printf("\n");
	printf("    GNLClient v0.0.1\n");
	printf("    by (mhahn, enijakow)@student.42heilbronn.de\n");
	printf("\n");
}


bool GNLClient_ParseArgs(int argc, char** argv, const char** address, unsigned int* port)
{
	if (argc != 3) return false;

	*address = argv[1];
	*port    = atoi(argv[2]);

	return true;
}



void GNLClient_Run(const char* address, unsigned int port)
{
	struct GNLClient_Connection*  connection;

	connection = GNLClient_Connection_New(address, port);
	             printf("Connection is %p\n", connection);
	             GNLClient_Connection_Delete(connection);
}


int main(int argc, char** argv)
{
	const char*   address;
	unsigned int  port;

	GNLClient_Banner();

	if (GNLClient_ParseArgs(argc, argv, &address, &port)) {
		GNLClient_Run(address, port);
	} else {
		printf("usage: %s address port\n", argv[0]);
	}

	return 0;
}

