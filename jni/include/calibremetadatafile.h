#ifndef CALIBREMETADATAFILE_H
#define CALIBREMETADATAFILE_H

#include <string>
#include "CppSQLite3.h"
#include <ctime>
#include <iostream>

class CalibreMetadataFile {
public:

    static const char* dbFilename;
    static const char* lineEndings;

    /** Default constructor */
    CalibreMetadataFile();

    /** Default destructor */
    virtual ~CalibreMetadataFile();

    /** Access m_filename
     * \return The current value of m_filename
     */
    std::string Getfilename() {
        return m_filename;
    }

    /** Set m_filename
     * \param val New value to set
     */
    void Setfilename(std::string val) {
        m_filename = val;
    }

    /** Access m_dbname
     * \return The current value of m_dbname
     */
    std::string Getdbname() {
        return m_dbname;
    }

    /** Set m_dbname
     * \param val New value to set
     */
    void Setdbname(std::string val) {
        m_dbname = val;
    }

    /** import the file content into a database
     */
    void importFile();
protected:
private:
    std::string m_filename; //!< Member variable "m_filename"
    std::string m_dbname; //!< Member variable "m_dbname"
};

#endif // CALIBREMETADATAFILE_H
