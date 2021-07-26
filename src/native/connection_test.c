#include <stdio.h>
#include <string.h>

#include "connection.h"

#define PATH = "../testData/"
#define FILE_NAME = "test"
#define TEST_COUNT = 9

void GNLClient_Connection_Test(struct GNLClient_Connection* this)
{

}

/*
 * Compares the sent file read with the get_next_line function to the original
 * text read from the file using the system functions.
 */
void GNLClient_Connection_TestNo(struct GNLClient_Connection* this, int testNumber)
{
	char* gnl = GNLClient_Connection_ReadLine(this);
	char* ogl = NULL;
	FILE* tf = fdopen("../testData/test1.txt", "r");
	gnl = GNLClient_Connection_ReadLine(this);
	ssize_t count = getline(&ogl, 0, tf);
	while (gnl != NULL && count != -1)
	{
		if (!strcmp(gnl, ogl)) {
			// TODO Test failed
		}
	}
}