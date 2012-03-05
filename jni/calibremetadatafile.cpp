#include "calibremetadatafile.h"
#include <string>
#include "CppSQLite3.h"
#include <ctime>
#include <iostream>

const char* CalibreMetadataFile::dbFilename="calibreBooks.db";
const char* CalibreMetadataFile::lineEndings = "[{,}]\"";

CalibreMetadataFile::CalibreMetadataFile() {
    //ctor
}

CalibreMetadataFile::~CalibreMetadataFile() {
    //dtor
}

void CalibreMetadataFile::importFile() {

    CppSQLite3DB db;

    std::cout << "SQLite Version: " << db.SQLiteVersion() << std::endl;

    db.open(dbFilename);

    db.execDML("create table emp(empno int, empname char(20));");




}
