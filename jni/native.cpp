#include <cerrno>
#include <cstddef>
#include <string>

#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <errno.h>

#include <android/log.h>

#include <sqlite3.h>

#include "mysqlite.h"
#include "calibremetadatafile.h"

#define DEBUG_TAG "NDK_AndroidNDK1SampleActivity"

#ifdef __cplusplus
extern "C" {
#endif

void Java_uk_co_droidinactu_CalibreImportIntf_importFile(JNIEnv * env,
		jobject javaThis, jstring metadataFilename, jstring dbFilename) {
	jboolean isCopy;
	const char * metadataFilenamePtr = env->GetStringUTFChars(metadataFilename,
			NULL);
	const char * dbFilenamePtr = env->GetStringUTFChars(metadataFilename, NULL);

	__android_log_print(ANDROID_LOG_DEBUG, DEBUG_TAG, "NDK:LC: [%s]",
			metadataFilenamePtr);

	env->ReleaseStringUTFChars(metadataFilename, metadataFilenamePtr);
	env->ReleaseStringUTFChars(dbFilename, dbFilenamePtr);

	//Open Database for writing
	sqlite3 *db;
	int rc = sqlite3_open(dbFilenamePtr, &db);

	CalibreMetadataFile* importer = new CalibreMetadataFile();
	importer->Setfilename(metadataFilenamePtr);
	importer->Setdbname(dbFilenamePtr);

//open calibre metadata file

//loop over file

//read in record

	//write record to db

	//loop

	//close metadata file

	//close db
	sqlite3_close(db);

	//end
}

sqlite3* openDatabase(const char* dbFilename) {
	sqlite3* db;
	int rc;
	rc = sqlite3_open(dbFilename, &db);
	if (rc) {
		fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
		sqlite3_close(db);
		return (0);
	}
	return db;
}

void Java_uk_co_droidinactu_CalibreImportIntf_helloLog(JNIEnv * env,
		jobject javaThis, jstring logThis) {
	jboolean isCopy;
	const char * szLogThis = env->GetStringUTFChars(logThis, NULL);

	__android_log_print(ANDROID_LOG_DEBUG, DEBUG_TAG, "NDK:LC: [%s]",
			szLogThis);

	env->ReleaseStringUTFChars(logThis, szLogThis);
}

jstring Java_uk_co_droidinactu_CalibreImportIntf_invokeNativeFunction(
		JNIEnv* env, jobject javaThis) {
	return env->NewStringUTF("Hello from native code!");
}

jstring Java_uk_co_droidinactu_CalibreImportIntf_getString(JNIEnv * env,
		jobject javaThis, jint value1, jint value2) {
	const char *szFormat = "The sum of the two numbers is: %i";
	char * szResult;

	// add the two values
	jlong sum = value1 + value2;

	// malloc room for the resulting string
	szResult = (char *) malloc(sizeof(szFormat) + 20);

	// standard sprintf
	sprintf(szResult, szFormat, sum);

	// get an object string
	jstring result = env->NewStringUTF(szResult);

	// cleanup
	free(szResult);

	return result;
}

#ifdef __cplusplus
}
#endif
